package main;

import game.MainMenu;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	public void start(Stage stage) throws Exception {
		MainMenu menu = new MainMenu(stage);
		stage.setScene(menu.getScene());
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}