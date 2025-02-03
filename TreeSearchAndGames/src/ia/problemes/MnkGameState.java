package ia.problemes;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un état d'un jeu générique m,n,k Game 
 */

public class MnkGameState extends AbstractMnkGameState {


    protected ArrayList<ArrayList<Pair>> ensembles_gagnants;//stocke les ensembles de taille k

    /**
     * Construire une grille vide de la bonne taille
     *
     * @param r nombre de lignes
     * @param c nombre de colonnes 
     */
    public MnkGameState(int r, int c, int s) {
        super(r,c,s);
        //initialisation de l'ensemble des gagnants
        this.generer_ensembles_gagnants();
    }
    private void generer_ensembles_gagnants() {
        ensembles_gagnants = new ArrayList<>();

        // les lignes gagantes
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols - streak; c++) {
                ArrayList<Pair> set = new ArrayList<>();
                for (int k = 0; k < streak; k++) {
                    set.add(new Pair(r, c + k));
                }
                ensembles_gagnants.add(set);
            }
        }

        // les colonnes gagantes
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r <= rows - streak; r++) {
                ArrayList<Pair> set = new ArrayList<>();
                for (int k = 0; k < streak; k++) {
                    set.add(new Pair(r + k, c));
                }
                ensembles_gagnants.add(set);
            }
        }

        // diagonales de haut-gauche à bas-droit gagantes
        for (int r = 0; r <= rows - streak; r++) {
            for (int c = 0; c <= cols - streak; c++) {
                ArrayList<Pair> set = new ArrayList<>();
                for (int k = 0; k < streak; k++) {
                    set.add(new Pair(r + k, c + k));
                }
                ensembles_gagnants.add(set);
            }
        }

        // les diagonales de bas-gauche à haut-droit gagantes
        for (int r = streak - 1; r < rows; r++) {
            for (int c = 0; c <= cols - streak; c++) {
                ArrayList<Pair> set = new ArrayList<>();
                for (int k = 0; k < streak; k++) {
                    set.add(new Pair(r - k, c + k));
                }
                ensembles_gagnants.add(set);
            }
        }
    }


    public MnkGameState cloneState() {
        MnkGameState new_s = new MnkGameState(this.rows, this.cols, this.streak);
        new_s.board = this.board.clone();
        new_s.player_to_move = player_to_move;
        new_s.game_value = game_value;
        if(this.last_action != null)
            new_s.last_action = this.last_action.clone();
        for (Pair p: this.winning_move)
            new_s.winning_move.add(p.clone());
        new_s.ensembles_gagnants=this.ensembles_gagnants;
        return new_s;
	}
    /**
     * Un fonction d'évaluation pour cet état du jeu. 
     * Permet de comparer différents états dans le cas ou on ne  
     * peut pas développer tout l'arbre. Le joueur 1 (X) choisira les
     * actions qui mènent au état de valeur maximal, Le joueur 2 (O)
     * choisira les valeurs minimal.
     * 
     * Cette fonction dépend du jeu.
     * 
     * @return la valeur du jeux
     **/
    protected double evaluationFunction() {
        double pos_x = calculDanger('O');
        double pos_o = calculDanger('X');

        double value = pos_x-pos_o;

        // System.out.println("Possibilities: X = "+pos_x+", O = "+pos_o+
        //". Value = "+value);
        return value;
    }
    private double calculDanger(char perso){

        double danger = 0.0;


        for (ArrayList<Pair> ensemble_gagnant_F : ensembles_gagnants) {
            int l =  0;
            boolean perso_in = false;

            for (Pair cell : ensemble_gagnant_F) {
                char val_case = getValueAt(cell.getRow(), cell.getCol());
                if (val_case == perso) {
                    perso_in = true;
                } else if (val_case == EMPTY) {
                    l++;
                }
            }

            if (!perso_in && l > 0) {
                danger += 1.0 / Math.pow(2, l);
            }
        }

        return danger;
    }
}
