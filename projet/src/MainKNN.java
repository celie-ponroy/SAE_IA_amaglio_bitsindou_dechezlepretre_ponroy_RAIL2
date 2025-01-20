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

        Imagette[] imgTest = Imagette.charger("doc/baque_images/train-images.idx3-ubyte");
        Etiquette[] etiquettestest = Etiquette.charger("doc/baque_images/train-labels.idx1-ubyte");
        Donnees dTest = new Donnees(img);

        d.etiquetter(etiquettes);
        //dTest.etiquetter(etiquettesNoTrain);

        int tailleInput = img[0].getNiveauGris().length * img[0].getNiveauGris()[0].length;

        MLP mlp = new MLP(new int[]{tailleInput, 128, 64, 10}, 0.03, new Sigmoide());
        double erreurMoy = 0;
        ArrayList<Double> erreurs = new ArrayList<>();
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
            erreurMoy += error;
        }
        erreurMoy = erreurMoy / img.length;
        erreurs.add(erreurMoy);

        for (int j = 0; j < 3; j++) {
            double erreurMoyEntrainement = 0;
            for (int i = 0; i < 5; i++) {
                System.out.println("It en cour : " + i);
                for (Imagette imagette : img) {
                    double[] entrees = applatissement(imagette.getNiveauGris());
                    double[] sortieVoulu = new double[10];
                    sortieVoulu[imagette.getEtiquette().getEtiquette()] = 1.0;
                    erreurMoyEntrainement += mlp.backPropagate(entrees, sortieVoulu);
                }
            }
            erreurMoyEntrainement = erreurMoyEntrainement / (img.length * 5);
            erreurs.add(erreurMoyEntrainement);
        }
        Courbe.genererGraphique(new int[]{0, 5, 10, 15}, new double[]{erreurs.get(0), erreurs.get(1), erreurs.get(2), erreurs.get(3)}, "erreur_128_64_0.03_sig");
        mlp.sauve("doc/res/128_64_0.03_sig");
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
