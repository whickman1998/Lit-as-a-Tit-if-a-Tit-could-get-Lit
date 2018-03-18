import processing.core.PImage;

import java.util.List;

public class Weapon extends Entity {
    
    public Weapon(Point position,
                  List<PImage> images)
    {
        super(position, images);
    }
    
    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
    }
    
}

