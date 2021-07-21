package game_utils;

import java.util.Set;

public class GraphNode {

    private final XOField node;

    private final Set<GraphNode> children;

    public GraphNode(final XOField node, final Set<GraphNode> children) {
        this.node = node;
        this.children = children;
    }

    public XOField getNode() {
        return node;
    }

    public Set<GraphNode> getChildren() {
        return children;
    }
}
