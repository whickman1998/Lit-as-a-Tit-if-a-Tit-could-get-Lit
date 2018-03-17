import processing.core.PImage;

import java.util.List;

public class Weapon extends JustActivityActions {
    
    public Weapon(Point position,
                  List<PImage> images, int actionPeriod)
    {
        super(position, images, actionPeriod);
    }
    
    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {
        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);
    }
    
}

