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
		text.getChildren().clear();
		text.setPadding(new Insets(20));

		text.getChildren().add(createStyledText("📚 Game Background & Development Team\n\n", "neon-title"));

		text.getChildren().add(createStyledText("🧩 About NeoJackaroo\n", "neon-subtitle"));
		text.getChildren().add(createStyledText("NeoJackaroo is a game developed as part of the CSEN401 course \nin the Spring 2025 semester at the German University in Cairo, under the supervision of:\n", "neon-body"));
		text.getChildren().add(createStyledText("\rProf. Dr. Slim Abdennadher\r\n"
				+ "\r\n"
				+ "Assoc. Prof. Dr. Mervat Abuelkheir\r\n"
				+ "\r\n"
				+ "Dr. Ahmed Abdelfattah\n\n", "neon-body"));

		text.getChildren().add(createStyledText("👨‍💻 Developers\n", "neon-subtitle"));
		text.getChildren().add(createStyledText("Meet the team behind NeoJackaroo:\n", "neon-body"));
		text.getChildren().add(createStyledText("\rMohamed Tamer – 61-MET\r\n"
				+ "(GitHub: Mohamedtamer1265)\r\n"
				+ "\r\n"
				+ "Seif Alaa Sedky – 61-Business Informatics\r\n"
				+ "(GitHub: Seif-Sedky)\r\n"
				+ "\r\n"
				+ "Ziad Sameh Elnaggar – 61-Mechatronics\r\n"
				+ "(GitHub: ziadelnaggar01)\n\n", "neon-body"));

		text.getChildren().add(createStyledText("🎮 A Reimagined Classic\n", "neon-subtitle"));
		text.getChildren().add(createStyledText("Jackaroo is traditionally a multiplayer board game for 2–8 \nplayers in teams of two.\r\n"
				+ "NeoJackaroo reimagines this concept as a single-player experience, where the player competes against 3 intelligent CPU opponents.\n", "neon-body"));
		text.getChildren().add(createStyledText("This is not just a digital recreation — it's a bold reinterpretation of the original game.\n\n", "neon-body"));

		text.getChildren().add(createStyledText("🆕 What’s New?:\n", "neon-subtitle"));
		text.getChildren().add(createStyledText("✨ Custom cards and rules to bring new strategic depth\r\n"
				+ "\r\n"
				+ "🧠 Innovative mechanics designed for solo play\r\n"
				+ "\r\n"
				+ "🎲 A dynamic and exciting twist on a classic game — tailored for today's players\n", "neon-body"));
		
		aboutGameScrollPane.getStyleClass().add("scroll-pane");
	}

	
	private Text createStyledText(String content, String styleClass) {
		Text text = new Text(content);
		text.setWrappingWidth(500);
		text.getStyleClass().add(styleClass);
		return text;
	}

	@FXML
	private void switchSceneToStartMenu(MouseEvent event) throws Exception {
		Parent root = SceneConfig.getInstance().getStartScene();
		GenericController.switchScene(event, root);
	}
}
