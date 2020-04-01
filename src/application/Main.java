package application;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;


public class Main extends Application{

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
			VBox columnOne = new VBox(20);
			columnOne.setAlignment(Pos.TOP_LEFT);
			
			//TODO: Use this to add things in column one
			
			Label schemeL = new Label ("Delivery Scheme: ");
			CheckBox schemeKCB = new CheckBox("Knapsack Packing");
			CheckBox schemeFCB = new CheckBox("FIFO - First in First out");
			Boolean oneSchemeSelected = false;
			
			//Event handler for Knapsack Check Box
			EventHandler<ActionEvent> knapEvent = new EventHandler<ActionEvent>() { 
                public void handle(ActionEvent e) { 
                	//TODO: Knap method
                	//So we know that only one method is selected.
                    if (schemeKCB.isSelected() && !(schemeFCB.isSelected())) {
                    	System.out.println("knap" + " was selected");
                    	//KNAP method
                    }
                        
                    else {
                    	System.out.println("None or Two choices were selected");
                    }
                }
			};
            schemeKCB.setOnAction(knapEvent);
            
          //Event handler for FIFO Check Box
			EventHandler<ActionEvent> fifoEvent = new EventHandler<ActionEvent>() { 
                public void handle(ActionEvent e) { 
                	//TODO: FIFO method
                	//So we know that only one method is selected.
                    if (schemeFCB.isSelected() && !(schemeKCB.isSelected())) {
                    	System.out.println("fifo" + " was selected");
                    	// FIFO method here
                    }
                        
                    else {
                    	System.out.println("None or Two choices were selected");
                    }
                }
			};
			schemeFCB.setOnAction(fifoEvent);
			

			Label dronesL = new Label ("Drones: ");
			CheckBox defaultDroneCB = new CheckBox("Default Grove City Drone");
			EventHandler<ActionEvent> defaultDroneEvent = new EventHandler<ActionEvent>() { 
                public void handle(ActionEvent e) { 
                	//TODO: FIFO method
                    if (defaultDroneCB.isSelected()) {
                    	//System.out.println("Default Drone" + " was selected");
                    }
                        
                    else {
                    }
                }
			};
			defaultDroneCB.setOnAction(defaultDroneEvent);
			
			
			columnOne.getChildren().addAll(schemeL, schemeKCB, schemeFCB, dronesL, defaultDroneCB);
			
			settingsScreenLayout.add(columnOne,0,1);
			
			//TODO: Add method for upload new campus map
			
			//TODO: Add method for add new drone
			
			//VBox for things in column 2
			VBox columnTwo = new VBox();
			columnTwo.setAlignment(Pos.TOP_LEFT);
			
			String perUsed1 = "Percentage Used";
        	//Slider sliderO1 = new Slider();
			double perSum = 100;
			double perLeft = 0;
			String perUsedStr = "Percentage Used: " + String.valueOf(perSum);
			//Label perUsedL = new Label("Percentage Used: "  + String.valueOf(perSum));
			Label perUsedL = new Label("");
		    columnTwo.getChildren().add(perUsedL);
		    Label perLeftL = new Label("Percentage Left: " + String.valueOf(perLeft));
		    columnTwo.getChildren().add(perLeftL);
			
			GridPane grid = new GridPane();
	        //grid.setPadding(new Insets(50, 10, 10, 10));
	        grid.setVgap(1);
	        grid.setHgap(1);
	        
	        
	        String [] orderNames = {"Order 1: ", "Order 2: ", "Order 3: ", "Order 4: "};
	        
	        for(int i = 0; i < 4; i++){
			
	        	Label order1L = new Label(orderNames[i]);
	        	//Label order1L = new Label("Order 1");
	        	//Label order2L = new Label("Order 2");
	        	//Label order3L = new Label("Order 3");
	        	//Label order4L = new Label("Order 4");
	        	Label currentSliderVal = new Label("");
	        	Label burgerL = new Label("Burger");
	        	Label burgerWeight = new Label("");
	        	Label friesL = new Label("Fries");
	        	Label friesWeight = new Label("");
	        	Label cokeL = new Label("Coke");
	        	Label cokeWeight = new Label("");
	        	
	        	//final int initialVal = 0;

	        	//Spinner val that doesn't go up to 16 burgers (to not go over 96 oz)
	        	final Spinner<Integer> spinnerB = new Spinner<Integer>();
	        	SpinnerValueFactory<Integer> valueFactoryB = //
	        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 16, 0);
	        	spinnerB.setValueFactory(valueFactoryB);
	        	spinnerB.setMaxWidth(50);
	        	spinnerB.setEditable(true);

