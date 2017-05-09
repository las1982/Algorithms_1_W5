import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class PointSET {
    private Set<Point2D> set;

    public PointSET() {
        this.set = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    public int size() {
        return this.set.size();
    }

    public void insert(Point2D p) {
        this.set.add(p);

    }

    public boolean contains(Point2D p) {
        return this.set.contains(p);
    }

    public void draw() {
        for (Point2D p : this.set) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> ret = new ArrayList<Point2D>();
        for (Point2D p : this.set)
            if (rect.contains(p))
                ret.add(p);

        return ret;
    }

    public Point2D nearest(Point2D p) {
        Point2D ret = null;
        for (Point2D c : this.set)
            if (ret == null || c.distanceSquaredTo(p) < ret.distanceSquaredTo(p))
                ret = c;

        return ret;
    }

    public static void main(String[] args) {

    }
}