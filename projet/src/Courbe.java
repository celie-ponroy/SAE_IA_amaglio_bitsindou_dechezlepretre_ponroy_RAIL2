import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;

public class Courbe {

    /**
     * Génère un graphique à partir des données d'erreurs
     * @param it les itérations
     * @param errors les erreurs
     * @param name le nom du graphique
     */
    public static void genererGraphique(int[] it, double[] errors, String name,
                                        double learningRate, int[] nbNeurones,
                                        String dossierArrivee) {

        // Vérification du tableau de neurones
        if (nbNeurones.length < 2) {
            throw new IllegalArgumentException("Le tableau 'nbNeurones' doit contenir au moins deux éléments (entrées et sorties).");
        }

        // Création des séries de données
        XYSeries errorSeries = new XYSeries("Erreur de sortie");
        for (int i = 0; i < it.length; i++) {
            errorSeries.add(it[i], errors[i]);
        }

        // Collection de séries pour le graphique
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(errorSeries);

        // Construction de la description des paramètres
        StringBuilder details = new StringBuilder();
        details.append("Taux d'apprentissage: ").append(learningRate).append("\n")
                .append("Structure du réseau: ");
        for (int i = 0; i < nbNeurones.length; i++) {
            details.append(nbNeurones[i]);
            if (i < nbNeurones.length - 1) details.append(" -> ");
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Erreur de sortie en fonction des itérations", // Titre du graphique
                "Itérations",                    // Label de l'axe X
                "Erreur",                        // Label de l'axe Y
                dataset,                         // Données
                PlotOrientation.VERTICAL,        // Orientation
                true,                            // Légende
                true,                            // Info bulle
                false                            // URL
        );

        // Ajout des informations dans le sous-titre
        chart.addSubtitle(new org.jfree.chart.title.TextTitle(details.toString()));



        // Sauvegarde du graphique dans un fichier PNG
        try {
            // Créer le dossier s'il n'existe pas
            File dossier = new File("doc/" + dossierArrivee);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            File imageFile = new File("doc/"+ dossierArrivee+"/" + name + ".png");
            ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
            System.out.println("Graphique sauvegardé sous 'doc/"+ dossierArrivee+"/" + name + ".png'");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du graphique : " + e.getMessage());
        }
    }

    /**
     * Génère un graphique à partir des données d'erreurs avec des informations sur la structure du réseau
     *
     * @param it           les itérations
     * @param errors       les erreurs
     * @param name         le nom du graphique
     * @param learningRate le taux d'apprentissage
     * @param nbNeurones   tableau contenant la structure du réseau (ex : {2, 4, 2})
     * @param dossierArrivee   le chemin où sauvegarder le graphique
     */
    public static void genererGraphiqueAvecInfos(int[] it, double[] errors, String name,
                                                 double learningRate, int[] nbNeurones,
                                                 String dossierArrivee) {

        // Vérification du tableau de neurones
        if (nbNeurones.length < 2) {
            throw new IllegalArgumentException("Le tableau 'nbNeurones' doit contenir au moins deux éléments (entrées et sorties).");
        }

        // Création des séries de données
        XYSeries errorSeries = new XYSeries("Erreur de sortie");
        for (int i = 0; i < it.length; i++) {
            errorSeries.add(it[i], errors[i]);
        }

        // Collection de séries pour le graphique
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(errorSeries);

        // Construction de la description des paramètres
        StringBuilder details = new StringBuilder();
        details.append("Taux d'apprentissage: ").append(learningRate).append("\n")
                .append("Structure du réseau: ");
        for (int i = 0; i < nbNeurones.length; i++) {
            details.append(nbNeurones[i]);
            if (i < nbNeurones.length - 1) details.append(" -> ");
        }

        // Création du graphique
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Pourcentage de réussite en fonction des itérations\n", // Titre principal
                "Itérations",                                                 // Label de l'axe X
                "Erreur",                                                     // Label de l'axe Y
                dataset,                                                      // Données
                PlotOrientation.VERTICAL,                                     // Orientation
                true,                                                         // Légende
                true,                                                         // Info bulle
                false                                                         // URL
        );

        // Ajout des informations dans le sous-titre
        chart.addSubtitle(new org.jfree.chart.title.TextTitle(details.toString()));

        // Sauvegarde du graphique dans un fichier PNG
        try {

            // Créer le dossier s'il n'existe pas
            File dossier = new File("doc/" + dossierArrivee);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            // Sauvegarder le fichier
            File imageFile = new File(dossier, name + ".png");
            ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
            System.out.println("Graphique sauvegardé sous '" + imageFile.getPath() + "'");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde du graphique : " + e.getMessage());
        }
    }
}