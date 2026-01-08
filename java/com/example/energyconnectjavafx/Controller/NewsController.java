package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.NewsDAO;
import com.example.energyconnectjavafx.Model.News;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.animation.PauseTransition;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import com.example.energyconnectjavafx.HelloApplication;
import javafx.scene.Cursor;

public class NewsController extends NavController implements Initializable {

    @FXML
    private ImageView logoHomepage;
    @FXML
    private ImageView featuredImage;
    @FXML
    private Label featuredAuthor;
    @FXML
    private Label featuredDateTime;
    @FXML
    private Label featuredHeadline;
    @FXML
    private Label featuredSummary;
    @FXML
    private TextField searchField; // for search feature
    @FXML
    private ComboBox<String> newsSorter; // for sorting
    @FXML
    private ScrollPane newsScroll;
    @FXML
    private HBox newsContainer; // all news container

    private Integer featuredNewsId = null;
    private final NewsDAO newsDao = new NewsDAO();
    private final PauseTransition searchDelay = new PauseTransition(javafx.util.Duration.millis(300));

    private String currentSort = "newest";
    private String currentKeyword = "";
    private String activeCategory = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logoHomepage.setCursor(Cursor.HAND);
        logoHomepage.setPickOnBounds(true);
        logoHomepage.setMouseTransparent(false);

