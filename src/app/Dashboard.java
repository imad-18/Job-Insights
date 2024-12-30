package app;

import db.Database;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import rmi_api.Annonce;
import scraper.Sitemanager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dashboard extends Application {
    private TableView<JobAnnouncement> announcementTable;
    private PieChart siteDistributionChart;
    private BarChart<String, Number> userFeedbackChart;
    String url = "jdbc:mysql://localhost:3306/job_insight";
    String user = "root";
    String password = "";
    Database database = new Database(url,user,password);
    Sitemanager sitemanager = new Sitemanager();
    String StatusCol;
    String SiteCol;
    String LastUpdate;
    String NextUpdate;
    List<Annonce> annonceList;
    public static double moyenneperjourMjob;
    public static double moyenneperjourEmploiMA;
    public static double moyenneperjourRekrute;
    public List<Integer> startList = new ArrayList<>(Arrays.asList(1, 2,3,4,5));


    public static Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/job_insight";
        String user = "root";
        String password = "";
        return DriverManager.getConnection(url, user, password);
    }


    public Dashboard() throws SQLException {
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        VBox sideMenu = createSideMenu();
        BorderPane root = new BorderPane();
        root.setLeft(sideMenu);
        root.setCenter(createDashboard());

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Interface Administratif - Système de Chatbot d'Emploi");
        primaryStage.setScene(scene);
        primaryStage.show();
        moyenneperjourRekrute = database.getAverageAnnoncesPerDay("rekrute");
        moyenneperjourMjob = database.getAverageAnnoncesPerDay("M-job");
        moyenneperjourEmploiMA = database.getAverageAnnoncesPerDay("emploi.ma");
    }

    private VBox createSideMenu() {
        VBox sideMenu = new VBox(20);
        sideMenu.setStyle("-fx-background-color: #2f4f4f; -fx-padding: 20;");
        sideMenu.setPrefWidth(250);

        Label menuTitle = new Label("Menu Administrateur");
        menuTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button[] buttons = {
                createMenuButton("Tableau de Bord", this::createDashboard),
                createMenuButton("Gestion BD", this::createDBManagement),
                createMenuButton("Gestion Scraping", this::createScrapingManagement),
                createMenuButton("Statistiques Annonces", this::createStatsPage),
                createMenuButton("Avis Utilisateurs", this::createUserFeedbackPage)
        };

        sideMenu.getChildren().add(menuTitle);
        sideMenu.getChildren().addAll(buttons);
        return sideMenu;
    }

    private Button createMenuButton(String text, Interface handler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #4a6f6f; -fx-text-fill: white; -fx-min-width: 200;");
        button.setOnAction(e -> {
            BorderPane root = (BorderPane) button.getScene().getRoot();
            try {
                root.setCenter(handler.createPane());
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        return button;
    }

    private BorderPane createDashboard() throws SQLException {
        BorderPane dashboard = new BorderPane();
        dashboard.setPadding(new Insets(20));

        // En-tête
        Label title = new Label("Tableau de Bord");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        dashboard.setTop(title);

        // Statistiques générales
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setPadding(new Insets(20));

        // Cartes de statistiques
        //data
        String TotaleAnnonces = String.valueOf(database.DBsize());
        String MoyenneParperiode = String.valueOf(moyenneperjourMjob+moyenneperjourEmploiMA+moyenneperjourRekrute);
        statsGrid.add(createStatCard("Total Annonces", TotaleAnnonces, "↑ 12%"), 0, 0);
        statsGrid.add(createStatCard("Moyenne/Jour", MoyenneParperiode, "↑ 5%"), 1, 0);
        statsGrid.add(createStatCard("Sites Actifs", "3", "stable"), 2, 0);
        statsGrid.add(createStatCard("Score Chatbot", "4.2/5", "↑ 0.3"), 3, 0);

        // Graphiques
        HBox chartsBox = new HBox(20);
        chartsBox.setPadding(new Insets(20));

        // Distribution par site
        PieChart siteChart = createSiteDistributionChart();

        // Activité récente
        LineChart<String, Number> activityChart = createActivityChart();

        chartsBox.getChildren().addAll(siteChart, activityChart);

        VBox centerContent = new VBox(20);
        centerContent.getChildren().addAll(statsGrid, chartsBox);
        dashboard.setCenter(centerContent);

        return dashboard;
    }

    private BorderPane createDBManagement() {
        BorderPane dbPane = new BorderPane();
        dbPane.setPadding(new Insets(20));

        VBox content = new VBox(20);
        Label title = new Label("Gestion de la Base de Données");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Table des annonces
        announcementTable = new TableView<>();
        setupAnnouncementTable();

        // Boutons de gestion
        HBox buttonBox = new HBox(20);
        Button deleteAllButton = new Button("Supprimer toute la base");
        deleteAllButton.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");

        ComboBox<String> siteSelector = new ComboBox<>();
        siteSelector.getItems().addAll("mjob", "Site 2", "Site 3");

        Button deleteSiteButton = new Button("Supprimer données du site");
        deleteSiteButton.setStyle("-fx-background-color: #ff8800; -fx-text-fill: white;");

        buttonBox.getChildren().addAll(deleteAllButton, siteSelector, deleteSiteButton);

        content.getChildren().addAll(title, buttonBox, announcementTable);
        dbPane.setCenter(content);

        return dbPane;
    }

    private BorderPane createScrapingManagement() {
        BorderPane scrapingPane = new BorderPane();
        scrapingPane.setPadding(new Insets(20));

        VBox content = new VBox(20);
        Label title = new Label("Gestion du Scraping");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Contrôles de scraping
        GridPane controls = new GridPane();
        controls.setHgap(20);
        controls.setVgap(20);
        controls.setPadding(new Insets(20));

        ComboBox<String> siteSelector = new ComboBox<>();
        siteSelector.getItems().addAll("Mjob", "emploiMA", "Rekrute");
        siteSelector.setPromptText("Sélectionner un site");

        ComboBox<String> frequencySelector = new ComboBox<>();
        frequencySelector.getItems().addAll("24 heures", "48 heures");
        frequencySelector.setPromptText("Fréquence de scraping");

        Button updateButton = new Button("Actualiser maintenant");
        Button scheduleButton = new Button("Programmer le scraping");

        updateButton.setOnAction(e -> {
            String selectedSite = siteSelector.getValue();
            SiteCol=selectedSite;
            StatusCol="Actualisé";
            LocalDateTime now = LocalDateTime.now();

            // Créez un formateur pour la date et l'heure au format "yyyy-MM-dd HH:mm"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // Formatez la date et l'heure actuelles en chaîne
            LastUpdate = now.format(formatter);
            NextUpdate = "Nouveau update";
            if (selectedSite != null) {
                switch (selectedSite) {
                    case "Mjob":
                        try {
                            annonceList = sitemanager.ActualiserScrappingMjob();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "emploiMA":
                        try {
                            annonceList = sitemanager.ActualiserScrappingEmploiMA();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "Rekrute":
                        try {
                            annonceList = sitemanager.ActualiserScrappingRekrute();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    default:
                        showAlert("Erreur", "Site non pris en charge.");
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner un site et une fréquence.");
            }
        });


        // Event handler for the schedule button
        scheduleButton.setOnAction(e -> {
            String selectedSite = siteSelector.getValue();
            String selectedFrequency = frequencySelector.getValue();

            SiteCol=selectedSite;
            StatusCol="Programmé";
            LocalDateTime now = LocalDateTime.now();

            // Créez un formateur pour la date et l'heure au format "yyyy-MM-dd HH:mm"
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            // Formatez la date et l'heure actuelles en chaîne
            LastUpdate = now.format(formatter);

            LocalDateTime nextUpdateTime = now;

            if (selectedSite != null && selectedFrequency != null) {
                long frequencyInHours = 0; // Default value

                // Convert frequency to hours (as long)
                switch (selectedFrequency) {
                    case "24 heures":
                        frequencyInHours = 24; // 24 hours
                        nextUpdateTime = now.plusHours(24);
                        break;
                    case "48 heures":
                        frequencyInHours = 48; // 48 hours
                        nextUpdateTime = now.plusHours(48);

                        break;
                    default:
                        nextUpdateTime = now; // Par défaut, pas de changement
                        showAlert("Erreur", "Fréquence non prise en charge.");
                        return; // Exit if frequency is not recognized
                }

                NextUpdate = nextUpdateTime.format(formatter);

                // Call the schedule scraping function based on the selected values
                switch (selectedSite) {
                    case "Mjob":
                        try {
                            sitemanager.ProgrammerScrappingMjob(frequencyInHours);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "emploiMA":
                        try {
                            sitemanager.ProgrammerScrappingEmploiMA(frequencyInHours);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    case "Rekrute":
                        try {
                            sitemanager.ProgrammerScrappingRekrute(frequencyInHours);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        break;
                    default:
                        showAlert("Erreur", "Site non pris en charge.");
                }
            } else {
                showAlert("Erreur", "Veuillez sélectionner un site et une fréquence.");
            }
        });


        controls.add(new Label("Site:"), 0, 0);
        controls.add(siteSelector, 1, 0);
        controls.add(new Label("Fréquence:"), 0, 1);
        controls.add(frequencySelector, 1, 1);
        controls.add(updateButton, 0, 2);
        controls.add(scheduleButton, 1, 2);

        // Statut du scraping
        TableView<ScrapingStatus> statusTable = createScrapingStatusTable(annonceList);

        content.getChildren().addAll(title, controls, new Label("Statut du Scraping:"), statusTable);
        scrapingPane.setCenter(content);

        return scrapingPane;
    }

    private BorderPane createStatsPage() {
        BorderPane statsPane = new BorderPane();
        statsPane.setPadding(new Insets(20));

        VBox content = new VBox(20);
        Label title = new Label("Statistiques des Annonces");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Graphiques
        HBox chartsRow1 = new HBox(20);
        chartsRow1.getChildren().addAll(
                createSectorChart(),
                createRegionChart()
        );

        HBox chartsRow2 = new HBox(20);
        chartsRow2.getChildren().addAll(
                createExperienceChart(),
                createEducationLevelChart()
        );

        content.getChildren().addAll(title, chartsRow1, chartsRow2);
        statsPane.setCenter(content);

        return statsPane;
    }

    private BorderPane createUserFeedbackPage() {
        BorderPane feedbackPane = new BorderPane();
        feedbackPane.setPadding(new Insets(20));

        VBox content = new VBox(20);
        Label title = new Label("Statistiques des Avis Utilisateurs");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Statistiques des questions
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setPadding(new Insets(20));

        statsGrid.add(createFeedbackCard("Question la plus posée",
                "\"Quels sont les prérequis pour ce poste?\"", "342 fois"), 0, 0);
        statsGrid.add(createFeedbackCard("Question avec le plus d'erreurs",
                "\"Quel est le salaire proposé?\"", "Taux d'erreur: 15%"), 1, 0);

        // Graphique de satisfaction
        BarChart<String, Number> satisfactionChart = createSatisfactionChart();

        content.getChildren().addAll(title, statsGrid, satisfactionChart);
        feedbackPane.setCenter(content);

        return feedbackPane;
    }

    // Méthodes utilitaires
    private VBox createStatCard(String title, String value, String trend) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label trendLabel = new Label(trend);
        trendLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " +
                (trend.contains("↑") ? "green" : trend.contains("↓") ? "red" : "#666"));

        card.getChildren().addAll(titleLabel, valueLabel, trendLabel);
        return card;
    }

    private VBox createFeedbackCard(String title, String question, String stats) {
        startList = database.getAll();
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label questionLabel = new Label(question);
        questionLabel.setStyle("-fx-font-size: 14px; -fx-wrap-text: true;");

        Label statsLabel = new Label(stats);
        statsLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666;");

        card.getChildren().addAll(titleLabel, questionLabel, statsLabel);
        return card;
    }

    private void setupAnnouncementTable() {
        TableColumn<JobAnnouncement, String> titleCol = new TableColumn<>("Titre");
        TableColumn<JobAnnouncement, String> siteCol = new TableColumn<>("Site");
        TableColumn<JobAnnouncement, String> dateCol = new TableColumn<>("Date");
        TableColumn<JobAnnouncement, String> statusCol = new TableColumn<>("fonction");

        announcementTable.getColumns().addAll(titleCol, siteCol, dateCol, statusCol);
    }

    private ObservableList<ScrapingStatus> data = FXCollections.observableArrayList();  // Déclarer la liste observable en tant que champ de classe

    private TableView<ScrapingStatus> createScrapingStatusTable(List<Annonce> annonces) {
        TableView<ScrapingStatus> table = new TableView<>();

        // Create the table columns
        TableColumn<ScrapingStatus, String> siteCol = new TableColumn<>("Site");
        TableColumn<ScrapingStatus, String> lastUpdateCol = new TableColumn<>("Dernière MàJ");
        TableColumn<ScrapingStatus, String> nextUpdateCol = new TableColumn<>("Prochaine MàJ");
        TableColumn<ScrapingStatus, String> statusCol = new TableColumn<>("Statut");

        table.getColumns().addAll(siteCol, lastUpdateCol, nextUpdateCol, statusCol);

        if (annonces != null ) {
            // Calculer la taille de la liste
            int size = annonces.size();

            // Créez un objet ScrapingStatus pour chaque Annonce
            String sitecol = SiteCol;
            if (StatusCol.equals("Actualisé")) {
                sitecol = SiteCol + " nbrs des annonces ajouté: " + size;
            }

            // Créez une nouvelle entrée de statut
            ScrapingStatus status = new ScrapingStatus(sitecol, LastUpdate, NextUpdate, StatusCol);

            // Ajouter le statut en haut de la liste observable
            data.add(0, status);  // Ajouter à l'index 0 pour que ce soit en haut
        }else{

            ScrapingStatus status = new ScrapingStatus(SiteCol, LastUpdate, NextUpdate, StatusCol);

            // Ajouter le statut en haut de la liste observable
            data.add(0, status);
        }

        // Définir les cellValueFactory pour chaque colonne
        siteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSiteName()));
        lastUpdateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLastUpdate()));
        nextUpdateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNextUpdate()));
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        // Ajouter les données à la table (les données sont maintenant maintenues entre les appels)
        table.setItems(data);

        return table;
    }




    private PieChart createSiteDistributionChart() throws SQLException {
        double Mjob = database.getPercentageBySiteName("M-job");
        double Rekrute = database.getPercentageBySiteName("rekrute");
        double EmploiMA = database.getPercentageBySiteName("emploi.ma");
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("mjob", Mjob),
                new PieChart.Data("Rekrute", Rekrute),
                new PieChart.Data("Emploi Ma", EmploiMA)
        );
        return new PieChart(pieChartData);
    }

    private LineChart<String, Number> createActivityChart() throws SQLException {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Activité des Annonces par site (moyenne par jour)");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Site name");

        series.getData().addAll(
                new XYChart.Data<>("Mjob", database.getAverageAnnoncesPerDay("M-job")),
                new XYChart.Data<>("Rekrute", database.getAverageAnnoncesPerDay("rekrute")),
                new XYChart.Data<>("EmploiMA", database.getAverageAnnoncesPerDay("emploi.ma"))
        );

        lineChart.getData().add(series);
        return lineChart;
    }

    private BarChart<String, Number> createSatisfactionChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Satisfaction des Utilisateurs");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Notes moyennes");
        if((database.getAll() !=null)){
        startList = database.getAll();
        }

        System.out.println(startList.size());
        Map<Object, Long> occurrences = startList.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

        // Afficher les résultats
        occurrences.forEach((key, value) -> System.out.println("Nombre: " + key + ", Occurrences: " + value));
        System.out.println(occurrences.get(5));
        series.getData().addAll(
                new XYChart.Data<>("5 étoiles", 3),
                new XYChart.Data<>("4 étoiles", 5),
                new XYChart.Data<>("3 étoiles", 9),
                new XYChart.Data<>("2 étoiles", 3),
                new XYChart.Data<>("1 étoile", 4)
        );

        barChart.getData().add(series);
        return barChart;
    }

    private PieChart createSectorChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String query = "SELECT Secteur, COUNT(*) AS sector_count FROM annonce GROUP BY Secteur";

        try (Connection conn = connect(); // Directly calling the connect() method
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String sectorName = rs.getString("Secteur");
                int sectorCount = rs.getInt("sector_count");
                pieChartData.add(new PieChart.Data(sectorName, sectorCount));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Distribution par Secteur");
        return chart;
    }


    private BarChart<String, Number> createRegionChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Distribution par Région");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Annonces par Région");

        String query = "SELECT Region, COUNT(*) AS region_count FROM annonce GROUP BY Region";

        try (Connection conn = connect(); // Assuming connect() method is defined
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String regionName = rs.getString("Region");
                int regionCount = rs.getInt("region_count");
                series.getData().add(new XYChart.Data<>(regionName, regionCount));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        barChart.getData().add(series);
        return barChart;
    }


    private BarChart<String, Number> createExperienceChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Niveau d'Expérience Requis");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expérience Requise");

        String query = "SELECT Experience, COUNT(*) AS experience_count FROM annonce GROUP BY Experience";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String experienceLevel = rs.getString("Experience");
                int experienceCount = rs.getInt("experience_count");
                series.getData().add(new XYChart.Data<>(experienceLevel, experienceCount));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        barChart.getData().add(series);
        return barChart;
    }

    private PieChart createEducationLevelChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String query = "SELECT EtudeLevel, COUNT(*) AS education_count FROM annonce GROUP BY EtudeLevel";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String educationLevel = rs.getString("EtudeLevel");
                int educationCount = rs.getInt("education_count");
                pieChartData.add(new PieChart.Data(educationLevel, educationCount));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions appropriately
        }

        PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Niveau d'Études Requis");
        return chart;
    }

    // Classes de modèle
    public static class JobAnnouncement {
        private int id;
        private String title;
        private String description;
        private String startDate;
        private String endDate;
        private int postsNum;
        private String secteur;
        private String fonction;
        private String experience;
        private String etudeLevel;
        private String contratDetails;
        private String url;
        private String siteName;
        private String adresseEntreprise;
        private String siteWebEntreprise;
        private String nomEntreprise;
        private String descriptionEntreprise;
        private String region;
        private String city;
        private String industry;
        private String traitsPersonnalite;
        private String competencesRequises;
        private String softSkills;
        private String competencesRecommandees;
        private String langue;
        private String niveauLangue;
        private String salaire;

        // Getters et Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        // ... autres getters et setters
    }

    public static class ScrapingStatus {
        private String siteName;
        private String lastUpdate;
        private String nextUpdate;
        private String status;

        public ScrapingStatus(String siteName, String lastUpdate, String nextUpdate, String status) {
            this.siteName = siteName;
            this.lastUpdate = lastUpdate;
            this.nextUpdate = nextUpdate;
            this.status = status;
        }

        // Getters
        public String getSiteName() { return siteName; }
        public String getLastUpdate() { return lastUpdate; }
        public String getNextUpdate() { return nextUpdate; }
        public String getStatus() { return status; }
    }

    private interface Interface {
        BorderPane createPane() throws SQLException;
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
