package application;
	
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;


public class Main extends Application {
	
	private Scene simulationScreen, settingsScreen;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Simulation Screen Layout
			GridPane simulationScreenLayout = buildSimulationScreen();
			
			//TODO: Add Scroll panel and radio buttons
			
			//Section that holds results
			GridPane resultsBox = new GridPane();
			//Column Constraints for the results pane
			ColumnConstraints c1 = new ColumnConstraints();
			c1.setPercentWidth(50);
			ColumnConstraints c2 = new ColumnConstraints();
			c2.setPercentWidth(50);
			resultsBox.getColumnConstraints().addAll(c1,c2);
			//Row Constraints for the results pane
			RowConstraints r1 = new RowConstraints();
			r1.setPercentHeight(10);
			RowConstraints r2 = new RowConstraints();
			r2.setPercentHeight(90);
			resultsBox.getRowConstraints().addAll(r1,r2);
			
			//Box for the results title heading TODO: figure out how we want to place the box and how we want to format the text
			HBox resultsTextBox = new HBox();
			resultsTextBox.setAlignment(Pos.CENTER);	//Centers box elements
			
			Label resultsText = new Label("Results");
			resultsTextBox.getChildren().add(resultsText);
			
			//Add the box to the results grid and span 2 columns
			resultsBox.add(resultsTextBox, 0, 0, 2, 1);
			
			//FIFO results section
			VBox fifoResultsBox = new VBox();
			fifoResultsBox.setAlignment(Pos.TOP_LEFT);	//Left Align
			
			Label fifoLabel = new Label("FIFO");
			Label fifoAvgLabel = new Label("Average Delivery Time: ");	//TODO: Add variable Here
			Label fifoWrstLabel = new Label("Worst Deivery Time: ");	//TODO: Add variable Here
			
			//TODO: Add FIFO results graph here
			
			fifoResultsBox.getChildren().addAll(fifoLabel, fifoAvgLabel,fifoWrstLabel);
			
			//Knapsack results section
			VBox knapsackResultsBox = new VBox();
			knapsackResultsBox.setAlignment(Pos.TOP_LEFT);	//Left Align
			
			Label knapsackLabel = new Label("Knapsack");
			Label knapsackAvgLabel = new Label("Average Delivery Time: ");	//TODO: Add variable here
			Label knapsackWrstLabel = new Label("Worst Delivery Time: ");	//TODO: Add variable here
			
			//TODO: Add Knapsack results graph here
			
			knapsackResultsBox.getChildren().addAll(knapsackLabel,knapsackAvgLabel,knapsackWrstLabel);
			
			//Add results boxes to results grid
			resultsBox.add(fifoResultsBox, 0, 1);
			resultsBox.add(knapsackResultsBox, 1, 1);

			//Add box to the main grid in the second column stretching three rows if need be			
			simulationScreenLayout.add(resultsBox, 1, 0, 1, 3);
			
			
			//Edit Simulation Button on Simulation Screen
			Button runSimulationBtn = new Button("Run Simulation");
			runSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			runSimulationBtn.setOnAction(e -> {
				System.out.println("TODO: Run Simulation");
			}); //TODO: Add some logic to run the simulation
			simulationScreenLayout.add(runSimulationBtn, 0, 1);	//Add the button to the screen
			
			//Edit Simulation Button on Simulation Screen
			Button editSimulationBtn = new Button("Edit Simulation");
			editSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			editSimulationBtn.setOnAction(e -> primaryStage.setScene(settingsScreen));	//Adds function to the button TODO: Expand function to grab which simulation we need to edit from the radio buttons
			simulationScreenLayout.add(editSimulationBtn, 0, 2);	//Add the button to the screen
			
			//Create Simulation Button on Simulation Screen
			Button createSimulationBtn = new Button("Create New Simulation");
			createSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			createSimulationBtn.setOnAction(e -> primaryStage.setScene(settingsScreen));	//Adds function to the button TODO: Expand function to grab which simulation we need to edit from the radio buttons
			simulationScreenLayout.add(createSimulationBtn, 0, 3);	//Add the button to the screen
			
			//A Horizontal Stack Box to put buttons for save data and open data
			HBox dataButtonsBox = new HBox();
			dataButtonsBox.setAlignment(Pos.CENTER);	//Center the elements
			dataButtonsBox.setSpacing(100);	//Set the spacing of the buttons
			
			//Button for loading a data file
			Button loadDataFileBtn = new Button("Load a Results File");
			loadDataFileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			loadDataFileBtn.setOnAction(e ->{
				System.out.println("TODO: Load a Results File");	//TODO: Add the logic here
			});
			dataButtonsBox.getChildren().add(loadDataFileBtn);	//Add button to screen
			
