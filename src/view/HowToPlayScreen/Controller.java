package view.HowToPlayScreen;

import controller.GenericController;
import controller.SceneConfig;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class Controller {
	
	@FXML private ScrollPane howToPlayScrollPane;

	@FXML
	private void initialize() {
	    Text text = new Text("How to Play NeoJackaroo\r\n"
	    		+ "\r\n"
	    		+ "🎯 Objective:\r\n"
	    		+ "Move all 4 of your marbles from your Home Zone to your Safe Zone"
	    		+ "\r\n"
	    		+ "before your 3 CPU opponents do.\r\n"
	    		+ "\r\n"
	    		+ "🎲 Game Setup:\r\n"
	    		+ "You play against 3 CPU players, each with a unique color.\r\n"
	    		+ "\r\n"
	    		+ "Each player has 4 marbles that start in their Home Zone.\r\n"
	    		+ "\r\n"
	    		+ "The game board includes a 100-cell track, plus special zones and traps.\r\n"
	    		+ "\r\n"
	    		+ "🃏 Cards & Rounds:\r\n"
	    		+ "Each round, every player gets 4 cards from a special 102-card deck.\r\n"
	    		+ "\r\n"
	    		+ "Turns proceed clockwise, one card per player per turn.\r\n"
	    		+ "\r\n"
	    		+ "Cards let you move, field, swap, burn, discard, or save marbles.\r\n"
	    		+ "\r\n"
	    		+ "Unusable cards are discarded to the Fire Pit, and your turn is skipped.\r\n"
	    		+ "\r\n"
	    		+ "Your Turn:\r\n"
	    		+ "Select a card from your hand.\r\n"
	    		+ "\r\n"
	    		+ "Perform the associated action (e.g., move a marble X steps).\r\n"
	    		+ "\r\n"
	    		+ "Strategically plan around opponents and traps.\r\n"
	    		+ "\r\n"
	    		+ "🏁 Marble Movement:\r\n"
	    		+ "Marbles must be fielded from Home Zone to Base using an Ace or King.\r\n"
	    		+ "\r\n"
	    		+ "Marbles move clockwise along the track based on the card’s rank.\r\n"
	    		+ "\r\n"
	    		+ "You can’t pass or destroy your own marbles.\r\n"
	    		+ "\r\n"
	    		+ "Entering the Safe Zone requires an exact move count.\r\n"
	    		+ "\r\n"
	    		+ "💥 Special Cells:\r\n"
	    		+ "Trap Cells: Randomly change positions and destroy any marble"
	    		+ "\r\n"
	    		+ "landing on them.\r\n"
	    		+ "\r\n"
	    		+ "Base Cell: Starting cell for marbles; blocks other marbles from passing.\r\n"
	    		+ "\r\n"
	    		+ "Safe Zone Entry: Blocks marbles from entering if already occupied.\r\n"
	    		+ "\r\n"
	    		+ "💡 Strategic Actions:\r\n"
	    		+ "Use Jack to swap marbles with opponents.\r\n"
	    		+ "\r\n"
	    		+ "Use Burner Wild Card to send an enemy marble back home.\r\n"
	    		+ "\r\n"
	    		+ "Use Saver Wild Card to instantly rescue your own marble to a safe cell.\r\n"
	    		+ "\r\n"
	    		+ "Use Ten or Queen to discard an opponent’s card and skip their turn.\r\n"
	    		+ "\r\n"
	    		+ "🏆 Winning:\r\n"
	    		+ "Be the first to move all 4 marbles into your Safe Zone. "
	    		+ "\r\n"
	    		+ "That’s how you win NeoJackaroo!");
	    text.setWrappingWidth(500);
	    text.setStyle("-fx-fill: #22abdd; -fx-font-size: 23px;");

	    TextFlow flow = new TextFlow(text);
	    flow.setPadding(new Insets(20));
	    howToPlayScrollPane.setContent(flow);
	}
	
	@FXML
	private void switchSceneToStartMenu(MouseEvent event) throws Exception {
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
}
