import processing.core.PImage;

import java.util.List;

public abstract class QuakeScheduleActions extends CanAnimate {
    
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
    
    public QuakeScheduleActions(Point position,
                                List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }
    
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this, QUAKE_ANIMATION_REPEAT_COUNT), getAnimationPeriod());
    }
    
}

