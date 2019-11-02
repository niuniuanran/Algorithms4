import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private LineSegment[] segs;
    private int segCounter;
    private Point[] repeatPoint;
    private double[] repeatSlope;


    public FastCollinearPoints(Point[] givenPoints) {
        // finds all line segments containing 4 or more points

        if (givenPoints == null) throw new IllegalArgumentException("null Point array");
        int counter;
        int len = givenPoints.length;
        Point[] points = new Point[len];
        for (int t = 0; t < len; t++) {
            if (givenPoints[t] == null) throw new IllegalArgumentException("null Point");
            points[t] = givenPoints[t];
        }
        segs = new LineSegment[len * 4];
        segCounter = 0;

        repeatPoint = new Point[len * 4];
        repeatSlope = new double[len * 4];
        int repeatIndex = 0;

        int maxLen = 0;

        if (len <= 3) {
            Arrays.sort(points);
            for (int i = 1; i < len; i++) {
                if (points[i].equals(points[i - 1]))
                    throw new IllegalArgumentException("Duplicate Points!");
            }
            segs = new LineSegment[0];
            return;
        }

        for (int i = 0; i < len - 3; i++) {
            counter = 1;
            Arrays.sort(points, i, len);
            Arrays.sort(points, i + 1, len, points[i].slopeOrder());

            for (int j = i + 1; j < len; j++) {
                if (points[j].equals(points[j - 1]))
                    throw new IllegalArgumentException("Duplicate Points!");

                if (Double.compare(points[j].slopeTo(points[i]), points[j - 1].slopeTo(points[i]))
                        == 0) counter++;

                else if (counter >= 3) {
                    // if current length >= maxlen, there will be no repeat.
                    if (counter >= maxLen) maxLen = counter;

                        // if current length < maxlen, need to check if there is a repeat.
                        // If there is a repeat, don't need to go any further.
                    else if (checkRepeat(points[j - 1], points[j - 1].slopeTo(points[i]),
                                         repeatIndex)) {
                        counter = 1;
                        continue;
                    }

                    // if there is no repeat and there are four or more points collinear to points[i],
                    // when you come to points[j-counter] you will need to do it again.
                    if (counter > 3) {
                        if (repeatIndex >= repeatPoint.length) resizeRepeat(repeatIndex);
                        repeatPoint[repeatIndex] = points[j - 1];
                        repeatSlope[repeatIndex++] = points[j - 1].slopeTo(points[i]);
                    }
                    if (segCounter >= segs.length) resizeLineSegs();
                    segs[segCounter++] = new LineSegment(points[i], points[j - 1]);
                    counter = 1;
                }
                else counter = 1;
            }

            // when j reaches the end
            if (counter >= 3) {
                if (counter >= maxLen) maxLen = counter;
                else if (checkRepeat(points[len - 1],
                                     points[len - 1].slopeTo(points[i]), repeatIndex)) {
                    continue;
                }

                if (counter > 3) {
                    if (repeatIndex >= repeatSlope.length) resizeRepeat(repeatIndex);

                    repeatPoint[repeatIndex] = points[len - 1];
                    repeatSlope[repeatIndex++] = points[len - 1].slopeTo(points[i]);
                }
                if (segCounter >= segs.length) resizeLineSegs();
                segs[segCounter++] = new LineSegment(points[i], points[len - 1]);
            }

        }

    }

    private void resizeRepeat(int oldSize) {

        Point[] newRepeatPoint = new Point[oldSize * 4];
        double[] newRepeatSlope = new double[oldSize * 4];
        for (int i = 0; i < oldSize; i++) {
            newRepeatPoint[i] = repeatPoint[i];
            newRepeatSlope[i] = repeatSlope[i];
        }
        repeatPoint = newRepeatPoint;
        repeatSlope = newRepeatSlope;
    }

    private void resizeLineSegs() {

        LineSegment[] newSegs = new LineSegment[segCounter * 4];
        for (int i = 0; i < segCounter; i++) {
            newSegs[i] = segs[i];
        }
        segs = newSegs;
    }


    // check the first point and the last point.
    private boolean checkRepeat(Point lastPoint,
                                double slope, int repeatIndex) {
        boolean repeatFlag = false;
        for (int p = 0; p < repeatIndex; p++) {
            if (repeatPoint[p].equals(lastPoint) && Double.compare(repeatSlope[p], slope) == 0)
                repeatFlag = true;
        }
        if (repeatFlag) return true;
        return false;

    }


    public int numberOfSegments() {      // the number of line segments
        return segCounter;
    }

    public LineSegment[] segments() {               // the line segments
        return Arrays.copyOfRange(segs, 0, segCounter);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
