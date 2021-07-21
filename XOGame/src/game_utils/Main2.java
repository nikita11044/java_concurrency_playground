package game_utils;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

public class Main2 {
    public static void main(String[] args) {
        final ForkJoinPool pool = new ForkJoinPool();
        final RecursiveTaskGraphBuilder gb = new RecursiveTaskGraphBuilder(XOField.Figure.X, new XOField(), 0);
        ForkJoinTask<GraphNode> rootTask = gb.fork();
        pool.submit(rootTask);
        System.out.println(GraphHelper.countNodes(rootTask.join()));
    }
}
