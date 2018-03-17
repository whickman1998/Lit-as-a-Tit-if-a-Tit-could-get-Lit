import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Mover extends CanAnimate {

    public Mover(Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(position, images, actionPeriod, animationPeriod);
    }

    public Point nextPosition(WorldModel world,
                              Point destPos)
    {
        Point currPos = super.getPosition();
        Point[] parr = {new Point(currPos.getX()+1, currPos.getY()), new Point(currPos.getX(), currPos.getY()-1),
                new Point(currPos.getX()-1, currPos.getY()), new Point(currPos.getX(), currPos.getY()+1)};
        PathingStrategy strategy = new AStarPathingStrategy();
        List<Point> paths = strategy.computePath(currPos, destPos, p -> world.withinBounds(p) && !world.isOccupied(p),
                (p1, p2) -> Math.abs(p1.getX() - p2.getX()) <= 1 && Math.abs(p1.getY() - p2.getY()) <= 1, p -> new ArrayList<Point>(Arrays.asList(parr)).stream());
        if (paths.size() == 0) {
            return super.getPosition();
        }
        else {
            return paths.get(0);
        }
    }

    protected abstract boolean moveTo(WorldModel world, Entity target, EventScheduler scheduler);

}
