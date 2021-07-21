package game_utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class RecursiveTaskGraphBuilder extends RecursiveTask<GraphNode> {

    private final XOField.Figure nextFigure;

    private final XOField currentField;

    private final int deepLevel;

    public RecursiveTaskGraphBuilder(
            final XOField.Figure currentFigure,
            final XOField currentField,
            final int deepLevel) {
        this.nextFigure = currentFigure == XOField.Figure.X ? XOField.Figure.X : XOField.Figure.O;
        this.currentField = currentField;
        this.deepLevel = deepLevel;
    }

    public GraphNode compute() {
        final List<ForkJoinTask<GraphNode>> tasks = new ArrayList<>();
        final Set<GraphNode> children = new HashSet<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (currentField.getFigure(x, y) != null) {continue;}
                final XOField newField = new XOField(currentField);
                newField.setFigure(x, y, nextFigure);
                final RecursiveTaskGraphBuilder graphBuilder = new RecursiveTaskGraphBuilder(
                        nextFigure,
                        newField,
                        deepLevel + 1
                );
                if (isAsync()) {
                    tasks.add(graphBuilder.fork());
                } else {
                    children.add(graphBuilder.compute());
                }
            }
        }
        if (!tasks.isEmpty()) {
            for (ForkJoinTask<GraphNode> task : tasks) {
                children.add(task.join());
            }
        }
        return new GraphNode(currentField, children);
    }

    private boolean isAsync() {
        if (deepLevel > 3) {
            return false;
        } else {
            return true;
        }
    }
}
