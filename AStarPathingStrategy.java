import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {
    
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        List<Point> open = new ArrayList<>();
        List<Point> closed = new ArrayList<>();
        
        int openMaxSize = 0;
        
        HashMap<Point, Integer> gvals = new HashMap<>();
        HashMap<Point, Integer> hvals = new HashMap<>();
        HashMap<Point, Integer> fvals = new HashMap<>();
        HashMap<Point, Point> prior = new HashMap<>();
        
        open.add(start);
        gvals.put(start, 0);
        hvals.put(start, Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY()));
        fvals.put(start, gvals.get(start) + hvals.get(start));
        prior.put(start, null);
        openMaxSize++;
        
        Point currpos = start;
        
        while (!withinReach.test(currpos, end) || closed.size() < openMaxSize) {
            List<Point> neighbors = CARDINAL_NEIGHBORS.apply(currpos)
            .filter(canPassThrough).filter(p -> !closed.contains(p) && !open.contains(p))
            .collect(Collectors.toList());
            if (neighbors.size() > 0) {
                for (int i = 0; i < neighbors.size(); i++) {
                    Point p = neighbors.get(i);
                    open.add(p);
                    gvals.put(p, gvals.get(currpos) + 1);
                    hvals.put(p, Math.abs(currpos.getX() - end.getX()) + Math.abs(currpos.getY() - end.getY()));
                    fvals.put(p, gvals.get(p) + hvals.get(p));
                    prior.put(p, currpos);
                }
            }
            open.remove(currpos);
            fvals.remove(currpos);
            closed.add(currpos);
            
            if (open.contains(start)) {
                open.remove(start);
                fvals.remove(start);
                closed.add(start);
            }
            if (fvals.size() > 0) {
                Object[] temp = fvals.values().toArray();
                int min = ((Integer) temp[0]).intValue();
                for (int i = 1; i < temp.length; i++) {
                    if (((Integer) temp[i]).intValue() < min) {
                        min = ((Integer) temp[i]).intValue();
                    }
                }
                Object[] keys = fvals.keySet().toArray();
                for (Object o : keys) {
                    if (fvals.get((Point) o) <= min) {
                        currpos = (Point) o;
                    }
                }
            }
            
        }
        
        List<Point> path = new ArrayList<>();
        while(prior.get(currpos) != null && prior.get(prior.get(currpos)) != null) {
            currpos = prior.get(currpos);
        }
        path.add(currpos);
        return path;
        
    }
    
}

