import com.google.common.base.Stopwatch;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainTest {
 public static void main(String[] args) throws IOException {
     System.out.println("Chargement des données...");
     Imagette[] imgTest = Imagette.charger("doc/baque_images/t10k-images.idx3-ubyte");
     Etiquette[] etiquettesTest = Etiquette.charger("doc/baque_images/t10k-labels.idx1-ubyte");
     Donnees dTest = new Donnees(imgTest);
     dTest.etiquetter(etiquettesTest);

     MLP mlp = MLP.charge("doc/res/500_100_0.1_sigmoide");
     Stopwatch stopwatch = Stopwatch.createStarted();
     StatsRN.testerReseauNeurone(mlp,imgTest);
     stopwatch.stop();
     System.out.println("Temps d'exécution: " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");



 }
}
