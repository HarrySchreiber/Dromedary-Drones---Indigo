package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;


public class Main extends Application {
	
	private Scene simulationScreen, settingsScreen;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Grid for Simulation Screen
			GridPane simulationScreenLayout = new GridPane();
			simulationScreenLayout.gridLinesVisibleProperty(); //TODO: Remove, for testing
			
			//Column Constraints for Simulation Screen
			ColumnConstraints c1 = new ColumnConstraints();
			c1.setPercentWidth(25);	//25% of the screen is used up for the side selection bar
			ColumnConstraints c2 = new ColumnConstraints();
			c2.setPercentWidth(75);	//75% of the screen is used for the results and data saving section
			simulationScreenLayout.getColumnConstraints().addAll(c1,c2);
			//Row Constraints for Simulation Screen
			RowConstraints r1 = new RowConstraints();
			r1.setPercentHeight(70);	//70% of the height is set up for the scroll view
			RowConstraints r2 = new RowConstraints();
			r2.setPercentHeight(10);	//10% of the height is set up for the run simulation button
			RowConstraints r3 = new RowConstraints();
			r3.setPercentHeight(10);	//10% of the height is set up for the edit simulation button
			RowConstraints r4 = new RowConstraints();
			r4.setPercentHeight(10);	//10% of the height is set up for the create new simulation button
			simulationScreenLayout.getRowConstraints().addAll(r1,r2,r3,r4);
			
			//Edit Simulation Button on Simulation Screen
			Button editSimulationBtn = new Button("Edit Simulation");
			editSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			editSimulationBtn.setOnAction(e -> primaryStage.setScene(settingsScreen));	//Adds function to the button TODO: Expand function to grab which simulation we need to edit from the radio buttons
			simulationScreenLayout.add(editSimulationBtn, 0, 2);	//Add the button to the screen
			
			//Grid for Settings Screen
			GridPane settingsScreenLayout = new GridPane();
			settingsScreenLayout.gridLinesVisibleProperty(); //TODO: Remove for testing
			
			//Save Simulation button on Simulation settings screen
			Button saveSimulationBtn = new Button("Save");
			saveSimulationBtn.setOnAction(e -> primaryStage.setScene(simulationScreen));	//Adds function to the button TODO: Expand function to not save if the user has not inputed correct values, possibly able to be done with throwing exceptions in a constructor
			settingsScreenLayout.add(saveSimulationBtn, 0, 0);	//Add the button to the screen
			
			//TODO: Decide on default
			simulationScreen = new Scene(simulationScreenLayout,800,500);
			//TODO: Are we going to use this ever?
			//simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//TODO: Decide on default 
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
