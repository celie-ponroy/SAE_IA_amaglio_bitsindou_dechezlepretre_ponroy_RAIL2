import java.util.Arrays;
import java.util.HashMap;

public class Main_MLP_OU_ET_XOR {
    public static void main(String[] args) {

        //-----------------Lignes à changer en fonction de la configuration voulue--------------------------
        // Paramètres généraux
        int[] nb_neurone_2 = {2, 4, 2}; // exemple d'architecture avec 2 sortie
        int[] nb_neurone_1 = {2, 4, 1}; // exemple d'architecture avec 1 sortie
        double learning_rate = 0.1;

//---------------------2 sorties--------------------------------
        //Tables de vérité pour XOR, ET, OU dans l'ordre a 2 sorties
//        HashMap<String, double[][][]> exemples = new HashMap<>();
//        exemples.put("XOR", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // Entrées
//                {{1,0}, {0,1}, {0,1}, {1,0}}             // Sorties
//        });
//        exemples.put("ET", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
//                {{1,0}, {1,0}, {1,0}, {0,1}}
//        });
//        exemples.put("OU", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
//                {{1,0}, {0,1}, {0,1}, {0,1}}
//        });
//
//        //Tables de vérité pour XOR, ET, OU dans le désordre a 2 sorties
        HashMap<String, double[][][]> exemplesMelanges = new HashMap<>();
        exemplesMelanges.put("XOR", new double[][][]{
                {{1, 0}, {0, 0}, {1, 1}, {0, 1}}, // Entrées
                {{0,1}, {1,0}, {1,0}, {0,1}}             // Sorties
        });
        exemplesMelanges.put("ET", new double[][][]{
                {{0, 1}, {1, 0}, {0, 0}, {1, 1}},
                {{1,0}, {1,0}, {1,0}, {0,1}}
        });
        exemplesMelanges.put("OU", new double[][][]{
                {{1, 0}, {0, 1}, {1, 1}, {0, 0}},
                {{0,1}, {0,1}, {0,1}, {1,0}}
        });
// --------------------------1 sortie--------------------------------
//        //Tables de vérité pour XOR, ET, OU dans l'ordre a 1 sortie
//        exemples.put("XOR", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // Entrées
//                {{0}, {1}, {1}, {0}}             // Sorties
//        });
//        exemples.put("ET", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
//                {{0}, {0}, {0}, {1}}
//        });
//        exemples.put("OU", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
//                {{0}, {1}, {1}, {1}}
//        });

        //Tables de vérité pour XOR, ET, OU dans le désordre a 1 sortie
//        exemplesMelanges.put("XOR", new double[][][]{
//                {{1, 0}, {0, 0}, {1, 1}, {0, 1}}, // Entrées
//                {{0}, {0}, {1}, {1}}             // Sorties
//        });
//        exemplesMelanges.put("ET", new double[][][]{
//                {{0, 1}, {1, 0}, {0, 0}, {1, 1}},
//                {{0}, {0}, {0}, {1}}
//        });
//        exemplesMelanges.put("OU", new double[][][]{ //A CHANGER
//                {{1, 0}, {0, 1}, {1, 1}, {0, 0}},
//                {{1}, {1}, {1}, {0}}
//        });

        //-----------------Lignes à changer en fonction de la configuration voulue--------------------------

        // Test avec la fonction Sigmoide
        testConfigurations(nb_neurone_2, learning_rate, new Sigmoide(), exemplesMelanges, "Sigmoide", 100000);

        // Test avec la fonction Tanh
        testConfigurations(nb_neurone_2, learning_rate, new TanH(), exemplesMelanges, "TanH", 100000);
    }

