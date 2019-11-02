import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int segCounter;
    private final LineSegment[] segCollection;

    public BruteCollinearPoints(
            Point[] givenPoints)    // finds all line segments containing 4 points
    {
        if (givenPoints == null) throw new IllegalArgumentException();

        int len = givenPoints.length;
        Point[] points = new Point[len];
        for (int i = 0; i < len; i++) {
            if (givenPoints[i] == null) throw new IllegalArgumentException();
            points[i] = givenPoints[i];
        }
        segCounter = 0;
        segCollection = new LineSegment[len];
        Arrays.sort(points);
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                if (points[j].equals(points[i])) throw new IllegalArgumentException();
                for (int k = j + 1; k < len; k++) {
                    if (points[j].equals(points[k])) throw new IllegalArgumentException();
                    for (int p = k + 1; p < len; p++) {
                        if (points[j].equals(points[p])) throw new IllegalArgumentException();

                        double slopeIJ = points[i].slopeTo(points[j]);
                        double slopeJK = points[j].slopeTo(points[k]);
                        double slopeJL = points[j].slopeTo(points[p]);

                        if (Double.compare(slopeIJ, slopeJK) == 0
                                && Double.compare(slopeJK, slopeJL) == 0) {
                            segCollection[segCounter++] = new LineSegment(points[i], points[p]);
                        }
                    }
                }
            }
        }

    }

    public int numberOfSegments() {  // the number of line segments
        return segCounter;
    }

    public LineSegment[] segments() { // the line segments
        LineSegment[] segs = new LineSegment[segCounter];
        for (int i = 0; i < segCounter; i++) {
            segs[i] = segCollection[i];
        }
        return segs;
    }


    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
