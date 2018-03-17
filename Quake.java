import java.util.List;

import processing.core.PImage;
import sun.awt.geom.AreaOp;

public class Quake extends QuakeScheduleActions {

    public Quake(Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                     ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }

}
