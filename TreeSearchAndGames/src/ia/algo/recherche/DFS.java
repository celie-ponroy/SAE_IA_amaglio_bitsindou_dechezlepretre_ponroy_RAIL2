package ia.algo.recherche;

import ia.framework.common.Action;
import ia.framework.common.State;
import ia.framework.recherche.SearchNode;
import ia.framework.recherche.SearchProblem;
import ia.framework.recherche.TreeSearch;

import java.util.Stack;

public class DFS extends TreeSearch {

    /**
     * Crée un algorithme de recherche
     *
     * @param prob          Le problème à résoudre
     * @param initial_state L'état initial
     */
    public DFS(SearchProblem prob, State initial_state) {
        super(prob, initial_state);
    }

    @Override
    public boolean solve() {
        Stack<SearchNode> frontier = new Stack<>();
        frontier.add(SearchNode.makeRootSearchNode(initial_state));

        while (!frontier.empty()) {
            SearchNode node = frontier.pop();
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