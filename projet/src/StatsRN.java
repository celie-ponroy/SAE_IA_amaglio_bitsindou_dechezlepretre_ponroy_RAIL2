import java.io.IOException;

public class StatsRN {

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
    public static double testerReseauNeurone(MLP mlp, Imagette[] imagettes) throws IOException {
        int nbImgBonnes = 0;
        for(Imagette img : imagettes){
            double[] entrees = MainKNN.applatissement(img.getNiveauGris());
            double[] sortie = mlp.execute(entrees);
            //System.out.println("sortie :");
            //afficherTab(sortie);
            //System.out.println("etiquette : "+img.getEtiquette().getEtiquette());
            if(img.getEtiquette().getEtiquette() == getIndiceMax(sortie)){
                //System.out.println("trouvé !");
                nbImgBonnes++;
            }
        }
        System.out.println("Nombre d'imagette trouvées : "+nbImgBonnes+"/"+imagettes.length);
        return (nbImgBonnes*100)/imagettes.length;
    }
}
