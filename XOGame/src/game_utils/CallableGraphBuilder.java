package game_utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class CallableGraphBuilder implements Callable<GraphNode> {

    private final ExecutorService executorService;

    private final XOField.Figure nextFigure;

    private final XOField currentField;

    private final int deepLevel;

    public CallableGraphBuilder(
            final ExecutorService executorService,
            final XOField.Figure currentFigure,
            final XOField currentField,
            final int deepLevel) {
        this.executorService = executorService;
        this.nextFigure = currentFigure == XOField.Figure.X ? XOField.Figure.X : XOField.Figure.O;
        this.currentField = currentField;
        this.deepLevel = deepLevel;
    }

    public GraphNode call() throws Exception {
        final List<Future<GraphNode>> futures = new ArrayList<>();
        final Set<GraphNode> children = new CopyOnWriteArraySet<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (currentField.getFigure(x, y) != null) {continue;}
                final XOField newField = new XOField(currentField);
                newField.setFigure(x, y, nextFigure);
                final CallableGraphBuilder graphBuilder = new CallableGraphBuilder(
                        executorService,
                        nextFigure,
                        newField,
                        deepLevel + 1
                );
                if (isAsync()) {
                    final Future<GraphNode> future = executorService.submit(graphBuilder);
                    futures.add(future);
                } else {
                    children.add(graphBuilder.call());
                }
            }
        }
        if (!futures.isEmpty()) {
            for (Future<GraphNode> future : futures) {
                try {
                    children.add(future.get());
                } catch (final InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
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
