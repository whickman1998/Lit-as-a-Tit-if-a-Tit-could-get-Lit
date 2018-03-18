import java.util.List;
import processing.core.PImage;

public class Blacksmith extends Entity {

    public Blacksmith(Point position, List<PImage> images)
    {
        super(position, images);
    }
    
    /*public boolean transform(WorldModel world,
            EventScheduler scheduler, ImageStore imageStore)
    {
    		Miner miner = Factory.createMinerNotFull(super.getId(), super.getResourceLimit(),
    				super.getPosition(), super.getActionPeriod(), super.getAnimationPeriod(),
    				super.getImages());

    		world.removeEntity(this);
    		scheduler.unscheduleAllEvents(this);

    		world.addEntity(miner);
    		miner.scheduleActions(scheduler, world, imageStore);
    		return false;
    }*/

}
