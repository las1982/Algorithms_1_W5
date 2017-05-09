import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;


import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;

    private static class Node {
        private Point2D p;
        private Node left, right;
        private boolean vertical;
        private RectHV rect;
        private int sz;

        public Node(Point2D p, boolean vertical, RectHV rect) {
            p = p;
            vertical = vertical;
            rect = rect;
            sz = 1;
        }
    }

    public KdTree() {

    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        root = insert(root, p, true, 0, 0, 1, 1);
    }

    private Node insert(Node cur, Point2D p, boolean vertical, double l, double b, double r, double t) {
        if (cur == null)
            cur = new Node(p, vertical, new RectHV(l, b, r, t));

        if (cur.p.equals(p))
            return cur;

        if (vertical) {
            if (p.x() < cur.p.x()) {
                cur.left = insert(cur.left, p, !vertical,
                        cur.rect.xmin(), cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
            } else
                cur.right = insert(cur.right, p, !vertical,
                        cur.p.x(), cur.rect.ymin(), cur.rect.xmax(), cur.rect.ymax());
        } else {
            if (p.y() < cur.p.y())
                cur.left = insert(cur.left, p, !vertical,
                        cur.rect.xmin(), cur.rect.ymin(), cur.rect.xmax(), cur.p.y());
            else
                cur.right = insert(cur.right, p, !vertical,
                        cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), cur.rect.ymax());

        }

        cur.sz = 1 + size(cur.left) + size(cur.right);
        return cur;
    }

    private int size(Node cur) {
        if (cur == null)
            return 0;
        else
            return cur.sz;
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        Node ret = search(p);
        return ret != null;
    }

    private Node search(Point2D p) {
        Node cur = root;
        while (true) {
            if (cur == null || cur.p.equals(p))
                return cur;

            if (cur.vertical) {
                if (p.x() < cur.p.x())
                    cur = cur.left;
                else
                    cur = cur.right;
            } else {
                if (p.y() < cur.p.y())
                    cur = cur.left;
                else
                    cur = cur.right;
            }
        }
    }

    public void draw() {
        draw(root);
    }

    private void draw(Node cur) {
        if (cur == null)
            return;

        StdDraw.point(cur.p.x(), cur.p.y());
        if (cur.vertical)
            StdDraw.line(cur.p.x(), cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
        else
            StdDraw.line(cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), cur.p.y());

        draw(cur.left);
        draw(cur.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();
        List<Point2D> ret = new ArrayList<Point2D>();
        range(root, rect, ret);
        return ret;
    }

    private void range(Node cur, RectHV rect, List<Point2D> ret) {
        if (cur == null)
            return;

        if (!cur.rect.intersects(rect))
            return;

        if (rect.contains(cur.p))
            ret.add(cur.p);

        range(cur.left, rect, ret);
        range(cur.right, rect, ret);
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        Point2D[] ret = new Point2D[]{null};
        nearest(root, p, ret);
        return ret[0];
    }

    private void nearest(Node cur, Point2D p, Point2D[] ret) {
        if (cur == null)
            return;

        if (ret[0] == null || cur.p.distanceSquaredTo(p) < ret[0].distanceSquaredTo(p))
            ret[0] = cur.p;

        Node[] children = new Node[]{cur.left, cur.right};
        if (cur.vertical && p.x() >= cur.p.x() ||
                !cur.vertical && p.y() >= cur.p.y()) {
            children[0] = cur.right;
            children[1] = cur.left;
        }
        for (Node c : children)
            if (c != null && c.rect.distanceSquaredTo(p) < ret[0].distanceSquaredTo(p))
                nearest(c, p, ret);
    }

    public static void main(String[] args) {

    }
}
