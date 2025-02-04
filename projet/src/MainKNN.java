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
        //entrées
        couches.add(tailleInput);
        //couches cachées
        couches.add(10);
        couches.add(10);
        //sortie
        couches.add(10);

        int[] couchesTab = couches.stream().mapToInt(i -> i).toArray();
        double learning = 0.1;

        //suffle
        img = Imagette.melanger(img);
        imgTest = Imagette.melanger(imgTest);

        MLP mlp = new MLP(couchesTab, learning, new TanH());
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
        stats.add(StatsRN.testerReseauNeurone(mlp,imgTest));
        erreurs.add(erreurMoyEntrainement);
        it.add(0);

        int countIt = 0;
        while (stats.get(countIt) < 99 && countIt < 500) {
            erreurMoyEntrainement = 0;
            System.out.println("It en cour : " + countIt);
            for (Imagette imagette : img) {
                double[] entrees = applatissement(imagette.getNiveauGris());
                double[] sortieVoulu = new double[10];
                sortieVoulu[imagette.getEtiquette().getEtiquette()] = 1.0;
                erreurMoyEntrainement += mlp.backPropagate(entrees, sortieVoulu);
            }
            countIt += 1;
            it.add(countIt);
            stats.add(StatsRN.testerReseauNeurone(mlp,imgTest));
            erreurs.add(erreurMoyEntrainement/img.length);
        }

        int[] x = it.stream().mapToInt(i -> i).toArray();
        double[] errors = erreurs.stream().mapToDouble(i -> i).toArray();
        double[] reussites = stats.stream().mapToDouble(i -> i).toArray();

        String nFichierCouche = "";
        //generation des courbes
        for (int i=1; i<couches.size()-1;i++){
            nFichierCouche += couches.get(i)+"_";
        }
        Courbe.genererGraphiqueAvecInfos(x, reussites, "stats_"+nFichierCouche+learning+"_"+transfertF, learning, couchesTab, "courbes_MLP_VS_KNN");
        Courbe.genererGraphique(x, errors, "erreur_"+nFichierCouche+learning+"_"+transfertF, learning, couchesTab, "courbes_MLP_VS_KNN");
        mlp.sauve("doc/res/"+nFichierCouche+learning+"_"+transfertF);
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
