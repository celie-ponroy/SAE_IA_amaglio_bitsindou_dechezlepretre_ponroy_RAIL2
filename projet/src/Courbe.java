import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;

public class Courbe {

    public static void genererGraphique(int[] it, double[] errors, String name) {

        // Création des séries de données
        XYSeries errorSeries = new XYSeries("Erreur de sortie");
        for (int i = 0; i < it.length; i++) {
            errorSeries.add(it[i], errors[i]);
        }

        // Collection de séries pour le graphique
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(errorSeries);

        // Création du graphique
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Erreur de sortie vs Itérations", // Titre du graphique
                "Itérations",                    // Label de l'axe X
                "Erreur",                        // Label de l'axe Y
                dataset,                         // Données
                PlotOrientation.VERTICAL,        // Orientation
                true,                            // Légende
                true,                            // Info bulle
                false                            // URL
        );

        // Sauvegarde du graphique dans un fichier PNG
        try {
            File imageFile = new File("doc/courbes/" + name + ".png");
            ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
            System.out.println("Graphique sauvegardé sous 'doc/courbes/\"" + name + "\".png.png'");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du graphique : " + e.getMessage());
        }
    }
}