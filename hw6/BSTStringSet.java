import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Alfred Wang
 */
public class BSTStringSet implements SortedStringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = helper(_root, s);
    }

    private Node helper(Node n, String s) {
        if (n == null) {
            return new Node(s);
        }
        if (n.s.compareTo(s) < 0) {
            n.right = helper(n.right, s);
        } else if (n.s.compareTo(s) > 0) {
            n.left = helper(n.left, s);
        }
        return n;
    }

    @Override
    public boolean contains(String s) {
        return this.asList().contains(s);
    }

    @Override
    public List<String> asList() {
        List<String> l = new ArrayList<>();
        for (String s : this) {
            l.add(s);
        }
        return l;
    }


    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new BSTIterator(helper(_root, low, high));
    }

    private Node helper(Node n, String low, String high) {
        if (n == null) {
            return null;
        }
        Node result;
        if (n.s.compareTo(low) < 0) {
            result = helper(n.right, low, high);
        } else if (n.s.compareTo(high) >= 0) {
            result = helper(n.left, low, high);
        } else {
            result = new Node(n.s);
            result.left = helper(n.left, low, high);
            result.right = helper(n.right, low, high);
        }
        return result;
    }

    /** Root node of the tree. */
    private Node _root;
}