    /**
     * Teste différentes configurations pour un MLP avec 2 sorties
     *
     * @param nb_neurone    architecture des couches
     * @param learning_rate taux d'apprentissage
     * @param activation    fonction d'activation
     * @param exemples      tables de vérité à tester
     * @param functionName  nom de la fonction d'activation pour les graphiques
     */
    public static void testConfigurations(int[] nb_neurone, double learning_rate, TransferFunction activation, HashMap<String, double[][][]> exemples, String functionName, int itereation) {
        // Teste pour chaque exemple
        for (String key : exemples.keySet()) {

            System.out.println("\n== Test pour : " + key + " ==");

            double[][] inputs = exemples.get(key)[0];
            double[][] outputs = exemples.get(key)[1];

            // Création du réseau
            MLP mlp = new MLP(nb_neurone, learning_rate, activation);

            // Phase d'apprentissage
            double[] errors = new double[itereation]; // Stockage des erreurs pour le graphique
            double[] precision = new double[itereation]; // STockage des precisions pour le graphique
            boolean converged = false;
            for (int ite = 0; ite < itereation; ite++) {
                double totalError = 0.0;
                for (int i = 0; i < inputs.length; i++) {
                    totalError += mlp.backPropagate(inputs[i], outputs[i]);
                }
                //Analyse des erreurs
                errors[ite] = totalError;

                int correct = 0;
                for (int i = 0; i < inputs.length; i++) {
                    //Analyse des reussites
                    double []reussites = mlp.execute(inputs[i]);
                    //int pred = (int) Math.round(reussites[0]);

                    if (reussites[0] < reussites[1]) {
                        reussites[0] = 0;
                        reussites[1] = 1;

                    }else{
                        reussites[0] = 1;
                        reussites[1] = 0;
                    }

                    if(reussites[0] == outputs[i][0] && reussites[1] == outputs[i][1]){
                        correct++;
                    }
                }
                // Précision
                precision[ite] = (correct * 100.0) / inputs.length;


            }

            if (!converged) {
                System.out.println("Limite d'itérations atteinte sans convergence complète.");
            }

            // Génération du graphique des erreurs
            int[] nbIte = new int[itereation];
            for (int i = 0; i < nbIte.length; i++) nbIte[i] = i + 1;
            String chartName = functionName + "_" + key + "_erreur_donneesMelangees_2_sortie";
            Courbe.genererGraphique(nbIte, errors, chartName, learning_rate, nb_neurone,"courbes_ET_OU_XOR");

            //Génération du graphique des réussites
            chartName = functionName + "_" + key + "_reussite_donneesMelangees_2_sortie";
            Courbe.genererGraphiqueAvecInfos(nbIte, precision, chartName, learning_rate,nb_neurone,"courbes_ET_OU_XOR");

        }
    }

    /**
     * Teste différentes configurations pour un MLP avec 1 sortie
     *
     * @param nb_neurone    architecture des couches
     * @param learning_rate taux d'apprentissage
     * @param activation    fonction d'activation
     * @param exemples      tables de vérité à tester
     * @param functionName  nom de la fonction d'activation pour les graphiques
     */
    public static void testConfigurations2(int[] nb_neurone, double learning_rate, TransferFunction activation, HashMap<String, double[][][]> exemples, String functionName, int itereation) {
        // Teste pour chaque exemple
        for (String key : exemples.keySet()) {
            System.out.println("\n== Test pour : " + key + " ==");

            double[][] inputs = exemples.get(key)[0];
            double[][] outputs = exemples.get(key)[1];

            // Création du réseau
            MLP mlp = new MLP(nb_neurone, learning_rate, activation);

            // Phase d'apprentissage
            double[] errors = new double[itereation]; // Stockage des erreurs pour le graphique
            double[] precision = new double[itereation]; // STockage des precisions pour le graphique
            boolean converged = false;
            for (int ite = 0; ite < itereation; ite++) {
                double totalError = 0.0;
                for (int i = 0; i < inputs.length; i++) {
                    totalError += mlp.backPropagate(inputs[i], outputs[i]);
                }
                //Analyse des erreurs
                errors[ite] = totalError;

                int correct = 0;
                for (int i = 0; i < inputs.length; i++) {
                    //Analyse des reussites
                    double []reussites = mlp.execute(inputs[i]);
                    int pred = (int) Math.round(reussites[0]);

                    if (pred == (int) outputs[i][0]) {
                        correct++;
                    }

                }
                // Précision
                precision[ite] = (correct * 100.0) / inputs.length;
            }

            if (!converged) {
                System.out.println("Limite d'itérations atteinte sans convergence complète.");
            }

            // Génération du graphique des erreurs
            int[] nbIte = new int[itereation];
            for (int i = 0; i < nbIte.length; i++) nbIte[i] = i + 1;
            String chartName = functionName + "_" + key + "_erreur_donneesMelangees_1_sortie";
            Courbe.genererGraphique(nbIte, errors, chartName, learning_rate, nb_neurone,"courbes_ET_OU_XOR");

            //Génération du graphique des réussites
            chartName = functionName + "_" + key + "_reussite_donneesMelangees_1_sortie";
            Courbe.genererGraphiqueAvecInfos(nbIte, precision, chartName, learning_rate,nb_neurone,"courbes_ET_OU_XOR");
        }



    }
}