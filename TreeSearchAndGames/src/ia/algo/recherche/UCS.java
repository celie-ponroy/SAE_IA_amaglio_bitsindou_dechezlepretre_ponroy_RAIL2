package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.Comparator;
import java.util.PriorityQueue;

public class UCS extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param prob Le problème à résoudre
     * @param inital_state L'état initial
     */
    public UCS(SearchProblem prob, State inital_state) {
        super(prob, inital_state);
    }

    @Override
    public boolean solve() {
        //Liste triée grace à getCost()
        PriorityQueue<SearchNode> frontier = new PriorityQueue<>(
                new Comparator<SearchNode>() {
                    @Override
                    public int compare(SearchNode o1, SearchNode o2) {
                        return (int) (o1.getCost() - o2.getCost());
                    }
                }
        );
        frontier.add(SearchNode.makeRootSearchNode(initial_state));

        while (!frontier.isEmpty()) {
            SearchNode node = frontier.poll();
            State state = node.getState();

            if (problem.isGoalState(state)) {
                end_node = node;
                return true;
            }

            explored.add(state);

            for (Action action : problem.getActions(state)) {
                SearchNode child = SearchNode.makeChildSearchNode(problem, node, action);
                State childState = child.getState();

                if (!explored.contains(childState)) {
                    frontier.add(child);
                }
            }
        }

        return false;
    }
}
