import java.util.Arrays;
import java.util.HashMap;

public class Main_MLP_OU_ET_XOR {
    public static void main(String[] args) {

        //-----------------Lignes à changer en fonction de la configuration voulue--------------------------
        // PDifférentes configurations testées
        int[] config_1c_100n_2s = {2, 100, 1}; // config avec 2 couches cachées et 2 sorties
        int[] config_1c_8n_1s = {2, 8, 1}; // config avec 2 couches cachées et 2 sorties
        int[] config_2c_2s = {2, 4, 4, 2}; // config avec 2 couches cachées et 2 sorties
        int[] config_2c_1s = {2, 4, 4, 1}; //config avec 2 couches cachées et 1 sortie
        int[] config_1c_2s = {2, 4, 2}; // config  avec 2 sorties
        int[] config_1c_1s = {2, 4, 1}; // config avec 1 sortie
        double learning_rate = 0.1;
        int nb_itereation = 500000;

        //Initialisation des tables de vérité
        HashMap<String, double[][][]> exemplesRanges = new HashMap<>();
        HashMap<String, double[][][]> exemplesMelanges = new HashMap<>();
// --------------------------1 sortie--------------------------------
        //Tables de vérité pour XOR, ET, OU dans l'ordre a 1 sortie
        exemplesRanges.put("XOR", new double[][][]{
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // Entrées
                {{0}, {1}, {1}, {0}}             // Sorties
        });
        exemplesRanges.put("ET", new double[][][]{
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
                {{0}, {0}, {0}, {1}}
        });
        exemplesRanges.put("OU", new double[][][]{
                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
                {{0}, {1}, {1}, {1}}
        });

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
//---------------------2 sorties--------------------------------
        //Tables de vérité pour XOR, ET, OU dans l'ordre a 2 sorties
//        exemplesRanges.put("XOR", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}}, // Entrées
//                {{1,0}, {0,1}, {0,1}, {1,0}}             // Sorties
//        });
//        exemplesRanges.put("ET", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
//                {{1,0}, {1,0}, {1,0}, {0,1}}
//        });
//        exemplesRanges.put("OU", new double[][][]{
//                {{0, 0}, {0, 1}, {1, 0}, {1, 1}},
//                {{1,0}, {0,1}, {0,1}, {0,1}}
//        });
//
//        //Tables de vérité pour XOR, ET, OU dans le désordre a 2 sorties
//        exemplesMelanges.put("XOR", new double[][][]{
//                {{1, 0}, {0, 0}, {1, 1}, {0, 1}}, // Entrées
//                {{0,1}, {1,0}, {1,0}, {0,1}}             // Sorties
//        });
//        exemplesMelanges.put("ET", new double[][][]{
//                {{0, 1}, {1, 0}, {0, 0}, {1, 1}},
//                {{1,0}, {1,0}, {1,0}, {0,1}}
//        });
//        exemplesMelanges.put("OU", new double[][][]{
//                {{1, 0}, {0, 1}, {1, 1}, {0, 0}},
//                {{0,1}, {0,1}, {0,1}, {1,0}}
//        });

        //-----------------Lignes à changer en fonction de la configuration voulue--------------------------

        // Test avec la fonction Sigmoide
        testConfigurations1sortie(config_1c_100n_2s, learning_rate, new Sigmoide(), exemplesRanges, "Sigmoide", nb_itereation);

        // Test avec la fonction Tanh
        testConfigurations1sortie(config_1c_100n_2s, learning_rate, new TanH(), exemplesRanges, "TanH", nb_itereation);
    }

    /**
     * Teste différentes configurations pour un MLP avec 1 sortie
     *
     * @param nb_neurone    configuration du réseau
     * @param learning_rate taux d'apprentissage
     * @param activation    fonction d'activation
     * @param exemples      tables de vérité à tester
     * @param functionName  nom de la fonction d'activation pour les graphiques
     */
    public static void testConfigurations1sortie(int[] nb_neurone, double learning_rate, TransferFunction activation, HashMap<String, double[][][]> exemples, String functionName, int itereation) {
        // Teste pour chaque exemple
        for (String key : exemples.keySet()) {

            String activationFunctionName = activation.getClass().getSimpleName();
            System.out.println("Fonction d'activation : " + activationFunctionName);

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
            //PENSER À CHANGER LE NOM EN FONCTION DE LA CONFIGURATION
            String configName = Arrays.toString(nb_neurone)
                    .replaceAll("[\\[\\] ]", "")  // Supprime les crochets et les espaces
                    .replace(",", "_");           // Remplace les virgules par des underscores
            String chartName = functionName + "_" + key + "_erreur_donneesRangees_" + configName;//nom du fichier avec la configuration
            Courbe.genererGraphique(nbIte,errors, chartName, learning_rate, nb_neurone,"courbes_ET_OU_XOR","Taux d'erreur de sortie en fonction des itérations","Erreur");

            //Génération du graphique des réussites
            //PENSER À CHANGER LE NOM EN FONCTION DE LA CONFIGURATION
            chartName = functionName + "_" + key + "_reussite_donneesRangees_" + configName;//nom du fichier avec la configuration
            Courbe.genererGraphique(nbIte, precision, chartName, learning_rate,nb_neurone,"courbes_ET_OU_XOR","Taux de reussite en fonction de itérations","Reussite");
        }



    }

    /**
     * Teste différentes configurations pour un MLP avec 2 sorties
     *
     * @param nb_neurone    configuration du réseau
     * @param learning_rate taux d'apprentissage
     * @param activation    fonction d'activation
     * @param exemples      tables de vérité à tester
     * @param functionName  nom de la fonction d'activation pour les graphiques
     */
    public static void testConfigurations2sorties(int[] nb_neurone, double learning_rate, TransferFunction activation, HashMap<String, double[][][]> exemples, String functionName, int itereation) {
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
            //PENSER À CHANGER LE NOM EN FONCTION DE LA CONFIGURATION
            String configName = Arrays.toString(nb_neurone)
                    .replaceAll("[\\[\\] ]", "")  // Supprime les crochets et les espaces
                    .replace(",", "_");           // Remplace les virgules par des underscores
            String chartName = functionName + "_" + key + "_erreur_donneesMelangees_" + configName;//nom du fichier avec la configuration
            Courbe.genererGraphique(nbIte,errors, chartName, learning_rate, nb_neurone,"courbes_ET_OU_XOR","Taux d'erreur de sortie en fonction des itérations","Erreur");

            //Génération du graphique des réussites
            //PENSER À CHANGER LE NOM EN FONCTION DE LA CONFIGURATION
            chartName = functionName + "_" + key + "_reussite_donneesMelangees_" + configName;//nom du fichier avec la configuration
            Courbe.genererGraphique(nbIte, precision, chartName, learning_rate,nb_neurone,"courbes_ET_OU_XOR","Taux de reussite en fonction des itérations","Reussite");

        }
    }

}
