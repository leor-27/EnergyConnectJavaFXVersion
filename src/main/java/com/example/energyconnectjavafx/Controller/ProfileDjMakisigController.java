package energyfm.energyconnect.energyconnectjavafxabout.Controllers;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileDjMakisigController extends ProfileMainController implements Initializable {

   public Text djNicknameText;
   public Text realNameText;
   public ImageView djPicture;
   public ImageView leftArrow;
   public ImageView rightArrow;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      if (djNicknameText != null) djNicknameText.setText("DJ MAKISIG");
      if (realNameText != null) realNameText.setText("Ruel Viñas");
      setupArrows();
      loadImage("/images/dj_makisig.png");
   }

   private void setupArrows() {
      if (leftArrow != null) leftArrow.setOnMouseClicked(event -> clickLeft(event));
      if (rightArrow != null) rightArrow.setOnMouseClicked(event -> clickRight(event));
   }

   private void clickLeft(javafx.scene.input.MouseEvent event) {
      try { goToPreviousDJ(event); } catch (Exception e) { e.printStackTrace(); }
   }

   private void clickRight(javafx.scene.input.MouseEvent event) {
      try { goToNextDJ(event); } catch (Exception e) { e.printStackTrace(); }
   }

   private void loadImage(String imagePath) {
      if (djPicture != null) {
         try { djPicture.setImage(new Image(getClass().getResourceAsStream(imagePath))); }
         catch (Exception e) { e.printStackTrace(); }
      }
   }
}