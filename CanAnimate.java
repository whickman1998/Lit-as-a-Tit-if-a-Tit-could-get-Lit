import processing.core.PImage;

import java.util.List;

public abstract class CanAnimate extends ActiveEntity {
    
    private int animationPeriod;
    
    public CanAnimate(Point position,
                      List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod);
        this.animationPeriod = animationPeriod;
    }
    
    public int getAnimationPeriod() {
        return animationPeriod;
    }
    
    public void nextImage()
    {
        super.setImageIndex((super.getImageIndex() + 1) % super.getImages().size());
    }
    
    public void scheduleActions(EventScheduler scheduler,
                                WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this, Factory.createActivityAction(this, world, imageStore), super.getActionPeriod());
        scheduler.scheduleEvent(this, Factory.createAnimationAction(this, 0), getAnimationPeriod());
    }
    
}

