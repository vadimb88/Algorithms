import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;

    private class Node {
        private final Point2D key;
        private RectHV rect;
        private Node left, right;
        private int count;

        public Node(Point2D key, RectHV rect) {
            this.key = key;
            this.count = 1;
            this.rect = rect;
        }
    }

    public KdTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.count;
    }

    private double compare(Point2D a, Point2D b, boolean direction) {
        if (direction == HORIZONTAL) {
            return Point2D.Y_ORDER.compare(a, b);
        } else {
            return Point2D.X_ORDER.compare(a, b);
        }
    }

    public void insert(Point2D p) {
        checkNull(p);
        root = put(root, p, new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                                        Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), VERTICAL);
    }

    private RectHV getLeftRect(Node x, boolean direction) {
        if (direction == HORIZONTAL)
            return new RectHV(x.rect.xmin(), x.rect.ymin(), x.rect.xmax(), x.key.y());
        else
            return new RectHV(x.rect.xmin(), x.rect.ymin(), x.key.x(), x.rect.ymax());
    }

    private RectHV getRightRect(Node x, boolean direction) {
        if (direction == HORIZONTAL)
            return new RectHV(x.rect.xmin(), x.key.y(), x.rect.xmax(), x.rect.ymax());
        else
            return new RectHV(x.key.x(), x.rect.ymin(), x.rect.xmax(), x.rect.ymax());
    }

    private Node put(Node x, Point2D point, RectHV rect, boolean direction) {
        if (x == null)
            return new Node(point, rect);

        double cmp = compare(point, x.key, direction);
        if (cmp < 0) {
            RectHV leftRect = x.left == null ? getLeftRect(x, direction) : null;
            x.left = put(x.left, point, leftRect, !direction);
        } else if (cmp > 0) {
            RectHV rightRect = x.right == null ? getRightRect(x, direction) : null;
            x.right = put(x.right, point, rightRect, !direction);
        } else {
            if (compare(point, x.key, !direction) != 0) {
                x.left = put(x.left, point, getLeftRect(x, direction), !direction);
            }
        }

        x.count = 1 + size(x.left) + size(x.right);
        return x;
    }

    public boolean contains(Point2D p) {
        checkNull(p);
        Node x = root;
        boolean direction = VERTICAL;
        while (x != null) {
            double cmp = compare(p, x.key, direction);
            if      (cmp < 0)
                x = x.left;
            else if (cmp > 0)
                x = x.right;
            else if (compare(p, x.key, !direction) != 0)
                x = x.left;
            else
                return true;

            direction = !direction;
        }

        return false;
    }

    private void draw(Node x, RectHV drawRect, boolean direction) {
        if (x == null) return;

        final double pointX = x.key.x();
        final double pointY = x.key.y();

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(pointX, pointY);

        StdDraw.setPenRadius(0.005);
        double rectXmin = x.rect.xmin();
        double rectYmin = x.rect.ymin();
        double rectXmax = x.rect.xmax();
        double rectYmax = x.rect.ymax();
        double xmin, ymin, xmax, ymax;

        if (direction == HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            xmin = rectXmin != Double.NEGATIVE_INFINITY ? rectXmin : drawRect.xmin();
            ymin = pointY;
            xmax = rectXmax != Double.POSITIVE_INFINITY ? rectXmax : drawRect.xmax();
            ymax = pointY;
        } else {
            StdDraw.setPenColor(StdDraw.RED);
            xmin = pointX;
            ymin = rectYmin != Double.NEGATIVE_INFINITY ? rectYmin : drawRect.ymin();
            xmax = pointX;
            ymax = rectYmax != Double.POSITIVE_INFINITY ? rectYmax : drawRect.ymax();
        }

        StdDraw.line(xmin, ymin, xmax, ymax);
        draw(x.left, drawRect, !direction);
        draw(x.right, drawRect, !direction);
    }

    public void draw() {
        draw(root, new RectHV(0, 0, 1, 1), VERTICAL);
    }

    private void range(Node x, RectHV rect, ArrayList<Point2D> list, boolean direction) {
        if (x == null) return;

        if (rect.contains(x.key))
            list.add(x.key);

        if (x.left != null && rect.intersects(x.left.rect))
            range(x.left, rect, list, !direction);

        if (x.right != null && rect.intersects(x.right.rect))
            range(x.right, rect, list, !direction);

    }

    public Iterable<Point2D> range(RectHV rect) {
        checkNull(rect);
        ArrayList<Point2D> list = new ArrayList<>();
        range(root, rect, list, VERTICAL);
        return list;
    }

    private Point2D nearest(Node x, Point2D p, Point2D min, boolean direction) {
        if (x == null) return min;

        if (p.distanceTo(x.key) < p.distanceTo(min)) {
            min = x.key;
        }

        double cmp = compare(p, x.key, direction);
        if (cmp < 0) {
            min = nearest(x.left, p, min, !direction);
            if (x.right != null && min.distanceTo(p) > x.right.rect.distanceTo(p))
                min = nearest(x.right, p, min, !direction);

        } else {
            min = nearest(x.right, p, min, !direction);
            if (x.left != null && min.distanceTo(p) > x.left.rect.distanceTo(p))
                min = nearest(x.left, p, min, !direction);
        }

        return min;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        checkNull(p);
        return nearest(root, p, root.key, VERTICAL);
    }

    private void checkNull(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.9, 0.5));
        tree.insert(new Point2D(0.2, 0.5));
        tree.insert(new Point2D(0.3, 0.5));
        tree.insert(new Point2D(0.4, 0.5));
        tree.insert(new Point2D(0.1, 0.5));
        tree.insert(new Point2D(0.6, 0.5));
        tree.insert(new Point2D(0.5, 0.5));
        tree.insert(new Point2D(0.7, 0.5));
        System.out.println(tree.contains(new Point2D(0.7,0.5)));
        System.out.println(tree.contains(new Point2D(0.71,0.5)));
        System.out.println(tree.size());

    }
}