			//Button for saving a data file
			Button saveDataFileBtn = new Button("Save Data File");
			saveDataFileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveDataFileBtn.setOnAction(e ->{
				System.out.println("TODO: Save a Results File");	//TODO: Add the logic here
			});
			dataButtonsBox.getChildren().add(saveDataFileBtn);	//Add button to screen
			
			//Add the box to the grid layout
			simulationScreenLayout.add(dataButtonsBox, 1, 3);
			
			
			
			//-----------------------------------------------------------------------------------
			
			
			
			//Settings Screen Layout
			GridPane settingsScreenLayout = buildSettingsScreen();
			
			//HBox for the naming components of the simulation settings
			HBox simNameBox = new HBox();
			simNameBox.setAlignment(Pos.CENTER);	//Center the box
			simNameBox.setSpacing(5);	//Space between elements is 5
			
			//Label and TextField for the simulation TODO: Formatting
			Label simNameLabel = new Label("Simulation Name:");
			TextField simNameField = new TextField();
			
			//Add Items to the HBox
			simNameBox.getChildren().addAll(simNameLabel,simNameField);
			//Add HBox to the grid and stretch it over 3 columns
			settingsScreenLayout.add(simNameBox, 0, 0, 3, 1);
			
			
			//VBox for things in column 1
			VBox columnOne = new VBox();
			columnOne.setAlignment(Pos.TOP_LEFT);
			
			//TODO: Use this to add things in column one
			//columnOne.getChildren().addAll();
			
			settingsScreenLayout.add(columnOne,0,1);
			
			//VBox for things in column 2
			VBox columnTwo = new VBox();
			columnTwo.setAlignment(Pos.TOP_LEFT);
			
			//TODO: Use this to add things in column two
			//columnTwo.getChildren().addAll();
			
			settingsScreenLayout.add(columnTwo,1,1);
			
			//VBox for things in column 3
			VBox columnThree = new VBox();
			columnThree.setAlignment(Pos.TOP_LEFT);
			
			//TODO: Use this to add things in column three
			//columnThree.getChildren().addAll();
			
			settingsScreenLayout.add(columnThree,2,1);
			
			//HBox for the save and Cancel Buttons
			HBox saveAndCancelButtonsBox = new HBox();
			saveAndCancelButtonsBox.setAlignment(Pos.CENTER);
			saveAndCancelButtonsBox.setSpacing(100);
			
			//Save Simulation Settings button on Simulation settings screen
			Button saveSimulationSetngsBtn = new Button("Save Settings");
			saveSimulationSetngsBtn.setOnAction(e -> primaryStage.setScene(simulationScreen));	//Adds function to the button TODO: Expand function to not save if the user has not inputed correct values, possibly able to be done with throwing exceptions in a constructor
			saveSimulationSetngsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveAndCancelButtonsBox.getChildren().add(saveSimulationSetngsBtn);
			
			//Cancel Simulation Settings button on Simulation settings screen
			Button cancelSimulationSetngsBtn = new Button("Cancel");
			cancelSimulationSetngsBtn.setOnAction(e -> primaryStage.setScene(simulationScreen));	//Adds function to the button TODO: Idk if there's anything else were gonna have to do here cause as long as we dont save anything we should be good
			cancelSimulationSetngsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveAndCancelButtonsBox.getChildren().add(cancelSimulationSetngsBtn);
			
			settingsScreenLayout.add(saveAndCancelButtonsBox,0,2,3,1);
			
			
			
			//------------------------------------------------------------------------------------------------
			
			
			
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
	
	/**
	 * A method to extract away some of the setup for the simulation screen
	 * @return The built simulation screen
	 */
	public static GridPane buildSimulationScreen() {
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
		
		return simulationScreenLayout;
	}
	
	/**
	 * A method to extract away some of the setup for the settings screen
	 * @return The built settings screen
	 */
	public static GridPane buildSettingsScreen() {
		//Grid for Settings Screen
		GridPane settingsScreenLayout = new GridPane();
		settingsScreenLayout.gridLinesVisibleProperty(); //TODO: Remove for testing
		
		//Column Constraints for Settings screen
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(34);	//Screen is divided in thirds for different sections
		ColumnConstraints c2 = new ColumnConstraints();
		c2.setPercentWidth(33);	//Screen is divided in thirds for different sections
		ColumnConstraints c3 = new ColumnConstraints();
		c3.setPercentWidth(33);	//Screen is divided in thirds for different sections
		settingsScreenLayout.getColumnConstraints().addAll(c1,c2,c3);
		//Row Constraints for Settings screen
		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(10);	//10% of the screen is used for adding a name to the simulation settings
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(80);	//80% of the screen is used for the content of the simulation settings
		RowConstraints r3 = new RowConstraints();
		r3.setPercentHeight(10);	//10% is used for the save and cancel buttons
		settingsScreenLayout.getRowConstraints().addAll(r1,r2,r3);
		
		return settingsScreenLayout;
	}
}
