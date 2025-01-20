import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainKNN {
    public static void main(String[] args) throws IOException {
        System.out.println("Chargement des données...");
        Imagette[] img = Imagette.charger("../doc/baque_images/train-images.idx3-ubyte");
        Etiquette[] etiquettes = Etiquette.charger("../doc/baque_images/train-labels.idx1-ubyte");
        Imagette[] imgNoTrain = Imagette.charger("../doc/baque_images/t10k-images.idx3-ubyte");
        Etiquette[] etiquettesNoTrain = Etiquette.charger("../doc/baque_images/t10k-labels.idx1-ubyte");
        Donnees d = new Donnees(img);
        Donnees dTest = new Donnees(imgNoTrain);
        System.out.println("Etiquettage des imagettes...");
        d.etiquetter(etiquettes);
        dTest.etiquetter(etiquettesNoTrain);
        int tailleInput = img[0].getNiveauGris().length*img[0].getNiveauGris()[0].length;
        MLP mlp = new MLP(new int[]{ tailleInput, 100, 75, 50, 10}, 0.03, new Sigmoide());

        for(Imagette imagette : img){
            System.out.println("Traitement d'une nouvelle imagette");
            double[] entrees = applatissement(imagette.getNiveauGris());
            double[] sortieVoulu = new double[10];
            sortieVoulu[imagette.getEtiquette().getEtiquette()] = 1.0;
            System.out.println(mlp.backPropagate(entrees, sortieVoulu));
        }
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
    public static double[] applatissement(int[][] tab){
        double[][] res = new double[tab.length][tab[0].length];
        for(int i = 0; i < tab.length; i++){
            for(int j = 0; j < tab[0].length; j++){
                res[i][j] = tab[i][j];
            }
        }
        return applatissement(res);
    }
}
