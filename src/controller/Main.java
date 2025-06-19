package controller;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.startMenu.Controller;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		if(!displaySettingsCheck(primaryStage))
			return;
			
		// 1) Load the splash screen FXML
		Parent splashRoot = FXMLLoader.load(getClass().getResource("/view/splashScene/Scene.fxml"));
		// 🔸 Attach scene to stage and display
		Controller.initalizeStage(primaryStage);
		
		// Set Cursor
		Scene scene = new Scene(splashRoot);
		Image pointerImage = new Image(getClass().getResource("/view/assets/Mouse Cursor.png").toExternalForm());
		scene.setCursor(new ImageCursor(pointerImage, 5, 2));
		
		// Show Scene
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// Launch the application
		launch(args);
	}
	
	private boolean displaySettingsCheck(Stage primaryStage)
	{
		 // 1) Get the JavaFX "logical" screen bounds
        Screen screen = Screen.getPrimary();
        Rectangle2D logicalBounds = screen.getBounds();
        double logicalWidth  = logicalBounds.getWidth();
        double logicalHeight = logicalBounds.getHeight();

        // 2) Get the AWT transform scale (reflects true OS scaling)
        GraphicsDevice gd = GraphicsEnvironment
                              .getLocalGraphicsEnvironment()
                              .getDefaultScreenDevice();
        AffineTransform t = gd.getDefaultConfiguration().getDefaultTransform();
        double scaleX = t.getScaleX();  // e.g. 1.25 at 125%
        double scaleY = t.getScaleY();

        // 3) Recover the physical resolution
        int physicalWidth  = (int) Math.round(logicalWidth  * scaleX);
        int physicalHeight = (int) Math.round(logicalHeight * scaleY);

        // 4) Compute the scale percent for display
        int scalePercent = (int) Math.round(scaleX * 100);

        // 5) Check against desired 1920×1080 @ 100%
        boolean resOK   = (physicalWidth  == 1920 && physicalHeight == 1080);
        boolean scaleOK = (scalePercent    == 100);

        if (!resOK || !scaleOK) {
            String msg = String.format(
                "For the best experience, please switch your display to\n" +
                "1920×1080 resolution and 100%% scale (no DPI scaling).\n\n" +
                "Current: %d×%d @ %d%%",
                physicalWidth, physicalHeight, scalePercent
            );
            Alert alert = new Alert(AlertType.WARNING, msg);
            DialogPane pane = alert.getDialogPane();
            pane.setPrefWidth(600);
            //also ensure the text wraps nicely:
            pane.lookup(".content.label").setStyle("-fx-wrap-text: true;");
            pane.getStylesheets().add(getClass().getResource("/view/css/alert.css").toExternalForm());
            pane.getStyleClass().add("dialog-pane");
            
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/view/assests/scene/NeonErrorSymbol.png")));
            icon.setFitWidth(75);
            icon.setFitHeight(75);
            alert.setGraphic(icon);

            // **CHANGE the window icon**:
            Stage dialogStage = (Stage) pane.getScene().getWindow();
            dialogStage.getIcons().clear();  // optional: remove the default Java icon
            dialogStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/view/assets/Game Icon.png"))
            );
            
        
            alert.setHeaderText("Display Settings Check");
            alert.showAndWait();

            // exit so they can fix their settings
            Platform.exit();
            return false;
        }

        return true;
	}

}
