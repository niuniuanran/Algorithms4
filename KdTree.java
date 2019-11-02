import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;

public class KdTree {
    private final boolean VERTICAL = false, HORIZONTAL = true;
    private KdNode root;
    private double minDistanceSquared;
    private Point2D championPoint;

    private class KdNode {
        private final boolean orientation;
        private final double key;
        private int size;
        private final Point2D point;
        private KdNode left;
        private KdNode right;

        public KdNode(Point2D value, boolean currOrientation) {
            orientation = currOrientation;
            point = value;
            if (orientation == HORIZONTAL) key = point.y();
            else key = point.x();
            size = 1;
            left = null;
            right = null;
        }
    }

    public KdTree()                               // construct an empty set of points
    {
        root = null;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return (root == null);
    }

    public int size()                         // number of points in the set
    {
        if (isEmpty()) return 0;
        return root.size;
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException("Null point for method insert");
        if (isEmpty()) {
            root = new KdNode(p, VERTICAL);
            return;
        }
        KdNode insertOutcome = insert(p, root, VERTICAL);
        if (insertOutcome != null) root = insertOutcome;
        assert root.size == (root.left.size + root.right.size + 1);
    }

    private KdNode insert(Point2D p, KdNode rootNode, boolean rootOrientation) {

        if (rootNode == null) {
            rootNode = new KdNode(p, rootOrientation);
            return rootNode;
        }

        if (rootNode.point.equals(p)) return null;

        KdNode newNode;
        if (rootOrientation == VERTICAL) {
            if (p.x() < rootNode.key) {
                newNode = insert(p, rootNode.left, HORIZONTAL);
                if (newNode != null) {
                    rootNode.left = newNode;
                    rootNode.size++;
                    return rootNode;
                }
                else return null;
            }
            else { // if (p.x() >= rootNode.key)
                newNode = insert(p, rootNode.right, HORIZONTAL);
                if (newNode != null) {
                    rootNode.right = newNode;
                    rootNode.size++;
                    return rootNode;
                }
                else return null;
            }
        }
        else { // if (rootOrientation == HORIZONTAL)
            if (p.y() < rootNode.key) {
                newNode = insert(p, rootNode.left, VERTICAL);
                if (newNode != null) {
                    rootNode.left = newNode;
                    rootNode.size++;
                    return rootNode;
                }
                else return null;
            }
            else { // if (p.y() >= rootNode.key)
                newNode = insert(p, rootNode.right, VERTICAL);
                if (newNode != null) {
                    rootNode.right = newNode;
                    rootNode.size++;
                    return rootNode;
                }
                else return null;
            }
        }

    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException("Null point for method contains");
        return contains(p, root);
    }

    private boolean contains(Point2D p, KdNode rootNode) {
        if (rootNode == null) return false;
        if (rootNode.point.equals(p)) return true;
        if (rootNode.orientation == VERTICAL) {
            if (p.x() < rootNode.point.x()) return contains(p, rootNode.left);
            else return contains(p, rootNode.right);
        }
        else {
            if (p.y() < rootNode.point.y()) return contains(p, rootNode.left);
            else return contains(p, rootNode.right);
        }
    }

    public void draw()                         // draw all points to standard draw
    {
        RectHV currSquare = new RectHV(0, 0, 1, 1);
        if (!isEmpty()) draw(root, currSquare);
    }


    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException("Null rect at method range");

