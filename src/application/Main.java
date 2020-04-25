package application;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.beans.binding.Bindings;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application{
	
	//global variables
	private Scene simulationScreen, settingsScreen; //scenes 
	private int weightPerOrder[] = new int[50]; //int ary for the weights we need to display
	private double sumPercent= 100; //a total 
	private String realFileContents = ""; //what we want to print to the file
	private Map<Integer,Integer> fifoData;
	private Map<Integer,Integer> knapsackData;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			//opens the file with the default values
			Scanner sc = new Scanner(new File("DefSimData.txt")); 
			StringBuffer buffer = new StringBuffer();
			
			//reads all the lines of the file to buffer
			while (sc.hasNextLine()) {
		         buffer.append(sc.nextLine()+System.lineSeparator());
		      }
		     
			 //saves all the old file into the global variables
		     realFileContents = buffer.toString();
		     String defaultFileContents = buffer.toString();
		     
		     //Not quite done yet
		     String[] defaultFileLines = defaultFileContents.split(System.getProperty("line.separator"));
		     
		     boolean knapB = false, fifoB = false, defDroneB = false;
		     double perUsedB = 0, perLeftB = 0;
		     int perCountsB [] = new int [50];
		     int burgerCountsB [] = new int [50];
		     int fryCountsB [] = new int [50];
		     int cokeCountsB [] = new int [50];
		     int hoursPerShiftB = 0, upperOrdersB = 0, lowerOrdersB = 0;
		     
		    for(int i = 0; i < defaultFileLines.length; i++) {
		    	String curLine = defaultFileLines[i];
		    	String [] splitVal = curLine.split(": ");
		    	for(int j = 1; j <= 4; j++) {
		    		
		    		if(curLine.contains("Order " + String.valueOf(j) + " Percentage:" )){
		    			perCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(perCountsB[j-1]);
		    		}
		    		else if (curLine.contains("Order " + String.valueOf(j) + " Burgers:" )){
		    			burgerCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(burgerCountsB[j-1]);
		    			
		    		}
		    		else if (curLine.contains("Order " + String.valueOf(j) + " Fries:") ){
		    			fryCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(fryCountsB[j-1]);
		    		}
		    		else if (curLine.contains("Order " + String.valueOf(j) + " Cokes:")){
		    			cokeCountsB[j-1] = Integer.valueOf(splitVal[1]);
		    			//System.out.println(cokeCountsB[j-1]);
		    		}
		    		
		    	}
		    	
		    	if(curLine.contains("Percentage Used:")) {
		    		perUsedB = Double.valueOf(splitVal[1]);
		    		//System.out.println(perUsedB);
		    	}
		    	else if (curLine.contains("Percentage Left:")) {
		    		perLeftB = Double.valueOf(splitVal[1]);
		    		//System.out.println(perLeftB);
		    	}
		    	else if (curLine.contains("Hours Per Shift:")) {
		    		hoursPerShiftB = Integer.valueOf(splitVal[1]);
		    		//System.out.println(hoursPerShiftB);
		    	}
		    	else if (curLine.contains("Upper Bound")) {
		    		upperOrdersB = Integer.valueOf(splitVal[1]);
		    		//System.out.println(upperOrdersB);
		    	}
		    	else if (curLine.contains("Lower Bound")) {
		    		lowerOrdersB = Integer.valueOf(splitVal[1]);
		    		//System.out.println(lowerOrdersB);
		    	}
		    	else if(curLine.contains("Knapsack Packing")){
		    		knapB = Boolean.valueOf(splitVal[1]);
		    		//System.out.println(knapB);
		    	}
		    	else if(curLine.contains("Fifo")) {
		    		fifoB = Boolean.valueOf(splitVal[1]);
		    		//System.out.println(fifoB);
		    	}
		    	else if(curLine.contains("Default Grove City Drone")){
		    		defDroneB = Boolean.valueOf(splitVal[1]);
		    		//System.out.println(defDroneB);
		    	}
		    	
		    }
		     

		     sc.close();
		     //FileWriter writer = new FileWriter("NewSimData.txt" ,false);
		     //writer.write("We writing this..");
		     
			
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
			
		     final NumberAxis xAxis = new NumberAxis(); // we are gonna plot against time
		     final NumberAxis yAxis = new NumberAxis();
		     xAxis.setLabel("Time Between Order and Delivery (min)");
		     xAxis.setAnimated(false); // axis animations are removed
	         yAxis.setLabel("Number of Orders Delivered");
		     yAxis.setAnimated(false); // axis animations are removed

		     //creating the line chart with two axis created above
		     final LineChart<Number, Number> fifoLineChart = new LineChart<>(xAxis, yAxis);
		     fifoLineChart.setTitle("FIFO Results");
		     fifoLineChart.setAnimated(false); // disable animations
			
			fifoResultsBox.getChildren().addAll(fifoLabel, fifoAvgLabel,fifoWrstLabel, fifoLineChart);
			
			//Knapsack results section
			VBox knapsackResultsBox = new VBox();
			knapsackResultsBox.setAlignment(Pos.TOP_LEFT);	//Left Align
			
			Label knapsackLabel = new Label("Knapsack");
			Label knapsackAvgLabel = new Label("Average Delivery Time: ");	//TODO: Add variable here
			Label knapsackWrstLabel = new Label("Worst Delivery Time: ");	//TODO: Add variable here
			
			//TODO: Add Knapsack results graph here
			final LineChart<Number, Number> knapLineChart = new LineChart<>(xAxis, yAxis);
		     knapLineChart.setTitle("Knapsack Results");
		     knapLineChart.setAnimated(false); // disable animations
			
			knapsackResultsBox.getChildren().addAll(knapsackLabel,knapsackAvgLabel,knapsackWrstLabel, knapLineChart);
			
			//Add results boxes to results grid
			resultsBox.add(fifoResultsBox, 0, 1);
			resultsBox.add(knapsackResultsBox, 1, 1);
			

			//Add box to the main grid in the second column stretching three rows if need be			
			simulationScreenLayout.add(resultsBox, 1, 0, 1, 3);
			
			
			//Edit Simulation Button on Simulation Screen
			Button runSimulationBtn = new Button("Run Simulation");
			runSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			runSimulationBtn.setOnAction(e -> {
				Simulation s = new Simulation();	//TODO: Make sure this is populated with the actual simulation settings from the radio buttons
				s.runSimulation();
				
				//Reduce the data down to just the summary data in the graph
				reduceKnapsackToMapFromArrayListOrders(s.getKnapsackData());
				reduceFifoToMapFromArrayListOrders(s.getFifoData());
				
				//TODO: Do we want to truncate this?
				knapsackAvgLabel.setText("Average Time: " + s.findAverage(knapsackData));
				fifoAvgLabel.setText("Average Time: " + s.findAverage(fifoData));
				
				knapsackWrstLabel.setText("Worst Time: " + s.findWorst(knapsackData));
				fifoWrstLabel.setText("Worst Time: " + s.findWorst(fifoData));
				
				//Clear the chart
				knapLineChart.getData().clear();
				XYChart.Series<Number, Number> knapSeries = new XYChart.Series<Number, Number>();
				//Fill the series with data
				for(Integer minute : knapsackData.keySet()) {
					knapSeries.getData().add(new XYChart.Data<Number, Number>(minute,knapsackData.get(minute)));
				}
				knapLineChart.getData().add(knapSeries);
				
				//Clear the chart
				fifoLineChart.getData().clear();
				XYChart.Series<Number, Number> fifoSeries = new XYChart.Series<Number, Number>();
				//Fill the series with data
				for(Integer minute : fifoData.keySet()) {
					fifoSeries.getData().add(new XYChart.Data<Number, Number>(minute,fifoData.get(minute)));
				}
				fifoLineChart.getData().add(fifoSeries);
				
				//TODO:Remove from testing
				System.out.println("FIFO: " + s.getFifoData() + " Average Time: " + s.findAverage(fifoData) + " Worst Time: " + s.findWorst(fifoData));
				System.out.println("Knapsack: " + s.getKnapsackData()  + " Average Time: " + s.findAverage(knapsackData) + " Worst Time: " + s.findWorst(knapsackData));
				
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
			String temp = simNameField.getText();
			
			//Event for changing the values to the new Settings Name
			EventHandler<ActionEvent> event = new EventHandler<ActionEvent>()  { 
	            public void handle(ActionEvent e)   { 
	            	String oldSimName = "Simulation Name: " + temp;
	            	String newSimName = "Simulation Name: " + simNameField.getText();
	            	realFileContents = realFileContents.replaceFirst(oldSimName, newSimName);
	            		      	      
	      
			}}; 
	        simNameField.setOnAction(event); //sets up the Event
	                		
			
			//VBox for things in column 1
			VBox columnOne = new VBox(20);
			columnOne.setAlignment(Pos.TOP_LEFT);
			
			//TODO: Use this to add things in column one
			
			Label schemeL = new Label ("Delivery Scheme: ");
			CheckBox schemeKCB = new CheckBox("Knapsack Packing");
			schemeKCB.setSelected(knapB);
			
			CheckBox schemeFCB = new CheckBox("FIFO - First in First out");
			schemeFCB.setSelected(fifoB);
			
			Boolean oneSchemeSelected = false;
			
			//Event handler for Knapsack Check Box and replaces the new status of Knapsack
			EventHandler<ActionEvent> knapEvent = new EventHandler<ActionEvent>() { 
                public void handle(ActionEvent e) { 
                	//TODO: Knap method
                	//So we know that only one method is selected.
                    if (schemeKCB.isSelected() && !(schemeFCB.isSelected())) {
                    	//System.out.println("knap" + " was selected");
                    	String oldKnap = "Knapsack Packing: false";
                    	String newKnap = "Knapsack Packing: true";
                    	
                    	//makes the replace here
                    	realFileContents = realFileContents.replaceFirst(oldKnap, newKnap);
                    	//KNAP method
                    }
                        
                    else {
                    	System.out.println("None or Two choices were selected");
                    }
                }
			};
            schemeKCB.setOnAction(knapEvent);//sets up the event here
            
          //Event handler for FIFO Check Box and replaces the new status of Fifo 
			EventHandler<ActionEvent> fifoEvent = new EventHandler<ActionEvent>() { 
                public void handle(ActionEvent e) { 
                	//TODO: FIFO method
                	//So we know that only one method is selected.
                    if (schemeFCB.isSelected() && !(schemeKCB.isSelected())) {
                    	//System.out.println("fifo" + " was selected");
                    	String oldFifo = "Fifo: false";
                    	String newFifo = "Fifo: true";
                    
                    	//makes the replace here
                    	realFileContents = realFileContents.replaceFirst(oldFifo, newFifo);
                    	// FIFO method here
                    }
                        
                    else {
                    	System.out.println("None or Two choices were selected");
                    }
                }
			};
			schemeFCB.setOnAction(fifoEvent);//sets up the event here
			

			
			Label dronesL = new Label ("Drones: ");
			CheckBox defaultDroneCB = new CheckBox("Default Grove City Drone");
			defaultDroneCB.setSelected(defDroneB);
			
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
			
			//add everything to column one
			columnOne.getChildren().addAll(schemeL, schemeKCB, schemeFCB, dronesL, defaultDroneCB);
			
			settingsScreenLayout.add(columnOne,0,1);
			
			//TODO: Add method for upload new campus map
			
			//TODO: Add method for add new drone
			
			//VBox for things in column 2
			VBox columnTwo = new VBox();
			columnTwo.setAlignment(Pos.TOP_LEFT);
			
        	//Slider sliderO1 = new Slider();
			Label perUsedL = new Label("Percentage Used: "  + String.valueOf(perUsedB));		
		    columnTwo.getChildren().add(perUsedL);
		    Label perLeftL = new Label("Percentage Left: " + String.valueOf(perLeftB));
		    columnTwo.getChildren().add(perLeftL);
			
			GridPane grid = new GridPane();
	        grid.setVgap(1);
	        grid.setHgap(1);
	        
	        //Names of the 4 orders that we are using
	        String [] orderNames = {"Order 1: ", "Order 2: ", "Order 3: ", "Order 4: "};
	       
	        
	        for(int i = 0; i < 4; i++){
	        	
	        	//variable dictionary for the for loop 
	        	int curLoopVal = i;
	        	Label order1L = new Label(orderNames[i]);
	        	Slider sliderO1 = new Slider();
	        	//Label order1L = new Label("Order 1");
	        	//Label order2L = new Label("Order 2");
	        	//Label order3L = new Label("Order 3");
	        	//Label order4L = new Label("Order 4");
	        	Label currentSliderVal = new Label("");
	        	Label burgerL = new Label("Burger");
	        	final Spinner<Integer> spinnerB = new Spinner<Integer>();
	        	Label burgerWeight = new Label("0 oz");
	        	Label friesL = new Label("Fries");
	        	final Spinner<Integer> spinnerF = new Spinner<Integer>();
	        	Label friesWeight = new Label("0 oz");
	        	Label cokeL = new Label("Coke");
	        	final Spinner<Integer> spinnerC = new Spinner<Integer>();
	        	Label cokeWeight = new Label("0 oz");
	        	Label totalWeightL = new Label("Total:");
	        	Label weightPerOrderL = new Label("0 oz");
	        	
	      

	        	//Spinner val that doesn't go up to 16 burgers (to not go over 96 oz)	        	
	        	SpinnerValueFactory<Integer> valueFactoryB = //
	        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 16, burgerCountsB[i]);
	        	spinnerB.setValueFactory(valueFactoryB);
	        	spinnerB.setMaxWidth(50);
	        	spinnerB.setEditable(true);

	        	//Spinner val that doesn't go up to 24 fries (to not go over 96 oz)
	        	SpinnerValueFactory<Integer> valueFactoryF = //
	        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 24, fryCountsB[i]);
	        	spinnerF.setValueFactory(valueFactoryF);
	        	spinnerF.setMaxWidth(50);
	        	spinnerF.setEditable(true);

	        	//Spinner val that doesn't go up to 6 fries (to not go over 96 oz)
	        	SpinnerValueFactory<Integer> valueFactoryC = //
	        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 6, cokeCountsB[i]);
	        	spinnerC.setValueFactory(valueFactoryC);
	        	spinnerC.setMaxWidth(50);
	        	spinnerC.setEditable(true);

	        		
	        	//slider for percentages
	        	sliderO1.setMin(0);
	        	sliderO1.setMax(100);
	        	sliderO1.setValue(perCountsB[i]);
	        	//sliderO1.setShowTickLabels(true);
	        	sliderO1.setShowTickMarks(true);
	        	sliderO1.setMajorTickUnit(50);
	        	sliderO1.setMaxWidth(300);
	        	
	        	//order probability percentages
	        	sliderO1.valueProperty().addListener((obs, oldVal, newVal)
	        			->{
	        				//difference we can add to the percentage label
	        				double difference = newVal.intValue() - oldVal.intValue() ;
	        				double  perLeftNum;
	        				if(sumPercent >= 100 || (100-sumPercent) < 0)
	        					perLeftNum = 0;
	        				else
	        					perLeftNum = 100 - sumPercent;
	        				
	        				//prints lables here
	        				saveNewPValue(difference);
	        				perUsedL.textProperty().bind(Bindings.format("%s %.0f %s", "Percentage Used: ", sumPercent, "%"));
	        				perLeftL.textProperty().bind(Bindings.format("%s %.0f %s", "Percentage Used: ", perLeftNum , "%"));
	        				
	        				//makes new and old strings so we can write to the txt file.
	        				String oldPerc = "Order " + String.valueOf(curLoopVal+1) + " Percentage: " + String.valueOf(oldVal.intValue());
	        				String newPerc = "Order " + String.valueOf(curLoopVal+1) + " Percentage: " + String.valueOf(newVal.intValue());
	        				
	        				//does the replacement here	
	        				realFileContents = realFileContents.replaceFirst(oldPerc, newPerc);
	        			});
	        	
	        	//sends label here
	        	totalWeightL.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
	        	
	        	//curent val of slider percentage
	        	currentSliderVal.textProperty().bind(Bindings.format("%.0f %s ", sliderO1.valueProperty(), "%"));
	        	
	        	
	        	//burger spinner per each order with a listener
	        	spinnerB.valueProperty().addListener((obs, oldVal, newVal) 
	        			->{
	        				if(oldVal > newVal) // so we can remove weight
	        					saveNewWValue(curLoopVal, -6);
	        				else //standard add
	        					saveNewWValue(curLoopVal, 6);
	        				
	        				//updates the burger label
	        				weightPerOrderL.textProperty().bind(Bindings.format("%d %s", weightPerOrder[curLoopVal], "oz"));
	        				burgerWeight.textProperty().bind(Bindings.format("%d %s", newVal * 6, "oz"));
	        				
	        				//makes new and old strings so we can write to the txt file
	        				String oldBurger = "Order " + String.valueOf(curLoopVal+1) + " Burgers: " + String.valueOf(oldVal);
	        				String newBurger = "Order " + String.valueOf(curLoopVal+1) + " Burgers: " + String.valueOf(newVal);

	        				//does the replacement here
	        				realFileContents = realFileContents.replaceFirst(oldBurger, newBurger);
	        				
	        			});
	        	
	        	//fries spinner per each order with a listener
	        	spinnerF.valueProperty().addListener((obs, oldVal, newVal) 
	        			->{
	        				if(oldVal > newVal)//so we remove weight
	        					saveNewWValue(curLoopVal, -4);
	        				else //standard add
	        					saveNewWValue(curLoopVal, 4);
	        				
	        				//updates fries labels
	        				weightPerOrderL.textProperty().bind(Bindings.format("%d %s", weightPerOrder[curLoopVal], "oz"));
	        				friesWeight.textProperty().bind(Bindings.format("%d %s", newVal * 4, "oz"));
	        				
	        				//makes new/old strings to write to the txt file
	        				String oldFries = "Order " + String.valueOf(curLoopVal+1) + " Fries: " + String.valueOf(oldVal);
	        				String newFries = "Order " + String.valueOf(curLoopVal+1) + " Fries: " + String.valueOf(newVal);

	        				//does the replacement here
	        				realFileContents = realFileContents.replaceFirst(oldFries, newFries);
	        			});
	        	
	        	//coke spinner per each order with a listener
	        	spinnerC.valueProperty().addListener((obs, oldVal, newVal) 
	        			-> {
	        				if(oldVal > newVal) //so we remove weight 
	        					saveNewWValue(curLoopVal, -14);
	        				else //standard add
	        					saveNewWValue(curLoopVal, 14);
	        				
	        				
	        				//updates the coke labels
	        				weightPerOrderL.textProperty().bind(Bindings.format("%d %s", weightPerOrder[curLoopVal], "oz"));
	        				cokeWeight.textProperty().bind(Bindings.format("%d %s", newVal * 14, "oz"));
	        				
	        				//makes new/old strings to write to the txt file
	        				String oldCoke = "Order " + String.valueOf(curLoopVal+1) + " Cokes: " + String.valueOf(oldVal);
	        				String newCoke = "Order " + String.valueOf(curLoopVal+1) + " Cokes: " + String.valueOf(newVal);

	        				//does the replacement here
	        				realFileContents = realFileContents.replaceFirst(oldCoke, newCoke);
	        			});
	        		        	


	        	//adds all labels and spinners to the grid pane here
	        	GridPane.setConstraints(order1L, 0, 1 + (5*i));
	        	grid.getChildren().add(order1L);
	        	GridPane.setConstraints(currentSliderVal, 2, 1 + (5*i));
	        	grid.getChildren().add(currentSliderVal);
	        	GridPane.setConstraints(sliderO1, 1, 1 + (5*i));
	        	grid.getChildren().add(sliderO1);
	        	GridPane.setConstraints(burgerL, 0, 2 + (5*i));
	        	grid.getChildren().add(burgerL);
	        	GridPane.setConstraints(spinnerB, 1, 2 + (5*i));
	        	grid.getChildren().add(spinnerB);
	        	GridPane.setConstraints(burgerWeight, 2, 2 + (5*i));
	        	grid.getChildren().add(burgerWeight);
	        	GridPane.setConstraints(friesL, 0, 3 + (5*i));
	        	grid.getChildren().add(friesL);
	        	GridPane.setConstraints(spinnerF, 1, 3 + (5*i));
	        	grid.getChildren().add(spinnerF);
	        	GridPane.setConstraints(friesWeight, 2, 3 + (5*i));
	        	grid.getChildren().add(friesWeight);
	        	GridPane.setConstraints(cokeL, 0, 4 + (5*i));
	        	grid.getChildren().add(cokeL);
	        	GridPane.setConstraints(spinnerC, 1, 4 + (5*i));
	        	grid.getChildren().add(spinnerC);
	        	GridPane.setConstraints(cokeWeight, 2, 4 + (5*i));
	        	grid.getChildren().add(cokeWeight);
	        	GridPane.setConstraints(totalWeightL, 0, 5 + (5*i));
	        	grid.getChildren().add(totalWeightL);
	        	GridPane.setConstraints(weightPerOrderL, 1, 5 + (5*i));
	        	grid.getChildren().add(weightPerOrderL);
	        	
	        	
	        	
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
			Label orderPerHourL = new Label("Orders Per Hour");
			
			Label upperHoursL = new Label("Upper Bound:");
			final Spinner<Integer> spinnerUpperHours = new Spinner<Integer>();
        	Label lowerHoursL = new Label("Lower Bound:");
        	
        	final Spinner<Integer> spinnerLowerHours = new Spinner<Integer>();
        	
			//Hours Per Shift can't got lower than 1 or higher than 8
        	SpinnerValueFactory<Integer> valueFactoryH = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, hoursPerShiftB);
        	spinnerHours.setValueFactory(valueFactoryH);
        	spinnerHours.setMaxWidth(70);
        	spinnerHours.setEditable(true);
        	
        	//spinner for the hours per shift
    		spinnerHours.valueProperty().addListener((obs, oldVal, newVal)
    				-> {
				
    					//makes new/old string we can write to the txt
    					String oldHours = "Hours Per Shift: "  + String.valueOf(oldVal);
    					String newHours = "Hours Per Shift: "  + String.valueOf(newVal);

    					//does the replacement here
    					realFileContents = realFileContents.replaceFirst(oldHours, newHours);
			});
        	
        	
    		//adds everything to grid pane 3
        	GridPane.setConstraints(hoursLabel, 0, 1);
        	gridC3.getChildren().add(hoursLabel);
        	GridPane.setConstraints(spinnerHours, 1, 1);
        	gridC3.getChildren().add(spinnerHours);
        	
        	
        	orderPerHourL.setAlignment(Pos.BASELINE_RIGHT);
        	GridPane.setConstraints(orderPerHourL, 0, 5);
        	gridC3.getChildren().add(orderPerHourL);

        	
        	
        	
        	//NOTE: spinner can't go higher than 30 orders but can change this 
        	SpinnerValueFactory<Integer> valueFactoryUH = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, upperOrdersB);
        	spinnerUpperHours.setValueFactory(valueFactoryUH);
        	spinnerUpperHours.setMaxWidth(70);
        	spinnerUpperHours.setEditable(true);
        	
        	//spinner for the upper limit of the orders per hour
        	spinnerUpperHours.valueProperty().addListener((obs, oldVal, newVal)
    				-> {
				
    					//makes new and old strings to write to the txt
    					String oldHours = "Upper Bound of Orders per Hour: "  + String.valueOf(oldVal);
    					String newHours = "Upper Bound of Orders per Hour: "  + String.valueOf(newVal);

    					//does the replacement
    					realFileContents = realFileContents.replaceFirst(oldHours, newHours);
			});
        
        	//spinner for the lower limits of thr orders per hour
        	//NOTE: spinner can't go higher than 30 orders but can change this         	
        	SpinnerValueFactory<Integer> valueFactoryLH = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, lowerOrdersB);
        	spinnerLowerHours.setValueFactory(valueFactoryLH);
        	spinnerLowerHours.setMaxWidth(70);
        	spinnerLowerHours.setEditable(true);
        	
        	spinnerLowerHours.valueProperty().addListener((obs, oldVal, newVal)
    				-> {
				
    					//makes new and old strings to write to the txt
    					String oldHours = "Lower Bound of Orders per Hour: "  + String.valueOf(oldVal);
    					String newHours = "Lower Bound of Orders per Hour: "  + String.valueOf(newVal);

    					//does the replacement
    					realFileContents = realFileContents.replaceFirst(oldHours, newHours);
			});	
        	
        	//adds all this stuff to grid pane 3 
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

			//listener so we can go back to the simulation screen and write to the file
			saveSimulationSetngsBtn.setOnAction(e  ->  {

						//try catch to the write to the file
						try(FileWriter writer = new FileWriter("NewSimData.txt" ,false)) {
							writer.write(realFileContents);
							writer.flush();
							writer.close();
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					primaryStage.setScene(simulationScreen); //saves the stage
				});	//Adds function to the button TODO: Expand function to not save if the user has not inputed correct values, possibly able to be done with throwing exceptions in a constructor
			
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
			simulationScreen = new Scene(simulationScreenLayout,1000,600);
			//TODO: Are we going to use this ever?
			//simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//TODO: Decide on default 
			settingsScreen = new Scene(settingsScreenLayout,1000,600);
			
			
			primaryStage.setScene(simulationScreen);
			//TODO: Decide if we want to be able to resize
			primaryStage.setResizable(false);
			primaryStage.setTitle("Dromedary Drones Simulation");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
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
	
	
	/*
	 * Simple helper method to change the element of the weightPerOrder array
	 * @param int elem, the number of the array that we want to add onto
	 * @param int plus the number that we will add onto
	 * 
	 */
	public void saveNewWValue(int elem, int plus) {
		//Checker to make sure the element exists
		if(weightPerOrder.length - 1 < elem) 
			throw new IndexOutOfBoundsException("Index " + elem + " is out of bounds!");
		
		else {//adds the parameter onto the int value 
			int temp = weightPerOrder[elem];
			weightPerOrder[elem] =  temp + plus;
		}
	}
	
	/*
	 * Simple helper that alters the sum of the percetanges of the sliders with the paramter
	 * @param a double that we want to want add onto sumPercent
	 */
	public void saveNewPValue(double plus) {
			sumPercent =  sumPercent + plus;	
	}
	
	/**
	 * @param orders Array of order data from the simulation run
	 */
	public void reduceKnapsackToMapFromArrayListOrders(ArrayList<Order> orders) {
		knapsackData = new HashMap<Integer,Integer>();
		
		//Find the difference in time and add it to the map data
		for(Order order : orders) {
			int minutesTaken = order.getTimeStampDelivered() - order.getTimeStampOrder();
			if(knapsackData.containsKey(minutesTaken)) {
				knapsackData.put(minutesTaken, knapsackData.get(minutesTaken) + 1);
			}else {
				knapsackData.put(minutesTaken, 1);
			}
		}
	}
	
	/**
	 * @param orders Array of order data from the simulation run
	 */
	public void reduceFifoToMapFromArrayListOrders(ArrayList<Order> orders) {
		fifoData = new HashMap<Integer,Integer>();
		
		//Find the difference in time and add it to the map data
		for(Order order : orders) {
			int minutesTaken = order.getTimeStampDelivered() - order.getTimeStampOrder();
			if(fifoData.containsKey(minutesTaken)) {
				fifoData.put(minutesTaken, fifoData.get(minutesTaken) + 1);
			}else {
				fifoData.put(minutesTaken, 1);
			}
		}
	}
	
	public void reduceKnapsackToMapFromDataFile() {
		
	}
	
	public void reduceFifoToMapFromDataFile() {
		
	}
}
