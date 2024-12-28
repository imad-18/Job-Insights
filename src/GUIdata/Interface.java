package GUIdata;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Interface extends Application {

    @Override
    public void start(Stage stage) {
        // Create axes for the line chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        // Create the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Line Chart Example");

        // Add data series to the line chart
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Series 1");

        // Add points to the series
        series.getData().add(new XYChart.Data<>(1, 23));
        series.getData().add(new XYChart.Data<>(2, 14));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 24));
        series.getData().add(new XYChart.Data<>(5, 34));
        series.getData().add(new XYChart.Data<>(6, 36));
        series.getData().add(new XYChart.Data<>(7, 22));

        lineChart.getData().add(series);

        // Create two pie charts
        PieChart pieChart1 = new PieChart();
        pieChart1.setTitle("Category Distribution");

        PieChart.Data slice1 = new PieChart.Data("Category A", 30);
        PieChart.Data slice2 = new PieChart.Data("Category B", 25);
        PieChart.Data slice3 = new PieChart.Data("Category C", 45);

        pieChart1.getData().addAll(slice1, slice2, slice3);

        PieChart pieChart2 = new PieChart();
        pieChart2.setTitle("Department Distribution");

        PieChart.Data slice4 = new PieChart.Data("IT", 40);
        PieChart.Data slice5 = new PieChart.Data("Marketing", 35);
        PieChart.Data slice6 = new PieChart.Data("HR", 25);

        pieChart2.getData().addAll(slice4, slice5, slice6);

        // Layout for pie charts
        HBox hBox = new HBox(20); // Spacing between pie charts
        hBox.getChildren().addAll(pieChart1, pieChart2);

        // Layout for the entire interface
        VBox vBox = new VBox(20); // Spacing between charts
        vBox.getChildren().addAll(lineChart, hBox);

        // Create the scene and set the stage
        Scene scene = new Scene(vBox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("JavaFX Interface with Plot and Pie Charts");
        stage.show();
    }

}
