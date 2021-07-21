package game_utils;

public class Main {
    public static void main(String[] args) {
        final long before = System.currentTimeMillis();
        final GraphNode root = new GraphBuilder().build(XOField.Figure.X, new XOField(), 0);
        System.out.println(GraphHelper.countNodes(root));
        final long after = System.currentTimeMillis();
        System.out.printf("time delta: %d", (after - before));
    }
}
