import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class FastCollinearPoints {

    private final LineSegment[] segm;

    public FastCollinearPoints(Point[] points) {
        checkDuplicates(points);
        Point[] pointsCopy = Arrays.copyOf(points, points.length);
        Arrays.sort(pointsCopy);
        int len = points.length;
        ArrayList<LineSegment> found = new ArrayList<LineSegment>();

        for (Point cur: points) {
            Point[] pCopy = Arrays.copyOf(pointsCopy, pointsCopy.length);
            Arrays.sort(pCopy, cur.slopeOrder());

            double curSlope;
            double prevSlope = Double.NEGATIVE_INFINITY;
            List<Point> curSegm = new ArrayList<Point>();

            for (int i = 1; i < len; i++) {
                curSlope = cur.slopeTo(pCopy[i]);
                if (curSlope == prevSlope) {
                    curSegm.add(pCopy[i]);
                } else {
                    if (curSegm.size() >= 3) {
                        curSegm.add(cur);
                        Collections.sort(curSegm);
                        if (curSegm.get(0) == cur) {
                            found.add(new LineSegment(cur, curSegm.get(curSegm.size() - 1)));
                        }
                    }

                    curSegm.clear();
                    curSegm.add(pCopy[i]);
                }

                prevSlope = curSlope;
            }

            if (curSegm.size() >= 3) {
                curSegm.add(cur);
                Collections.sort(curSegm);
                if (curSegm.get(0) == cur) {
                    found.add(new LineSegment(cur, curSegm.get(curSegm.size() - 1)));
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
