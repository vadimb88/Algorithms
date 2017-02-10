import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segm;

    public BruteCollinearPoints(Point[] points) {
        checkDuplicates(points);

        int len = points.length;
        Point[] pCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pCopy);
        ArrayList<LineSegment> found = new ArrayList<LineSegment>();

        for (int i = 0; i < len - 3; i++) {
            Point a = pCopy[i];
            for (int j = i + 1; j < len - 2; j++) {
                Point b = pCopy[j];
                double slope = a.slopeTo(b);
                for (int k = j + 1; k < len - 1; k++) {
                    Point c = pCopy[k];
                    if (slope == b.slopeTo(c)) {
                        for (int l = k + 1; l < len; l++) {
                            Point d = pCopy[l];
                            if (slope == c.slopeTo(d)) {
                                found.add(new LineSegment(a, d));
                            }
                        }
                    }
                }
            }
        }

        this.segm = found.toArray(new LineSegment[found.size()]);
    }

    public int numberOfSegments() {
        return this.segm.length;
    }

    public LineSegment[] segments() {
        return Arrays.copyOf(this.segm, this.segm.length);
    }

    private void checkDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("Duplicated points in input array");
            }
        }
    }

    public static void main(String[] args) {

    }

}
