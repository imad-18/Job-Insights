package Frontside;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GUI extends Application {

    private boolean isGraphVisible = false; // Pour gérer l'affichage des statistiques

    @Override
    public void start(Stage primaryStage) {
        // Zone de texte pour afficher les messages
        TextArea chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setPrefHeight(300);

        // Champ de saisie utilisateur
        TextField userInput = new TextField();
        userInput.setPromptText("Posez votre question ici...");

        // Bouton pour envoyer le message
        Button sendButton = new Button("Envoyer");

        // Graphique linéaire pour afficher des données
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineGraph = new LineChart<>(xAxis, yAxis);
        lineGraph.setTitle("Statistiques Linéaires");
        lineGraph.setPrefHeight(300);
        lineGraph.setVisible(false); // Caché par défaut

        // Graphique circulaire pour afficher des données
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des Statistiques");
        pieChart.setVisible(false); // Caché par défaut

        // Bouton pour afficher ou masquer les graphiques
        Button toggleGraphButton = new Button("▶");
        toggleGraphButton.setStyle("-fx-background-color: #6200ee; -fx-text-fill: white;");
        toggleGraphButton.setOnAction(event -> {
            isGraphVisible = !isGraphVisible;
            animateGraphVisibility(lineGraph, isGraphVisible);
            animateGraphVisibility(pieChart, isGraphVisible);
            toggleGraphButton.setText(isGraphVisible ? "▼" : "▶");
        });

        // Bouton pour accéder à la page des annonces
        Button annoncesButton = new Button("Voir les annonces");
        annoncesButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        annoncesButton.setOnAction(event -> showAnnoncesPage());

        // Layout pour les entrées utilisateur
        HBox inputLayout = new HBox(10, userInput, sendButton);
        inputLayout.setPadding(new Insets(10));
        inputLayout.setHgrow(userInput, Priority.ALWAYS);

        // Layout pour les boutons
        HBox buttonLayout = new HBox(10, toggleGraphButton, annoncesButton);
        buttonLayout.setPadding(new Insets(10));

        // Layout principal
        VBox root = new VBox(10, chatArea, inputLayout, buttonLayout, lineGraph, pieChart);
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f4f4f4;");

        // Action du bouton Envoyer
        sendButton.setOnAction(event -> {
            String userMessage = userInput.getText();
            if (!userMessage.isEmpty()) {
                chatArea.appendText("Utilisateur: " + userMessage + "\n");
                chatArea.appendText("Chatbot: Réponse à votre question.\n"); // Réponse fictive
                userInput.clear();
                // Mise à jour fictive des graphiques
                updateLineGraph(lineGraph);
                updatePieChart(pieChart);
            }
        });

        // Configuration de la scène
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Calculatrice Chatbot");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour mettre à jour le graphique linéaire
    private void updateLineGraph(LineChart<String, Number> lineGraph) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 10));
        series.getData().add(new XYChart.Data<>("Fév", 20));
        series.getData().add(new XYChart.Data<>("Mar", 15));
        series.getData().add(new XYChart.Data<>("Avr", 30));
        lineGraph.getData().clear();
        lineGraph.getData().add(series);
    }

    // Méthode pour mettre à jour le graphique circulaire
    private void updatePieChart(PieChart pieChart) {
        pieChart.getData().clear();
        pieChart.getData().add(new PieChart.Data("Catégorie A", 40));
        pieChart.getData().add(new PieChart.Data("Catégorie B", 30));
        pieChart.getData().add(new PieChart.Data("Catégorie C", 20));
        pieChart.getData().add(new PieChart.Data("Catégorie D", 10));
    }

    // Méthode pour afficher une animation d'entrée (cinématique)
    private void animateGraphVisibility(Region graph, boolean isVisible) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), graph);
        fade.setFromValue(isVisible ? 0.0 : 1.0);
        fade.setToValue(isVisible ? 1.0 : 0.0);
        fade.setOnFinished(event -> graph.setVisible(isVisible));
        fade.play();
    }

    // Page des annonces
    private void showAnnoncesPage() {
        Stage annoncesStage = new Stage();
        annoncesStage.setTitle("Annonces");

        // Filtres (Ville et Secteur)
        ComboBox<String> villeFilter = new ComboBox<>();
        villeFilter.getItems().addAll("Casablanca", "Rabat", "Fès", "Marrakech");
        villeFilter.setPromptText("Ville");

        ComboBox<String> secteurFilter = new ComboBox<>();
        secteurFilter.getItems().addAll("Informatique", "Finance", "Éducation", "Santé");
        secteurFilter.setPromptText("Secteur");

        Button filterButton = new Button("Filtrer");
        filterButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        HBox filterLayout = new HBox(10, villeFilter, secteurFilter, filterButton);
        filterLayout.setPadding(new Insets(10));

        // Zone pour afficher les annonces
        ListView<String> annoncesList = new ListView<>();
        annoncesList.getItems().addAll(
            "Annonce 1 : Informatique - Casablanca",
            "Annonce 2 : Finance - Rabat",
            "Annonce 3 : Éducation - Fès",
            "Annonce 4 : Santé - Marrakech"
        );

        VBox annoncesLayout = new VBox(10, filterLayout, annoncesList);
        annoncesLayout.setPadding(new Insets(15));

        // Configuration de la scène
        Scene annoncesScene = new Scene(annoncesLayout, 600, 400);
        annoncesStage.setScene(annoncesScene);
        annoncesStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
