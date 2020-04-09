package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {

	private Scene simulationScreen, settingsScreen;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			simulationScreen = new Scene(root, 400, 400);
			simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(simulationScreen);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		//launch(args);
		SimulationSettings sim = new SimulationSettings();
		ArrayList<Location> locs = new ArrayList<Location>();
		locs = sim.populateLocations("src/application/gccLocationPoints.csv");

		for(int i = 0; i < locs.size(); i++) {
			System.out.println(locs.get(i).getName());
		}

	}
}
