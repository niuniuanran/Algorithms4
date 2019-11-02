import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointTreeSet;

    public PointSET()                               // construct an empty set of points
    {
        pointTreeSet = new TreeSet<Point2D>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return pointTreeSet.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return pointTreeSet.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("Null argument for method insert");

        pointTreeSet.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("Null argument for method contains");

        return pointTreeSet.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        for (Point2D point : pointTreeSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Null argument for method range");

        Stack<Point2D> rangePoints = new Stack<Point2D>();
        Set<Point2D> pointsInY = pointTreeSet.subSet(new Point2D(rect.xmin(), rect.ymin()), true,
                                                     new Point2D(rect.xmax(), rect.ymax()), true);
        for (Point2D p : pointsInY) {
            if (p.x() <= rect.xmax() && p.x() >= rect.xmin()) rangePoints.push(p);
        }
        return rangePoints;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("Null argument for method nearest");

        double minDistanceSquare = Double.POSITIVE_INFINITY;
        double currDistanceSquare;
        Point2D neighborPoint = null;
        for (Point2D point : pointTreeSet) {
            currDistanceSquare = p.distanceSquaredTo(point);
            if (currDistanceSquare < minDistanceSquare) {
                minDistanceSquare = currDistanceSquare;
                neighborPoint = point;
            }
        }
        return neighborPoint;
    }

    public String toString() {
        return pointTreeSet.toString();
    }

    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {
        PointSET myPointSet = new PointSET();
        StdOut.printf("Now our tree is empty: %b\n", myPointSet.isEmpty());
        myPointSet.insert(new Point2D(0.2, 0.5));
        myPointSet.insert(new Point2D(0.4, 0.5));
        myPointSet.insert(new Point2D(0.5, 0.5));
        myPointSet.insert(new Point2D(0.6, 0.5));
        myPointSet.draw();
        Iterable<Point2D> pointsInRect = myPointSet.range(new RectHV(0.1, 0.2, 0.4, 0.5));
        for (Point2D point : pointsInRect) StdOut.println(point);
        Iterable<Point2D> pointsInRect2 = myPointSet.range(new RectHV(0.1, 0.4, 0.3, 0.5));
        for (Point2D point : pointsInRect2) StdOut.println(point);

    }
}
