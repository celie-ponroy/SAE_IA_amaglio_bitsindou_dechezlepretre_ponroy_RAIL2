package ia.algo.jeux;

import ia.framework.common.Action;
import ia.framework.common.ActionValuePair;
import ia.framework.jeux.Game;
import ia.framework.jeux.GameState;
import ia.framework.jeux.Player;

public class MinMaxPlayer extends Player {
    private int profondeur;

    /**
     * Represente un joueur
     *
     * @param g          l'instance du jeux
     * @param player_one si joueur 1
     */
    public MinMaxPlayer(Game g, boolean player_one,int profondeur) {
        super(g, player_one);
        if(profondeur<0){
            profondeur = Integer.MAX_VALUE;
        }
        this.profondeur = profondeur;
        name = "MinMax";
    }

    @Override
    public Action getMove(GameState state) {
        if(this.player==PLAYER1) {
            return maxValeur(state,0).getAction();
        }else{
            return minValeur(state,0).getAction();
        }
    }

    private ActionValuePair maxValeur(GameState state,int prof){
        incStateCounter();
        if(game.endOfGame(state)||prof>=profondeur){
            return new ActionValuePair(null,state.getGameValue());
        }
        double v_max = Double.NEGATIVE_INFINITY;
        Action c_max = null;

        for (Action action : this.game.getActions(state)){

            GameState s_suivant = (GameState) this.game.doAction(state,action);
            ActionValuePair min = minValeur(s_suivant, prof+1);
            if(min.getValue()>=v_max){
                v_max = min.getValue();
                c_max = action;
            }
        }
        return new ActionValuePair(c_max,v_max);

    }

    private ActionValuePair minValeur(GameState state,int prof){
        incStateCounter();

        if(game.endOfGame(state)||prof>=profondeur){
            return new ActionValuePair(null,state.getGameValue());
        }
        double v_min = Double.POSITIVE_INFINITY;
        Action c_min = null;

        for (Action action :this.game.getActions(state)){
            GameState s_suivant = (GameState) this.game.doAction(state,action);
            ActionValuePair max = maxValeur(s_suivant, prof+1);
            if(max.getValue()<=v_min){
                v_min = max.getValue();
                c_min = action;
            }
        }
        return new ActionValuePair(c_min,v_min);

    }
}
