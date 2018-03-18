import java.util.List;
import processing.core.PImage;

public class Blacksmith extends Entity {

    public Blacksmith(Point position, List<PImage> images)
    {
        super(position, images);
    }
    
    public boolean transform(WorldModel world,
            EventScheduler scheduler, ImageStore imageStore)
    {
    		ProducingBlacksmith producingblacksmith = Factory.createProducingBlacksmith(super.getPosition(),(int)(11500+Math.random()*500), imageStore.getImageList("pblacksmith")); 

    		world.removeEntity(this);
    		scheduler.unscheduleAllEvents(this);

    		world.addEntity(producingblacksmith);
    		producingblacksmith.scheduleActions(scheduler, world, imageStore);
    		producingblacksmith.executeActivity(world, imageStore, scheduler);
    		return false;
    }

}
