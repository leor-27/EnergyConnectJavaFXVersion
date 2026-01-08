package com.example.energyconnectjavafx.Controller;

import com.example.energyconnectjavafx.DAO.NewsDAO;
import com.example.energyconnectjavafx.Model.News;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

public class AdminHomeController extends ProfileMainController {

    @FXML private VBox newsContainer;
    @FXML private AnchorPane newsTemplate;

    public void initialize() {
        loadAdminNews();
    }

    private void loadAdminNews() {
        newsContainer.getChildren().clear();

        List<News> newsList;

        try {
            newsList = new NewsDAO().getAllNewsForAdmin();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        for (News n : newsList) {
            AnchorPane card = createAdminNewsCard(n);
            if (card != null) {
                newsContainer.getChildren().add(card);
            }
        }
    }

    private AnchorPane createAdminNewsCard(News news) {

        AnchorPane card = new AnchorPane();
        card.setPrefHeight(220);
        card.setStyle(newsTemplate.getStyle());

        Text org = new Text(news.getOrganization());
        org.setLayoutX(25);
        org.setLayoutY(45);
        org.setWrappingWidth(110);
        org.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        ImageView image = new ImageView();
        image.setLayoutX(160);
        image.setLayoutY(20);
        image.setOnMouseClicked(e -> {
            try {
                if (news.getSourceUrl() != null && !news.getSourceUrl().isBlank()) {
                    try {
                        String url = news.getSourceUrl().trim();

                        if (!url.startsWith("http")) {
                            url = "https://" + url;
                        }

                        java.awt.Desktop.getDesktop()
                                .browse(new java.net.URI(url));

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        image.setStyle("-fx-cursor: hand;");
        image.setFitWidth(260);
        image.setFitHeight(180);
        image.setPreserveRatio(false);
        image.setSmooth(true);
        image.setCache(true);

        Rectangle clip = new Rectangle();
        clip.setArcWidth(10);
        clip.setArcHeight(10);

        clip.widthProperty().bind(image.fitWidthProperty());
        clip.heightProperty().bind(image.fitHeightProperty());

        image.setClip(clip);

        if (news.getImagePath() != null && !news.getImagePath().isBlank()) {

            try {
                String path = news.getImagePath().trim();
                Image img = null;

                if (path.startsWith("http")) {
                    img = new Image(path, true);

                } else if (path.startsWith("/")) {
                    var res = getClass().getResource(path);
                    if (res != null) {
                        img = new Image(res.toExternalForm(), true);
                    } else {
                        // fallback to absolute file path
                        img = new Image(new java.io.File(path).toURI().toString(), true);
                    }

                } else {
                    // relative file path
                    java.io.File file = new java.io.File(path);
                    if (file.exists()) {
                        img = new Image(file.toURI().toString(), true);
                    }
                }

                if (img != null && !img.isError()) {
                    image.setImage(img);
                    applyObjectFitCover(image, img, 260, 180);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Text summary = new Text(news.getSummary());
        summary.setWrappingWidth(750);
        summary.setStyle("-fx-font-size: 16px;");

        Text author = new Text("By: " + news.getAuthor());
        author.setStyle("-fx-fill: #1a4a7c; -fx-font-weight: bold; -fx-cursor: hand;");

        author.setOnMouseClicked(e -> {
            try {
                if (news.getSourceUrl() != null && !news.getSourceUrl().isBlank()) {
                    String url = news.getSourceUrl().trim();
                    if (!url.startsWith("http")) {
                        url = "https://" + url;
                    }
                    java.awt.Desktop.getDesktop()
                            .browse(new java.net.URI(url));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        String formattedDate = "Unknown date";
        try {
            formattedDate = news.getDatePosted().format(
                    DateTimeFormatter.ofPattern("MMMM d, yyyy")
            );
        } catch (Exception ignored) {}

        try {
            LocalDateTime dt = news.getDatePosted();
            formattedDate = dt.format(
                    DateTimeFormatter.ofPattern("MMMM d, yyyy")
            );
        } catch (Exception ignored) {}

        Text date = new Text(formattedDate);
        date.setStyle("-fx-fill: #666; -fx-font-weight: bold;");

        String cats = (news.getCategories() == null || news.getCategories().isEmpty())
                ? "Uncategorized"
                : String.join(", ", news.getCategories());

        if (cats == null || cats.trim().isEmpty()) {
            cats = "Uncategorized";
        }

        Text categories = new Text(cats);
        categories.setStyle("-fx-fill: #666; -fx-font-weight: bold;");

        VBox info = new VBox(10,
                summary,
                author,
                new HBox(5,
                        new Text("Attached On:"), date,
                        new Text(" |  Category/s:"), categories
                )
        );
        info.setLayoutX(455);
        info.setLayoutY(40);

        ImageView editBtn = new ImageView();
        ImageView deleteBtn = new ImageView();

        editBtn.setImage(new Image(
                getClass().getResource("/com/example/energyconnectjavafx/Images/edit2.png").toExternalForm()
        ));

        deleteBtn.setImage(new Image(
                getClass().getResource("/com/example/energyconnectjavafx/Images/delete2.png").toExternalForm()
        ));

        editBtn.setFitHeight(18);
        editBtn.setFitWidth(18);
        editBtn.setPreserveRatio(true);
        editBtn.setStyle("-fx-cursor: hand;");

        deleteBtn.setFitHeight(18);
        deleteBtn.setFitWidth(18);
        deleteBtn.setPreserveRatio(true);
        deleteBtn.setStyle("-fx-cursor: hand;");

        editBtn.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                try {
                    FXMLLoader loader = new FXMLLoader(
                            getClass().getResource("/com/example/energyconnectjavafx/View/attachNews-view.fxml")
                    );

                    Parent root = loader.load();
                    AttachNewsController controller = loader.getController();

                    controller.loadForEdit(news.getId());

                    editBtn.getScene().setRoot(root);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteBtn.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {

                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                confirm.setTitle("Confirm Delete");
                confirm.setHeaderText("Delete News Article");
                confirm.setContentText("Are you sure you want to delete this news?");

                confirm.initOwner(deleteBtn.getScene().getWindow());
                confirm.showAndWait().ifPresent(result -> {
                    if (result == ButtonType.OK) {

                        boolean deleted = NewsDAO.deleteNews(news.getId());

                        if (deleted) {
                            loadAdminNews();
                        } else {
                            Alert err = new Alert(
                                    Alert.AlertType.ERROR,
                                    "You are not allowed to delete this news item."
                            );
                            err.initOwner(deleteBtn.getScene().getWindow());
                            err.showAndWait();
                        }
                    }
                });
            }
        });

        HBox actions = new HBox(15, editBtn, deleteBtn);
        AnchorPane.setTopAnchor(actions, 20.0);
        AnchorPane.setRightAnchor(actions, 25.0);

        card.getChildren().add(actions);

        card.getChildren().addAll(org, image, info);
        return card;
    }

    private void applyObjectFitCover(ImageView view, Image img, double boxW, double boxH) {
        double imgW = img.getWidth();
        double imgH = img.getHeight();

        if (imgW <= 0 || imgH <= 0) return;

        double scale = Math.max(boxW / imgW, boxH / imgH);

        double cropW = boxW / scale;
        double cropH = boxH / scale;

        double x = (imgW - cropW) / 2;
        double y = (imgH - cropH) / 2;

        view.setViewport(new javafx.geometry.Rectangle2D(x, y, cropW, cropH));
    }
}
