import processing.core.PImage;

import java.util.List;

public abstract class JustActivityActions extends ActiveEntity {
    
    public JustActivityActions(Point position,
                               List<PImage> images, int actionPeriod)
    {
        super(position, images, actionPeriod);
    }
    
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), super.getActionPeriod());
    }
    
}

