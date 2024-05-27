package telegrambot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class BarChartExample extends JFrame {

    public BarChartExample(String title) {
        super(title);

        // Créer le dataset
        CategoryDataset dataset = createDataset();

        // Créer le graphique
        JFreeChart chart = ChartFactory.createBarChart(
                "Exemple de graphique à barres",
                "Catégorie",
                "Score",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // Créer un panneau de la charte
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(1, "Groupe 1", "Catégorie 1");
        dataset.addValue(4, "Groupe 1", "Catégorie 2");
        dataset.addValue(3, "Groupe 1", "Catégorie 3");
        dataset.addValue(5, "Groupe 1", "Catégorie 4");

        dataset.addValue(5, "Groupe 2", "Catégorie 1");
        dataset.addValue(7, "Groupe 2", "Catégorie 2");
        dataset.addValue(6, "Groupe 2", "Catégorie 3");
        dataset.addValue(8, "Groupe 2", "Catégorie 4");

        dataset.addValue(4, "Groupe 3", "Catégorie 1");
        dataset.addValue(3, "Groupe 3", "Catégorie 2");
        dataset.addValue(2, "Groupe 3", "Catégorie 3");
        dataset.addValue(3, "Groupe 3", "Catégorie 4");

        return dataset;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BarChartExample example = new BarChartExample("Exemple de graphique à barres");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}

