package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;


public class Main extends Application {
	
	private Scene simulationScreen, settingsScreen;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Grid for Simulation Screen
			GridPane simulationScreenLayout = new GridPane();
			simulationScreenLayout.gridLinesVisibleProperty(); //TODO: Remove, for testing
			
			Button editSimulationBtn = new Button("Edit Simulation");
			editSimulationBtn.setOnAction(e -> primaryStage.setScene(settingsScreen));
			
			simulationScreenLayout.add(editSimulationBtn, 0, 0);
			
			//Grid for Settings Screen
			GridPane settingsScreenLayout = new GridPane();
			settingsScreenLayout.gridLinesVisibleProperty(); //TODO: Remove for testing
			
			Button saveSimulationBtn = new Button("Save");
			saveSimulationBtn.setOnAction(e -> primaryStage.setScene(simulationScreen));
			
			settingsScreenLayout.add(saveSimulationBtn, 0, 0);
			
			//TODO: Decide on default
			simulationScreen = new Scene(simulationScreenLayout,800,500);
			//TODO: Are we going to use this ever?
			//simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			settingsScreen = new Scene(settingsScreenLayout,800,500);
			
			
			primaryStage.setScene(simulationScreen);
			//TODO: Decide if we want to be able to resize
			primaryStage.setResizable(false);
			primaryStage.setTitle("Dromedary Drones Simulation");
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
