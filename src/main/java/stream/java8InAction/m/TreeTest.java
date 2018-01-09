package stream.java8InAction.m;

/**
 * Created by fangyou on 2018/1/9.
 */
public class TreeTest {

}


class Tree {
    private String key;
    private int val;
    private Tree left, right;

    public Tree(String k, int v, Tree l, Tree r) {
        key = k;
        val = v;
        left = l;
        right = r;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Tree getLeft() {
        return left;
    }

    public void setLeft(Tree left) {
        this.left = left;
    }

    public Tree getRight() {
        return right;
    }

    public void setRight(Tree right) {
        this.right = right;
    }
}

class TreeProcessor {
    public static int lookup(String k, int defaultval, Tree t) {
        if (t == null) return defaultval;
        if (k.equals(t.getKey())) return t.getVal();
        return lookup(k, defaultval, k.compareTo(t.getKey()) < 0 ? t.getLeft() : t.getRight());
    }

    public static void update(String k, int newval, Tree t) {
        if (t == null) {}
        else if (k.equals(t.getKey())) t.setVal(newval);
        else update(k, newval, k.compareTo(t.getKey()) < 0 ? t.getLeft() : t.getRight());
    }

    public static Tree update2(String k, int newval, Tree t) {
        if (t == null)
            t = new Tree(k, newval, null, null);
        else if (k.equals(t.getKey()))
            t.setVal(newval);
        else if(k.compareTo(t.getKey()) < 0)
            t.setLeft(update2(k, newval, t.getLeft()));
        else
            t.setRight(update2(k,newval,t.getRight()));
        return t;
    }

    public static Tree fupdate(String k, int newval, Tree t) {
        return (t == null) ?
                new Tree(k, newval, null, null) :
                k.equals(t.getKey()) ?
                        new Tree(k, newval, t.getLeft(), t.getRight()) :
                        k.compareTo(t.getKey()) < 0 ?
                                new Tree(t.getKey(),t.getVal(), fupdate(k, newval, t.getLeft()), t.getRight()):
                                new Tree(t.getKey(),t.getVal(), t.getLeft(), fupdate(k, newval, t.getRight()));
    }
}