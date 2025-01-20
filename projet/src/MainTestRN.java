import java.io.IOException;

public class MainTestRN {
    public static void main(String[] args) throws IOException {
        Imagette[] imgNoTrain = Imagette.charger("doc/baque_images/t10k-images.idx3-ubyte");
        Etiquette[] etiquettesNoTrain = Etiquette.charger("doc/baque_images/t10k-labels.idx1-ubyte");
        Donnees dTest = new Donnees(imgNoTrain);
        dTest.etiquetter(etiquettesNoTrain);
        MLP mlp = MLP.charge("doc/res/128_64_tan");
        int nbImgBonnes = 0;
        for(Imagette img : imgNoTrain){
            double[] entrees = MainKNN.applatissement(img.getNiveauGris());
            double[] sortie = mlp.execute(entrees);
            System.out.println("sortie :");
            afficherTab(sortie);
            System.out.println("etiquette : "+img.getEtiquette().getEtiquette());
            if(img.getEtiquette().getEtiquette() == getIndiceMax(sortie)){
                nbImgBonnes++;
            }
        }
        System.out.println("Nombre d'imagette trouvées : "+nbImgBonnes+"/"+imgNoTrain.length);
    }

    public static int getIndiceMax(double[] tab){
        double max = Double.NEGATIVE_INFINITY;
        int indiceMax = 0;
        for(int i = 0; i < tab.length; i++){
            if(max < tab[i]){
                max = tab[i];
                indiceMax = i;
            }
        }
        return indiceMax;
    }

    public static void afficherTab(double[] tab){
        for(int i = 0; i < tab.length; i++){
            System.out.printf("%.3f\t", tab[i]);
        }
        System.out.println();
    }
}
