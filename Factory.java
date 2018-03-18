import processing.core.PImage;

import java.util.List;

public class Factory {

    private static final String MINER_KEY = "miner";
    private static final String OBSTACLE_KEY = "obstacle";
    private static final String ORE_KEY = "ore";
    private static final String SMITH_KEY = "blacksmith";
    private static final String VEIN_KEY = "vein";
    private static final String WEAPON_KEY = "weapon";
    private static final String SOLDIER_KEY = "soldier"; 

    private static final int QUAKE_ACTION_PERIOD = 1100;
    private static final int QUAKE_ANIMATION_PERIOD = 100;

    private static final int BGND_NUM_PROPERTIES = 4;
    private static final int BGND_ID = 1;
    private static final int BGND_COL = 2;
    private static final int BGND_ROW = 3;

    private static final int MINER_NUM_PROPERTIES = 7;
    private static final int MINER_ID = 1;
    private static final int MINER_COL = 2;
    private static final int MINER_ROW = 3;
    private static final int MINER_LIMIT = 4;
    private static final int MINER_ACTION_PERIOD = 5;
    private static final int MINER_ANIMATION_PERIOD = 6;

    private static final int OBSTACLE_NUM_PROPERTIES = 4;
    private static final int OBSTACLE_ID = 1;
    private static final int OBSTACLE_COL = 2;
    private static final int OBSTACLE_ROW = 3;

    private static final int ORE_NUM_PROPERTIES = 5;
    private static final int ORE_ID = 1;
    private static final int ORE_COL = 2;
    private static final int ORE_ROW = 3;
    private static final int ORE_ACTION_PERIOD = 4;

    private static final int SMITH_NUM_PROPERTIES = 4;
    private static final int SMITH_ID = 1;
    private static final int SMITH_COL = 2;
    private static final int SMITH_ROW = 3;

    private static final int VEIN_NUM_PROPERTIES = 5;
    private static final int VEIN_ID = 1;
    private static final int VEIN_COL = 2;
    private static final int VEIN_ROW = 3;
    private static final int VEIN_ACTION_PERIOD = 4;

    public static String getOreKey() {
        return ORE_KEY;
    }
    
    public static String getWeaponKey() {
    		return WEAPON_KEY; 
    }
    
    public static String getVeinKey() {
    		return VEIN_KEY; 
    }
    
    public static String getSoldierKey() {
    		return SOLDIER_KEY; 
    }
    
    public static String getSmithKey() {
    		return SMITH_KEY; 
    }

    public static Action createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation(entity, repeatCount);
    }

    public static Action createActivityAction(Entity entity, WorldModel world,
                                       ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore);
    }

    public static Blacksmith createBlacksmith(Point position,
                                       List<PImage> images)
    {
        return new Blacksmith(position, images);
    }

    public static Miner_Full createMinerFull(String id, int resourceLimit,
                                      Point position, int actionPeriod, int animationPeriod,
                                      List<PImage> images)
    {
        return new Miner_Full(id, position, images,
                resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public static Miner_Not_Full createMinerNotFull(String id, int resourceLimit,
                                             Point position, int actionPeriod, int animationPeriod,
                                             List<PImage> images)
    {
        return new Miner_Not_Full(id, position, images,
                resourceLimit, 0, actionPeriod, animationPeriod);
    }

    public static Obstacle createObstacle(String id, Point position,
                                   List<PImage> images)
    {
        return new Obstacle(position, images);
    }

    public static Ore createOre(Point position, int actionPeriod,
                         List<PImage> images)
    {
        return new Ore(position, images, actionPeriod);
    }

    public static Ore_Blob createOreBlob(Point position,
                                  int actionPeriod, int animationPeriod, List<PImage> images)
    {
        return new Ore_Blob(position, images, actionPeriod, animationPeriod);
    }
    
    public static Weapon createWeapon(Point position, List<PImage> images)
    {
        return new Weapon(position, images);
    }
    
    public static Soldier createSoldier(Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod) 
    {
    		return new Soldier(position, images, actionPeriod, animationPeriod); 
    }

    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(position, images, QUAKE_ACTION_PERIOD, QUAKE_ANIMATION_PERIOD);
    }

    public static Vein createVein(Point position, int actionPeriod,
                           List<PImage> images)
    {
        return new Vein(position, images, actionPeriod);
    }
    
    public static ProducingBlacksmith createProducingBlacksmith(Point position, int actionPeriod,
                           List<PImage> images) {
    		return new ProducingBlacksmith(position, images, actionPeriod); 
    }

    public static boolean parseBackground(WorldModel world, String [] properties, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            world.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public static boolean parseMiner(WorldModel world, String [] properties, ImageStore imageStore)
    {
        if (properties.length == MINER_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[MINER_COL]),
                    Integer.parseInt(properties[MINER_ROW]));
            Entity entity = Factory.createMinerNotFull(properties[MINER_ID],
                    Integer.parseInt(properties[MINER_LIMIT]),
                    pt,
                    Integer.parseInt(properties[MINER_ACTION_PERIOD]),
                    Integer.parseInt(properties[MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList(MINER_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == MINER_NUM_PROPERTIES;
    }

    public static boolean parseObstacle(WorldModel world, String [] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES)
        {
            Point pt = new Point(
                    Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = Factory.createObstacle(properties[OBSTACLE_ID],
                    pt, imageStore.getImageList(OBSTACLE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public static boolean parseOre(WorldModel world, String [] properties, ImageStore imageStore)
    {
        if (properties.length == ORE_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[ORE_COL]),
                    Integer.parseInt(properties[ORE_ROW]));
            Entity entity = Factory.createOre(pt, Integer.parseInt(properties[ORE_ACTION_PERIOD]),
                    imageStore.getImageList(ORE_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == ORE_NUM_PROPERTIES;
    }

    public static boolean parseSmith(WorldModel world, String [] properties, ImageStore imageStore)
    {
        if (properties.length == SMITH_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[SMITH_COL]),
                    Integer.parseInt(properties[SMITH_ROW]));
            Entity entity = Factory.createBlacksmith(pt, imageStore.getImageList(SMITH_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == SMITH_NUM_PROPERTIES;
    }

    public static boolean parseVein(WorldModel world, String [] properties, ImageStore imageStore)
    {
        if (properties.length == VEIN_NUM_PROPERTIES)
        {
            Point pt = new Point(Integer.parseInt(properties[VEIN_COL]),
                    Integer.parseInt(properties[VEIN_ROW]));
            Entity entity = Factory.createVein(pt,
                    Integer.parseInt(properties[VEIN_ACTION_PERIOD]),
                    imageStore.getImageList(VEIN_KEY));
            world.tryAddEntity(entity);
        }

        return properties.length == VEIN_NUM_PROPERTIES;
    }

}