	        	//Spinner val that doesn't go up to 24 fries (to not go over 96 oz)
	        	final Spinner<Integer> spinnerF = new Spinner<Integer>();
	        	SpinnerValueFactory<Integer> valueFactoryF = //
	        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, 0);
	        	spinnerF.setValueFactory(valueFactoryF);
	        	spinnerF.setMaxWidth(50);
	        	spinnerF.setEditable(true);

	        	//Spinner val that doesn't go up to 6 fries (to not go over 96 oz)
	        	final Spinner<Integer> spinnerC = new Spinner<Integer>();
	        	SpinnerValueFactory<Integer> valueFactoryC = //
	        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 6, 0);
	        	spinnerC.setValueFactory(valueFactoryC);
	        	spinnerC.setMaxWidth(50);
	        	spinnerC.setEditable(true);

	        		
	        	Slider sliderO1 = new Slider();
	        	sliderO1.setMin(0);
	        	sliderO1.setMax(100);
	        	sliderO1.setValue(25);
	        	//sliderO1.setShowTickLabels(true);
	        	sliderO1.setShowTickMarks(true);
	        	sliderO1.setMajorTickUnit(50);
	        	
	        	sliderO1.setMaxWidth(300);
	        	
	        	currentSliderVal.textProperty().bind(Bindings.format("%.0f %s ", sliderO1.valueProperty(), "%"));
	        	
	        	burgerWeight.textProperty().bind(Bindings.format("%d", spinnerB.valueProperty()));
	        	
	        	friesWeight.textProperty().bind(Bindings.format("%d", spinnerF.valueProperty()));
	        	
	        	cokeWeight.textProperty().bind(Bindings.format("%d", spinnerC.valueProperty()));
	        	
	        	
	        	


	        	GridPane.setConstraints(order1L, 0, 1 + (4*i));
	        	grid.getChildren().add(order1L);
	        	GridPane.setConstraints(currentSliderVal, 2, 1 + (4*i));
	        	grid.getChildren().add(currentSliderVal);
	        	GridPane.setConstraints(sliderO1, 1, 1 + (4*i));
	        	grid.getChildren().add(sliderO1);
	        	GridPane.setConstraints(burgerL, 0, 2 + (4*i));
	        	grid.getChildren().add(burgerL);
	        	GridPane.setConstraints(spinnerB, 1, 2 + (4*i));
	        	grid.getChildren().add(spinnerB);
	        	GridPane.setConstraints(burgerWeight, 2, 2 + (4*i));
	        	grid.getChildren().add(burgerWeight);
	        	GridPane.setConstraints(friesL, 0, 3 + (4*i));
	        	grid.getChildren().add(friesL);
	        	GridPane.setConstraints(spinnerF, 1, 3 + (4*i));
	        	grid.getChildren().add(spinnerF);
	        	GridPane.setConstraints(friesWeight, 2, 3 + (4*i));
	        	grid.getChildren().add(friesWeight);
	        	GridPane.setConstraints(cokeL, 0, 4 + (4*i));
	        	grid.getChildren().add(cokeL);
	        	GridPane.setConstraints(spinnerC, 1, 4 + (4*i));
	        	grid.getChildren().add(spinnerC);
	        	GridPane.setConstraints(cokeWeight, 2, 4 + (4*i));
	        	grid.getChildren().add(cokeWeight);
	        	
	        	//perSum += Double.valueOf(sliderO1.valueProperty().toString());
	        	//perSum += sliderO1.valueProperty().get();
	        	
	        	
	        
		}
	        	        
	        
			
			//TODO: Use this to add things in column two
			columnTwo.getChildren().addAll(grid);
			
			settingsScreenLayout.add(columnTwo,1,1);
			
			//VBox for things in column 3
			VBox columnThree = new VBox();
			
			GridPane gridC3= new GridPane(); 
			gridC3.setVgap(10);
			
			columnThree.setAlignment(Pos.TOP_LEFT);
			gridC3.setAlignment(Pos.CENTER_LEFT);
			
			/*
			 * Variable dictionary
			 */
			Label hoursLabel = new Label("Hours Per Shift:");
			final Spinner<Integer> spinnerHours = new Spinner<Integer>();
			Label orderPerHourL = new Label("Order Per House");
			
			Label upperHoursL = new Label("Upper Bound:");
			final Spinner<Integer> spinnerUpperHours = new Spinner<Integer>();
        	Label lowerHoursL = new Label("Lower Bound:");
        	
        	final Spinner<Integer> spinnerLowerHours = new Spinner<Integer>();
        	
			//Hours Per Shift can't got lower than 1 or higher than 8
        	SpinnerValueFactory<Integer> valueFactoryH = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, 4);
        	spinnerHours.setValueFactory(valueFactoryH);
        	spinnerHours.setMaxWidth(70);
        	spinnerHours.setEditable(true);
        	
        	
        	GridPane.setConstraints(hoursLabel, 0, 1);
        	gridC3.getChildren().add(hoursLabel);
        	GridPane.setConstraints(spinnerHours, 1, 1);
        	gridC3.getChildren().add(spinnerHours);
        	
        	
        	orderPerHourL.setAlignment(Pos.BASELINE_RIGHT);
        	GridPane.setConstraints(orderPerHourL, 0, 5);
        	gridC3.getChildren().add(orderPerHourL);

        	
        	
        	
        	//NOTE: spinner can't go higher than 30 orders but can change this 
        	SpinnerValueFactory<Integer> valueFactoryUH = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 22);
        	spinnerUpperHours.setValueFactory(valueFactoryUH);
        	spinnerUpperHours.setMaxWidth(70);
        	spinnerUpperHours.setEditable(true);
        	
        	
        	//NOTE: spinner can't go higher than 30 orders but can change this         	
        	SpinnerValueFactory<Integer> valueFactoryLH = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 15);
        	spinnerLowerHours.setValueFactory(valueFactoryLH);
        	spinnerLowerHours.setMaxWidth(70);
        	spinnerLowerHours.setEditable(true);
        	
        	GridPane.setConstraints(upperHoursL, 0, 8);
        	gridC3.getChildren().add(upperHoursL);
        	GridPane.setConstraints(spinnerUpperHours, 1, 8);
        	gridC3.getChildren().add(spinnerUpperHours);
        	
        	GridPane.setConstraints(lowerHoursL, 0, 10);
        	gridC3.getChildren().add(lowerHoursL);
        	GridPane.setConstraints(spinnerLowerHours, 1, 10);
        	gridC3.getChildren().add(spinnerLowerHours);
        	
			
			//TODO: Use this to add things in column three
			columnThree.getChildren().addAll(gridC3);
			
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
			simulationScreen = new Scene(simulationScreenLayout,800,600);
			//TODO: Are we going to use this ever?
			//simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//TODO: Decide on default 
			settingsScreen = new Scene(settingsScreenLayout,800,600);
			
			
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