        Stack<Point2D> rangeCollection = new Stack<Point2D>();
        range(rect, root, rangeCollection);
        return rangeCollection;
    }

    private void range(RectHV rect, KdNode rootNode, Stack<Point2D> rangeCollection) {
        if (rootNode == null) return;

        if (rect.contains(rootNode.point)) {
            rangeCollection.push(rootNode.point);
        }

        if (goLeft(rootNode.orientation, rootNode.point, rect))
            range(rect, rootNode.left, rangeCollection);

        if (goRight(rootNode.orientation, rootNode.point, rect))
            range(rect, rootNode.right, rangeCollection);
    }

    private boolean goLeft(boolean orientation, Point2D p, RectHV rect) {
        if (orientation == VERTICAL) {
            if (rect.xmin() <= p.x()) return true;
        }
        if (orientation == HORIZONTAL) {
            if (rect.ymin() <= p.y()) return true;
        }
        return false;
    }

    private boolean goRight(boolean orientation, Point2D p, RectHV rect) {
        if (orientation == VERTICAL) {
            if (rect.xmax() >= p.x()) return true;
        }
        if (orientation == HORIZONTAL) {
            if (rect.ymax() >= p.y()) return true;
        }
        return false;
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException("null point for method nearest");
        if (root == null) return null;
        minDistanceSquared = Double.POSITIVE_INFINITY;
        championPoint = null;
        nearest(p, root);
        return championPoint;
    }

    private void nearest(Point2D p, KdNode rootNode) {
        if (rootNode == null) return;

        if (p.distanceSquaredTo(rootNode.point) < minDistanceSquared) {
            minDistanceSquared = p.distanceSquaredTo(rootNode.point);
            championPoint = rootNode.point;
        }

        if (rootNode.orientation == VERTICAL) {
            if (rootNode.left != null
                    && p.x() < rootNode.key)
                nearest(p, rootNode.left);
            if (rootNode.right != null
                    && (p.x() - rootNode.key) * (p.x() - rootNode.key) < minDistanceSquared)
                nearest(p, rootNode.right);

        }
        else { // if (rootNode.orientation == HORIZONTAL)
            if (rootNode.left != null
                    && p.y() < rootNode.key)
                nearest(p, rootNode.left);


            if (rootNode.right != null
                    && (p.y() - rootNode.key) * (p.y() - rootNode.key) < minDistanceSquared)
                nearest(p, rootNode.right);

        }
    }

    private void draw(KdNode rootNode, RectHV squareRange) {

        double x0, y0, x, y, x1, y1;
        x = rootNode.point.x();
        y = rootNode.point.y();

        //draw current point
        setPointDraw();
        StdDraw.point(x, y);

        if (rootNode.orientation == VERTICAL) {
            // draw current line
            setLineDraw(VERTICAL);
            x0 = x;
            x1 = x;
            y0 = squareRange.ymin();
            y1 = squareRange.ymax();
            StdDraw.line(x0, y0, x1, y1);

            //draw point and line on the left
            if (rootNode.left != null) {
                draw(rootNode.left,
                     new RectHV(squareRange.xmin(), squareRange.ymin(), x, squareRange.ymax()));
            }

            //draw point and line on the right
            if (rootNode.right != null) {
                draw(rootNode.right,
                     new RectHV(x, squareRange.ymin(), squareRange.xmax(), squareRange.ymax()));
            }

        }
        else {
            setLineDraw(HORIZONTAL);
            //draw current line
            y0 = y;
            y1 = y;
            x0 = squareRange.xmin();
            x1 = squareRange.xmax();
            StdDraw.line(x0, y0, x1, y1);

            // draw below
            if (rootNode.left != null) {
                draw(rootNode.left,
                     new RectHV(squareRange.xmin(), squareRange.ymin(), squareRange.xmax(), y));
            }
            // draw above
            if (rootNode.right != null) {
                draw(rootNode.right,
                     new RectHV(squareRange.xmin(), y, squareRange.xmax(), squareRange.ymax()));
            }
        }


    }

    private void setPointDraw() {
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setPenRadius(0.005);
    }

    private void setLineDraw(boolean orientation) {
        if (orientation == VERTICAL) StdDraw.setPenColor(Color.RED);
        else StdDraw.setPenColor(Color.BLUE);
        StdDraw.setPenRadius(0.001);
    }


    public static void main(
            String[] args)                  // unit testing of the methods (optional)
    {


    }
}
