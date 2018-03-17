import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Miner extends Mover {

    private String id;
    private int resourceLimit;
    private int resourceCount;

    public Miner(String id, Point position,
                 List<PImage> images, int resourceLimit, int resourceCount,
                 int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
        this.id = id;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

    public void addResourceCount(int i) {
        resourceCount += i;
    }

    public String getId() {
        return id;
    }

    public int getResourceLimit() {
        return resourceLimit;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    protected abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    protected abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

}
