import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import processing.core.*;

public final class VirtualWorld
   extends PApplet
{

   private static final int PROPERTY_KEY = 0;
   private static final String BGND_KEY = "background";
   private static final String MINER_KEY = "miner";
   private static final String OBSTACLE_KEY = "obstacle";
   private static final String ORE_KEY = "ore";
   private static final String SMITH_KEY = "blacksmith";
   private static final String VEIN_KEY = "vein";

   private static final int TIMER_ACTION_PERIOD = 100;

   private static final int VIEW_WIDTH = 640;
   private static final int VIEW_HEIGHT = 480;
   private static final int TILE_WIDTH = 32;
   private static final int TILE_HEIGHT = 32;
   private static final int WORLD_WIDTH_SCALE = 2;
   private static final int WORLD_HEIGHT_SCALE = 2;

   private static final int VIEW_COLS = VIEW_WIDTH / TILE_WIDTH;
   private static final int VIEW_ROWS = VIEW_HEIGHT / TILE_HEIGHT;
   private static final int WORLD_COLS = VIEW_COLS * WORLD_WIDTH_SCALE;
   private static final int WORLD_ROWS = VIEW_ROWS * WORLD_HEIGHT_SCALE;

   private static final String IMAGE_LIST_FILE_NAME = "imagelist";
   private static final String DEFAULT_IMAGE_NAME = "background_default";
   private static final int DEFAULT_IMAGE_COLOR = 0x808080;

   private static final String LOAD_FILE_NAME = "gaia.sav";

   private static final String FAST_FLAG = "-fast";
   private static final String FASTER_FLAG = "-faster";
   private static final String FASTEST_FLAG = "-fastest";
   private static final double FAST_SCALE = 0.5;
   private static final double FASTER_SCALE = 0.25;
   private static final double FASTEST_SCALE = 0.10;

   private static double timeScale = 1.0;

   private ImageStore imageStore;
   private WorldModel world;
   private WorldView view;
   private EventScheduler scheduler;

   private long next_time;

   public void settings()
   {
      size(VIEW_WIDTH, VIEW_HEIGHT);
   }

   /*
      Processing entry point for "sketch" setup.
   */
   public void setup()
   {
      this.imageStore = new ImageStore(
         createImageColored(TILE_WIDTH, TILE_HEIGHT, DEFAULT_IMAGE_COLOR));
      this.world = new WorldModel(WORLD_ROWS, WORLD_COLS,
         createDefaultBackground(imageStore));
      this.view = new WorldView(VIEW_ROWS, VIEW_COLS, this, world,
         TILE_WIDTH, TILE_HEIGHT);
      this.scheduler = new EventScheduler(timeScale);

      loadImages(IMAGE_LIST_FILE_NAME, imageStore, this);
      loadWorld(world, LOAD_FILE_NAME, imageStore);

      scheduleActions(world, scheduler, imageStore);

      next_time = System.currentTimeMillis() + TIMER_ACTION_PERIOD;
   }

   public void draw()
   {
      long time = System.currentTimeMillis();
      if (time >= next_time)
      {
         this.scheduler.updateOnTime(time);
         next_time = time + TIMER_ACTION_PERIOD;
      }

      view.drawViewport();
   }

   public void keyPressed()
   {
      if (key == CODED)
      {
         int dx = 0;
         int dy = 0;

         switch (keyCode)
         {
            case UP:
               dy = -1;
               break;
            case DOWN:
               dy = 1;
               break;
            case LEFT:
               dx = -1;
               break;
            case RIGHT:
               dx = 1;
               break;
         }
         view.shiftView(dx, dy);
      }
   }
   
   public void mousePressed()
   {
      Point pressed = view.mouseToPoint(mouseX/TILE_WIDTH, mouseY/TILE_HEIGHT);
      List<Point> neighbors = PathingStrategy.CARDINAL_NEIGHBORS.apply(pressed).collect(Collectors.toList());
      List<Point> neighbors2 = new LinkedList<Point>();
      for(Point p:neighbors) {
    	  neighbors2.addAll(PathingStrategy.CARDINAL_NEIGHBORS.apply(p).filter(s->!neighbors2.contains(s)).collect(Collectors.toList()));
      }
      neighbors2.addAll(neighbors);
      
      //Check for entity at point
      for (Entity entity : world.getEntities())
      {
          if (entity.getPosition().equals(pressed))
          {
              System.out.println("Entity");
              return;
          }          
      }
      Optional<Entity> nearest = world.findNearest(pressed, Blacksmith.class);
      if(nearest.isPresent()) ((Blacksmith)nearest.get()).transform(world, scheduler, imageStore);
      

	  Soldier soldier = Factory.createSoldier(pressed, imageStore.getImageList("soldier"), (int)Math.random()*100+900, 400);
	  world.tryAddEntity(soldier);
	  soldier.scheduleActions(scheduler, world, imageStore);
	  soldier.executeActivity(world, imageStore, scheduler);
	  for(Point p : neighbors) {
		  if(Math.random()<.25) {
			  Soldier soldier2 = Factory.createSoldier(p, imageStore.getImageList("soldier"), (int)Math.random()*100+900, 400);
			  try {
				  world.tryAddEntity(soldier2);
				  soldier2.scheduleActions(scheduler, world, imageStore);
				  soldier2.executeActivity(world, imageStore, scheduler);
			  } 
			  catch (IllegalArgumentException e) {
				  System.err.println("Attempted to place soldier, " + world.getOccupant(p).toString() + " found instead");				  
			  }
			  
		  }
		  
	  }

      
      
      Random rand = new Random();
      for (Point p : neighbors2)
      {
          int dist = Point.distanceSquared(p,pressed);
          if (dist < rand.nextInt(6) + 1)
             world.setBackground(p, new Background("background_village",imageStore.getImageList("house")));
      }
      
   }
   
   

   public Background createDefaultBackground(ImageStore imageStore)
   {
      return new Background(DEFAULT_IMAGE_NAME,
         imageStore.getImageList(DEFAULT_IMAGE_NAME));
   }

   public PImage createImageColored(int width, int height, int color)
   {
      PImage img = new PImage(width, height, RGB);
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         img.pixels[i] = color;
      }
      img.updatePixels();
      return img;
   }

   private void loadImages(String filename, ImageStore imageStore,
      PApplet screen)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         imageStore.loadImages(in, screen);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void loadWorld(WorldModel world, String filename,
      ImageStore imageStore)
   {
      try
      {
         Scanner in = new Scanner(new File(filename));
         world.load(in, imageStore);
      }
      catch (FileNotFoundException e)
      {
         System.err.println(e.getMessage());
      }
   }

   public static void scheduleActions(WorldModel world,
      EventScheduler scheduler, ImageStore imageStore)
   {
      for (Entity entity : world.getEntities())
      {
         if (entity instanceof ActiveEntity) {
            ((ActiveEntity) entity).scheduleActions(scheduler, world, imageStore);
         }
      }
   }

   public static void parseCommandLine(String [] args)
   {
      for (String arg : args)
      {
         switch (arg)
         {
            case FAST_FLAG:
               timeScale = Math.min(FAST_SCALE, timeScale);
               break;
            case FASTER_FLAG:
               timeScale = Math.min(FASTER_SCALE, timeScale);
               break;
            case FASTEST_FLAG:
               timeScale = Math.min(FASTEST_SCALE, timeScale);
               break;
         }
      }
   }

   public static boolean processLine(WorldModel world, String line, ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return Factory.parseBackground(world, properties, imageStore);
            case MINER_KEY:
               return Factory.parseMiner(world, properties, imageStore);
            case OBSTACLE_KEY:
               return Factory.parseObstacle(world, properties, imageStore);
            case ORE_KEY:
               return Factory.parseOre(world, properties, imageStore);
            case SMITH_KEY:
               return Factory.parseSmith(world, properties, imageStore);
            case VEIN_KEY:
               return Factory.parseVein(world, properties, imageStore);
         }
      }

      return false;
   }

   public static void main(String [] args)
   {
      parseCommandLine(args);
      PApplet.main(VirtualWorld.class);
   }
}
