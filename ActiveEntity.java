import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity {

    private int actionPeriod;

    public ActiveEntity(Point position,
                 List<PImage> images, int actionPeriod)
    {
        super(position, images);
        this.actionPeriod = actionPeriod;
    }

    protected abstract void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore);
    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public int getActionPeriod() {
        return actionPeriod;
    }

}
