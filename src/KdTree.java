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
            this.p = p;
            this.vertical = vertical;
            this.rect = rect;
            this.sz = 1;
        }
    }

    public KdTree() {

    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int size() {
        return this.size(this.root);
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        this.root = this.insert(this.root, p, true, 0, 0, 1, 1);
    }

    private Node insert(Node cur, Point2D p, boolean vertical, double l, double b, double r, double t) {
        if (cur == null)
            cur = new Node(p, vertical, new RectHV(l, b, r, t));

        if (cur.p.equals(p))
            return cur;

        if (vertical) {
            if (p.x() < cur.p.x()) {
                cur.left = this.insert(cur.left, p, !vertical,
                        cur.rect.xmin(), cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
            } else
                cur.right = this.insert(cur.right, p, !vertical,
                        cur.p.x(), cur.rect.ymin(), cur.rect.xmax(), cur.rect.ymax());
        } else {
            if (p.y() < cur.p.y())
                cur.left = this.insert(cur.left, p, !vertical,
                        cur.rect.xmin(), cur.rect.ymin(), cur.rect.xmax(), cur.p.y());
            else
                cur.right = this.insert(cur.right, p, !vertical,
                        cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), cur.rect.ymax());

        }

        cur.sz = 1 + this.size(cur.left) + this.size(cur.right);
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

        Node ret = this.search(p);
        return ret != null;
    }

    private Node search(Point2D p) {
        Node cur = this.root;
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
        this.draw(this.root);
    }

    private void draw(Node cur) {
        if (cur == null)
            return;

        StdDraw.point(cur.p.x(), cur.p.y());
        if (cur.vertical)
            StdDraw.line(cur.p.x(), cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
        else
            StdDraw.line(cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), cur.p.y());

        this.draw(cur.left);
        this.draw(cur.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException();
        List<Point2D> ret = new ArrayList<Point2D>();
        this.range(this.root, rect, ret);
        return ret;
    }

    private void range(Node cur, RectHV rect, List<Point2D> ret) {
        if (cur == null)
            return;

        if (!cur.rect.intersects(rect))
            return;

        if (rect.contains(cur.p))
            ret.add(cur.p);

        this.range(cur.left, rect, ret);
        this.range(cur.right, rect, ret);
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new NullPointerException();

        Point2D[] ret = new Point2D[]{null};
        this.nearest(this.root, p, ret);
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
                this.nearest(c, p, ret);
    }

    public static void main(String[] args) {

    }
}