        logoHomepage.setOnMouseClicked(e -> {
            try {
                goToHomepage(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        newsScroll.setContent(newsContainer);
        newsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        newsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        newsScroll.setFitToHeight(true);
        newsScroll.setPannable(true);
        newsScroll.setHvalue(0); // start at left

        newsScroll.setOnScroll(e -> {
            if (e.getDeltaY() != 0) {
                newsScroll.setHvalue(
                        newsScroll.getHvalue() - e.getDeltaY() / newsContainer.getWidth()
                );
            }
        });
        setupSearch();
        setupSorting();

        try {
            loadFeaturedNews();
            loadAllNews();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String formatDateTime(LocalDateTime datePosted) {
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(datePosted, now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours();

        if (minutes < 1) {
            return "Just now";
        }

        if (minutes < 60) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        }

        if (hours < 24) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return datePosted.format(formatter);
    }

    private void loadFeaturedNews() throws SQLException {
        News news = newsDao.getFeaturedNews();

        if (news == null) {
            return;
        }

        featuredNewsId = news.getId();

        featuredHeadline.setText(news.getHeadline());
        featuredSummary.setText(news.getSummary());

        featuredAuthor.setText(news.getAuthor() != null ? news.getAuthor() : "Unknown Author");
        featuredAuthor.setCursor(Cursor.HAND);

        featuredAuthor.setOnMouseClicked(e -> {
            if (news.getSourceUrl() != null && !news.getSourceUrl().isBlank()) {
                HelloApplication.getHostServicesInstance()
                        .showDocument(news.getSourceUrl());
            }
        });

        featuredDateTime.setText(formatDateTime(news.getDatePosted()));

        Image image = new Image(news.getImagePath(), true);
        featuredImage.setImage(image);
        featuredImage.setCursor(Cursor.HAND);
        featuredImage.setOnMouseClicked(e -> {
            if (news.getSourceUrl() != null && !news.getSourceUrl().isBlank()) {
                HelloApplication.getHostServicesInstance()
                        .showDocument(news.getSourceUrl());
            }
        });
    }

    /**
     * Search Feature ( UI behavior, display search results)
     */
    private void setupSearch() {
        searchField.textProperty().addListener(
                (obs, oldText, newText) -> {
                    searchDelay.setOnFinished(event -> {
                        currentKeyword = (newText == null) ? "" : newText.trim();
                        refreshNews();
                    });

                    searchDelay.playFromStart();
                }
        );
    }

    /**
     * News Sorting
     */
    private void setupSorting() {

        newsSorter.getItems().addAll(
                "Newest - Oldest",
                "Oldest - Newest",
                "Title (A-Z)",
                "Organization (A-Z)",
                "Author (A-Z)"
        );

        newsSorter.getSelectionModel().selectFirst();

        newsSorter.valueProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if(newVal == null) return;

                    switch(newVal) {
                        case "Oldest - Newest":
                            currentSort = "oldest";
                            break;
                        case "Title (A-Z)":
                            currentSort = "title-az";
                            break;
                        case "Organization (A-Z)":
                            currentSort = "org-az";
                            break;
                        case "Author (A-Z)":
                            currentSort = "author-az";
                            break;
                        case "Newest - Oldest":
                        default:
                            currentSort = "newest";
                            break;
                    }

                    refreshNews();
                }
        );
    }

    /**
     * Category Filtering logic
     */
    private void onCategoryClicked(String category) {

        // same category clicked - toggle off
        if(category.equalsIgnoreCase(activeCategory)) {
            activeCategory = null;
            refreshNews();
            return;
        }

        // when new category selected
        activeCategory =
                category.toLowerCase();
        refreshNews();
    }

    private void refreshNews() {
        List<News> list;

        try {
            if (currentKeyword.isBlank()) {
                list = newsDao.getSortedNews(currentSort);
            } else {
                list = newsDao.searchNews(currentKeyword);
            }

            if (featuredNewsId != null) {
                list.removeIf(n -> n.getId() == featuredNewsId);
            }

            // category filter
            if (activeCategory != null) {
                list = list.stream()
                        .filter(news ->
                                news.getCategories() != null &&
                                        news.getCategories().stream()
                                                .anyMatch(cat ->
                                                        cat.equalsIgnoreCase(activeCategory)
                                                )
                        )
                        .toList();
            }
            renderNews(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadSearchResults(String keyword) throws SQLException {
        List<News> results = newsDao.searchNews(keyword);
        renderNews(results);
    }

    private void loadAllNews() throws SQLException {
        List<News> newsList = newsDao.getAllNews();
        if (featuredNewsId != null) {
            newsList.removeIf(n -> n.getId() == featuredNewsId);
        }
        renderNews(newsList);
    }

    // Display only the searched result (hide the default news)
    private void renderNews(List<News> newsList) {
        newsContainer.getChildren().clear();

        for (News nl : newsList) {
            newsContainer.getChildren().add(createNewsCard(nl));
        }
    }

    private VBox createNewsCard(News nl) {
        // news card
        VBox card = new VBox(10);
        card.setAlignment(Pos.TOP_LEFT);
        card.setPrefWidth(350);
        card.setMinWidth(350);
        card.setPrefHeight(530);
        card.setMinHeight(530);
        card.setStyle("-fx-background-color: #EEEEEE;" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 10;" +
                "-fx-effect: dropshadow(two-pass-box, rgba(0,0,0.1), 10, 0, 0, 0)");

        // news image
        ImageView newsImage = new ImageView();
        newsImage.setFitWidth(300);
        newsImage.setFitHeight(250);
        newsImage.setPreserveRatio(false);
        newsImage.setCursor(Cursor.HAND);

        if (nl.getImagePath() != null) {
            newsImage.setImage(new Image(nl.getImagePath(), true));
        }

        newsImage.setOnMouseClicked(e -> {
            if (nl.getSourceUrl() != null && !nl.getSourceUrl().isBlank()) {
                HelloApplication.getHostServicesInstance()
                        .showDocument(nl.getSourceUrl());
            }
        });

        VBox imageWrapper = new VBox(newsImage);
        imageWrapper.setAlignment(Pos.CENTER);
        imageWrapper.setMinWidth(300);
        imageWrapper.setPrefWidth(300);
        imageWrapper.setPrefHeight(250);

        // news headline
        Label newsHeadline = new Label(nl.getHeadline());
        newsHeadline.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        newsHeadline.setWrapText(true);

        // news organization label
        Label orgLabel = new Label(
                nl.getOrganization()
        );
        orgLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        // dot (between org and dateTime)
        Circle dot = new Circle(3);
        dot.setFill(Color.web("#393939"));
        dot.setRadius(3.0);

        // news date label
        Label dateLabel = new Label(formatDateTime(nl.getDatePosted()));
        dateLabel.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        // container for org • date
        HBox orgAndDate = new HBox(6); // spacing
        orgAndDate.setAlignment(Pos.CENTER_LEFT);
        orgAndDate.getChildren().addAll(orgLabel, dot, dateLabel);

        // news summary
        Label newsSummary = new Label(nl.getSummary());
        newsSummary.setStyle("-fx-font-size: 13;");
        newsSummary.setWrapText(true);

        // news author
        Label newsAuthor = new Label(
                nl.getAuthor() != null ? "By " + nl.getAuthor() : "By Unknown"
        );
        newsAuthor.setStyle("-fx-font-size: 12;");
        newsAuthor.setCursor(Cursor.HAND);
        newsAuthor.setWrapText(false);

        newsAuthor.setOnMouseClicked(e -> {
            if (nl.getSourceUrl() != null && !nl.getSourceUrl().isBlank()) {
                HelloApplication.getHostServicesInstance()
                        .showDocument(nl.getSourceUrl());
            }
        });

        // news category
        HBox categoryContainer = new HBox(5);

        if (nl.getCategories() != null) {
            for (String c : nl.getCategories()) {
                Button categoryPill = new Button(c);
                categoryPill.setWrapText(false);

                categoryPill.setOnAction(e ->
                        onCategoryClicked(c));

                if (activeCategory != null && c.equalsIgnoreCase(activeCategory)) {
                    categoryPill.setStyle(
                            "-fx-background-color: rgba(28, 81, 134, 1);" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 14;" +
                                    "-fx-padding: 4 10 4 10;" +
                                    "-fx-font-size: 11;"
                    );
                } else {
                    categoryPill.setStyle(
                            "-fx-background-color:  rgba(116, 114, 114, 1);" +
                                    "-fx-text-fill: white;" +
                                    "-fx-background-radius: 14;" +
                                    "-fx-padding: 4 10 4 10;" +
                                    "-fx-font-size: 11;"
                    );
                }

                categoryContainer.getChildren().add(categoryPill);
            }
        }

        // container for author & category
        HBox authorAndCategory = new HBox(10);
        authorAndCategory.setAlignment(Pos.CENTER_LEFT);
        authorAndCategory.getChildren().addAll(newsAuthor, categoryContainer);

        card.getChildren().addAll(
                imageWrapper,
                newsHeadline,
                orgAndDate,
                newsSummary,
                authorAndCategory
        );

        return card;
    }
}
