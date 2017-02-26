import java.util.Iterator;
import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {
        this.points = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return this.points.isEmpty();
    }

    public int size() {
        return this.points.size();
    }

    public void insert(Point2D p) {
        this.points.add(p);
    }

    public boolean contains(Point2D p) {
        return this.points.contains(p);
    }

    public void draw() {
        Iterator<Point2D> itr = this.points.iterator();

        while (itr.hasNext()) {
            Point2D point = itr.next();
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> res = new ArrayList<>();
        Iterator<Point2D> itr = this.points.iterator();

        while (itr.hasNext()) {
            Point2D point = itr.next();
            if (rect.contains(point)) {
                res.add(point);
            }
        }

        return res;
    }

    public Point2D nearest(Point2D p) {
        checkNull(p);
        if (points.isEmpty()) {
            return null;
        }

        Iterator<Point2D> itr = this.points.iterator();
        Point2D nearest = itr.next();
        double minDistance = p.distanceTo(nearest);
        while (itr.hasNext()) {
            Point2D point = itr.next();
            double distance = p.distanceTo(point);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = point;
            }
        }

        return nearest;
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
        System.out.println("HELLO!");
    }
}
