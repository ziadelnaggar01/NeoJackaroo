package view.aboutMenu;

import controller.GenericController;
import controller.SceneConfig;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class Controller {

	@FXML private ScrollPane aboutGameScrollPane;

	@FXML private TextFlow text;

	@FXML
	private void initialize() {
        // Let the ScrollPane force the TextFlow to match its viewport width:
        aboutGameScrollPane.setFitToWidth(true);

        // Apply padding inside the flow
        text.setPadding(new Insets(20));

        // Clear any placeholder content
        text.getChildren().clear();

        // Add your sections
        text.getChildren().add(createStyledText("📚 Game Background & Development Team\n\n", "neon-title"));
        text.getChildren().add(createStyledText("🧩 About NeoJackaroo\n", "neon-subtitle"));
        text.getChildren().add(createStyledText(
            "NeoJackaroo is a game developed as part of the CSEN401 course "
          + "in the Spring 2025 semester at the German University in Cairo, under the supervision of:\n\n",
            "neon-body"
        ));
        text.getChildren().add(createStyledText(
            "Prof. Dr. Slim Abdennadher\n"
          + "Assoc. Prof. Dr. Mervat Abuelkheir\n"
          + "Dr. Ahmed Abdelfattah\n\n",
            "neon-body"
        ));

        text.getChildren().add(createStyledText("👨‍💻 Developers\n", "neon-subtitle"));
        text.getChildren().add(createStyledText("Meet the team behind NeoJackaroo:\n\n", "neon-body"));
        text.getChildren().add(createStyledText(
            "Mohamed Tamer – 61-MET (GitHub: Mohamedtamer1265)\n\n"
          + "Seif Alaa Sedky – 61-Business Informatics (GitHub: Seif-Sedky)\n\n"
          + "Ziad Sameh Elnaggar – 61-Mechatronics (GitHub: ziadelnaggar01)\n\n",
            "neon-body"
        ));

        text.getChildren().add(createStyledText("🎮 A Reimagined Classic\n", "neon-subtitle"));
        text.getChildren().add(createStyledText(
            "Jackaroo is traditionally a multiplayer board game for 2–8 players in teams of two.\n"
          + "NeoJackaroo reimagines this concept as a single-player experience, where the player "
          + "competes against 3 intelligent CPU opponents.\n\n",
            "neon-body"
        ));
        text.getChildren().add(createStyledText("This is not just a digital recreation — it's a bold reinterpretation of the original game.\n\n", "neon-body"));

        text.getChildren().add(createStyledText("🆕 What’s New?:\n", "neon-subtitle"));
        text.getChildren().add(createStyledText(
            "✨ Custom cards and rules to bring new strategic depth\n\n"
          + "🧠 Innovative mechanics designed for solo play\n\n"
          + "🎲 A dynamic and exciting twist on a classic game — tailored for today's players\n",
            "neon-body"
        ));
	}

	
	private Text createStyledText(String content, String styleClass) {
        Text t = new Text(content);
        t.getStyleClass().add(styleClass);

        // Bind wrapping to the TextFlow's width minus its left/right insets
        t.wrappingWidthProperty().bind(
            text.widthProperty()
                     .subtract(text.getPadding().getLeft() + text.getPadding().getRight())
        );
        return t;
	}

	@FXML
	private void switchSceneToStartMenu(MouseEvent event) throws Exception {
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
}
