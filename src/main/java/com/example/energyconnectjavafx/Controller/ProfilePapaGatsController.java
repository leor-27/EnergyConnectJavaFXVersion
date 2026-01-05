package energyfm.energyconnect.energyconnectjavafxabout.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfilePapaGatsController extends energyfm.energyconnect.energyconnectjavafxabout.Controllers.ProfileMainController implements Initializable {

   public Text djNicknameText;
   public Text realNameText;
   public ImageView djPicture;
   public ImageView leftArrow;
   public ImageView rightArrow;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      if (djNicknameText != null) {
         djNicknameText.setText("PAPA GATS");
      }
      if (realNameText != null) {
         realNameText.setText("John Jordan Lanuzga");
      }

      if (leftArrow != null) {
         leftArrow.setOnMouseClicked(event -> {
            try {
               goToPreviousDJ(event);
            } catch (Exception e) {
               e.printStackTrace();
            }
         });
      }

      if (rightArrow != null) {
         rightArrow.setOnMouseClicked(event -> {
            try {
               goToNextDJ(event);
            } catch (Exception e) {
               e.printStackTrace();
            }
         });
      }

      if (djPicture != null) {
         try {
            Image image = new Image(getClass().getResourceAsStream("/images/papa_gats.png"));
            djPicture.setImage(image);
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
}