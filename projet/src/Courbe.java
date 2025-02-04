import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.title.TextTitle;

import java.io.File;
import java.io.IOException;

/**
 * Classe permettant de générer des graphiques à partir de données
 */

public class Courbe {

    /**
     * Génère un graphique à partir des données fournies.
     * @param it le nombre d'itérations
     * @param values les valeurs à afficher
     * @param name Nom du fichier de sortie
     * @param learningRate le pas d'apprentissage
     * @param nbNeurones la structure du réseau
     * @param dossierArrivee le dossier de destination
     * @param titre Titre du graphique
     * @param labelY Légende de l'axe Y
     */
    public static void genererGraphique(int[] it, double[] values, String name,
                                        double learningRate, int[] nbNeurones,
                                        String dossierArrivee, String titre, String labelY) {

        if (it.length != values.length) {
            throw new IllegalArgumentException("Les tableaux 'it' et 'values' doivent avoir la même longueur.");
        }
        if (nbNeurones.length < 2) {
            throw new IllegalArgumentException("Le tableau 'nbNeurones' doit contenir au moins deux éléments (entrées et sorties).");
        }

        XYSeries series = new XYSeries(labelY);
        for (int i = 0; i < it.length; i++) {
            series.add(it[i], values[i]);
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        // détails des paramètres
        StringBuilder details = new StringBuilder();
        details.append("Taux d'apprentissage: ").append(learningRate).append("\n")
                .append("Structure du réseau: ");
        for (int i = 0; i < nbNeurones.length; i++) {
            details.append(nbNeurones[i]);
            if (i < nbNeurones.length - 1) details.append(" -> ");
        }

        // Calcul du point de convergence
        double seuilConvergence = 0.1;
        int convergenceIteration = trouverPointDeConvergence(values, seuilConvergence);
        details.append("\nPoint de convergence: ");
        if (convergenceIteration > 0) {
            details.append(convergenceIteration).append(" itérations (seuil: ").append(seuilConvergence).append(")");
        } else {
            details.append("non atteint (seuil: ").append(seuilConvergence).append(")");
        }

        // Valeur finale (réussite ou erreur)
        double valeurFinale = values[values.length - 1];
        details.append("\nValeur finale (").append(labelY).append(") : ")
                .append(String.format("%.4f", valeurFinale));

        // Création du graphique
        JFreeChart chart = ChartFactory.createXYLineChart(
                titre,
                "Itérations",
                labelY,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        chart.addSubtitle(new TextTitle(details.toString()));

        try {
            File dossier = new File("doc/" + dossierArrivee);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            File imageFile = new File(dossier, name + ".png");
            ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
            System.out.println("Graphique sauvegardé sous '" + imageFile.getPath() + "'");

        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du graphique : " + e.getMessage());
            throw new RuntimeException("Échec de la sauvegarde du graphique", e);
        }
    }

    /**
     * Permet de trouver le point de convergence d'une série de valeurs.
     * @param values la série de valeurs
     * @param seuil le seuil de convergence
     * @return le nombre d'itérations avant de converger
     */
    private static int trouverPointDeConvergence(double[] values, double seuil) {
        for (int i = 0; i < values.length; i++) {
            if (values[i] <= seuil) {
                return i + 1;
            }
        }
        return values.length;
    }
}
