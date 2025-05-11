package view;

import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.image.ImageView;

public class StartMenu {
	public void startGame()
	{
		System.out.print("hi");
	}
	
	 	@FXML
	    private ImageView startButton;  // This should match the fx:id in Scene Builder

	    // Method for Mouse Entered event
	    @FXML
	    private void onMouseEntered() {
	        // Apply a red DropShadow effect on the image
	        DropShadow shadow = new DropShadow();
	        shadow.setColor(Color.RED);
	        shadow.setRadius(15);
	        shadow.setOffsetX(0);
	        shadow.setOffsetY(0);
	        startButton.setEffect(shadow);
	    }

	    // Method for Mouse Exited event
	    @FXML
	    private void onMouseExited() {
	        // Remove the effect when the mouse exits
	    	startButton.setEffect(null);
	    }
}
