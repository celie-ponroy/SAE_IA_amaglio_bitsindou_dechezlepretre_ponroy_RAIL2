import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainKNN {
    public static void main(String[] args) throws IOException {
        System.out.println("Chargement des données...");
        Imagette[] img = Imagette.charger("doc/baque_images/train-images.idx3-ubyte");
        Etiquette[] etiquettes = Etiquette.charger("doc/baque_images/train-labels.idx1-ubyte");
        Donnees d = new Donnees(img);
        d.etiquetter(etiquettes);

        Imagette[] imgTest = Imagette.charger("doc/baque_images/t10k-images.idx3-ubyte");
        Etiquette[] etiquettesTest = Etiquette.charger("doc/baque_images/t10k-labels.idx1-ubyte");
        Donnees dTest = new Donnees(imgTest);
        dTest.etiquetter(etiquettesTest);

        int tailleInput = img[0].getNiveauGris().length * img[0].getNiveauGris()[0].length;

        //Couches du reseaux
        ArrayList<Integer> couches = new ArrayList<Integer>();
        couches.add(128);
        couches.add(64);
        //couches.add(30);

        double learning = 0.03;

        MLP mlp = new MLP(new int[]{tailleInput, couches.get(0), couches.get(1), 10}, learning, new Sigmoide());
        String transfertF = mlp.fTransferFunction instanceof Sigmoide ? "sigmoide" : "tanH";

        double erreurMoyEntrainement = 0;
        //Test a vide
        ArrayList<Double> erreurs = new ArrayList<>();
        ArrayList<Double> stats = new ArrayList<>();
        ArrayList<Integer> it = new ArrayList<>();
        for (Imagette imagette : img) {
            double[] entrees = applatissement(imagette.getNiveauGris());
            double[] sortieVoulu = new double[10];
            sortieVoulu[imagette.getEtiquette().getEtiquette()] = 1.0;

            double[] nSortie = mlp.execute(entrees);

            // Calcul de l'erreur
            double error = 0.0;
            for (int i = 0; i < sortieVoulu.length; i++) {
                error += Math.abs(nSortie[i] - sortieVoulu[i]);
            }
            error = error / sortieVoulu.length;
            erreurMoyEntrainement += error;
        }
        erreurMoyEntrainement = erreurMoyEntrainement / img.length;
        stats.add(StatsRN.testerReseauNeurone(mlp));
        erreurs.add(erreurMoyEntrainement);
        it.add(0);

        int countIt = 1;
        for (int j = 0; j < 3; j++) {
            erreurMoyEntrainement = 0;
            for (int i = 0; i < 5; i++) {
                System.out.println("It en cour : " + i);
                for (Imagette imagette : img) {
                    double[] entrees = applatissement(imagette.getNiveauGris());
                    double[] sortieVoulu = new double[10];
                    sortieVoulu[imagette.getEtiquette().getEtiquette()] = 1.0;
                    erreurs.add(mlp.backPropagate(entrees, sortieVoulu));
                    stats.add(StatsRN.testerReseauNeurone(mlp));
                    countIt += 1;
                    it.add(countIt);
                }
            }
        }

        int[] x = it.stream().mapToInt(i -> i).toArray();
        double[] errors = erreurs.stream().mapToDouble(i -> i).toArray();
        double[] reussites = stats.stream().mapToDouble(i -> i).toArray();

        //generation des courbes
        Courbe.genererGraphiqueStats(x, reussites, "stats_"+couches.get(0)+"_"+couches.get(1)+"_"+learning+"_"+transfertF);
        Courbe.genererGraphique(x, errors, "erreur_"+couches.get(0)+"_"+couches.get(1)+"_"+learning+"_"+transfertF);
        mlp.sauve("doc/res/"+couches.get(0)+"_"+couches.get(1)+"_"+learning+"_"+transfertF);
    }

    public static double[] applatissement(double[][] tab) {
        List<Double> list = new ArrayList<>();
        Arrays.stream(tab).forEach(array -> Arrays.stream(array).forEach(list::add));
        double[] flatCarte = new double[list.size()];
        for (int i = 0; i < list.size(); i++) {
            flatCarte[i] = list.get(i);
        }
        return flatCarte;
    }

    public static double[] applatissement(int[][] tab) {
        double[][] res = new double[tab.length][tab[0].length];
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[0].length; j++) {
                res[i][j] = tab[i][j];
            }
        }
        return applatissement(res);
    }
}
