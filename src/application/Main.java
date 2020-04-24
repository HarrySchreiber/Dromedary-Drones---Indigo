package application;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

public class Main extends Application{
	
	//global variables
	private Scene simulationScreen, settingsScreen; //scenes 
	private int weightPerOrder[] = new int[50]; //int ary for the weights we need to display
	private double sumPercent= 100; //a total 
	private String realFileContents = ""; //what we want to print to the file
	private static Document simulationSettingsXML;
	private static ArrayList<String> simulationSettingsIDs;
	private static String currentSimulationSettingID;
	private static ArrayList<Location> temporaryLocations;
	private static ArrayList<Meal> temporaryMeals;
	
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
				
				//TODO: Do we want to truncate this?
				knapsackAvgLabel.setText("Average Time: " + s.findAverage(s.getKnapsackData()));
				fifoAvgLabel.setText("Average Time: " + s.findAverage(s.getFifoData()));
				
				knapsackWrstLabel.setText("Worst Time: " + s.findWorst(s.getKnapsackData()));
				fifoWrstLabel.setText("Worst Time: " + s.findWorst(s.getFifoData()));
				
				//Clear the chart
				knapLineChart.getData().clear();
				XYChart.Series<Number, Number> knapSeries = new XYChart.Series<Number, Number>();
				//Fill the series with data
				for(Integer minute : s.getKnapsackData().keySet()) {
					knapSeries.getData().add(new XYChart.Data<Number, Number>(minute,s.getKnapsackData().get(minute)));
				}
				knapLineChart.getData().add(knapSeries);
				
				//Clear the chart
				fifoLineChart.getData().clear();
				XYChart.Series<Number, Number> fifoSeries = new XYChart.Series<Number, Number>();
				//Fill the series with data
				for(Integer minute : s.getFifoData().keySet()) {
					fifoSeries.getData().add(new XYChart.Data<Number, Number>(minute,s.getFifoData().get(minute)));
				}
				fifoLineChart.getData().add(fifoSeries);
				
				//TODO:Remove from testing
				System.out.println("FIFO: " + s.getFifoData() + " Average Time: " + s.findAverage(s.getFifoData()) + " Worst Time: " + s.findWorst(s.getFifoData()));
				System.out.println("Knapsack: " + s.getKnapsackData()  + " Average Time: " + s.findAverage(s.getKnapsackData()) + " Worst Time: " + s.findWorst(s.getKnapsackData()));
				
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
	 * Populates an ArrayList with simulation setting IDs from the simualtionSettings.xml
	 * @return ArrayList of simulation IDs
	 */
	public static ArrayList<String> getSimulationSettingsIDs() {
		ArrayList<String> ret = new ArrayList<String>();
		//Grab all of the documents with the simulationsetting tag name
		NodeList simulationSettingsList = simulationSettingsXML.getElementsByTagName("simulationsetting");
		for(int i = 0; i < simulationSettingsList.getLength(); i++) {
			//Grab each simulationsetting item
			Element currentSettings = (Element) simulationSettingsList.item(i);
			//Grab the id of that simulationsetting
			ret.add(currentSettings.getAttribute("id"));
		}
		return ret;
	}
	
	
	/**
	 * a SimulationSettings builder from XML Document
	 * @param id The ID string of the simulation we want to parse out
	 * @return A SimulationSettings object that is populated from the xml
	 */
	public static SimulationSettings buildSimulationSettingsFromXML(String id) {
		//Get a list of all of the simulation settings
		NodeList simulationsettingList = simulationSettingsXML.getElementsByTagName("simulationsetting");
		//Get the current simulationsetting we want to look at
		Element currentSimulationSettingElement = (Element) simulationsettingList.item(0);
		for(int i = 0; i < simulationsettingList.getLength(); i++) {
			Element otherSimulationSettingElement = (Element) simulationsettingList.item(i);
			if(otherSimulationSettingElement.hasAttribute("id")) {
				//Find the simulationsetting by id and then update the current simulation setting
				if(otherSimulationSettingElement.getAttribute("id").contentEquals(id)) {
					currentSimulationSettingElement = otherSimulationSettingElement;
				}
			}	
		}
		
		//Simulation Variables for later use in building the simulationsetting object
		String simulationName = "";
		String droneIDNumber = "";
		ArrayList<Location> locations = new ArrayList<Location>();
		ArrayList<Meal> meals = new ArrayList<Meal>();
		int hoursInShift = 0;
		int upperOrdersPerHour = 0;
		int lowerOrdersPerHour = 0;
		
		//Break up all of the elements in the list
		NodeList elementList = currentSimulationSettingElement.getChildNodes();
		//Loop through the elements
		for(int i = 0; i < elementList.getLength(); i++) {
			//Set each child to a node, and then check if its an element node
			Node n = elementList.item(i);
			if(n.getNodeType()==Node.ELEMENT_NODE) {
				//Turn the node into an element
				Element field = (Element) n;
				//If the element is the simulation name then we set it
				if(field.getTagName().equals("name")) {
					simulationName = field.getTextContent();
				}
				//If the element is the drone id then we set it
				if(field.getTagName().equals("droneID")) {
					droneIDNumber = field.getTextContent();
				}
				//If the element is the locations then we set them
				if(field.getTagName().equals("locations")) {
					//Make a list of all of the locations in the locations element 
					NodeList locationNodes = field.getElementsByTagName("location");
					for(int j = 0; j < locationNodes.getLength(); j++) {
						//Break each location into a list of nodes
						NodeList locInfo = locationNodes.item(j).getChildNodes();
						//Location variables to save for later
						String locName = "";
						int locX = 0;
						int locY = 0;
						for(int k = 0; k < locInfo.getLength(); k++) {
							//Add the location info into a node to check to make sure its an element
							Node locInfoFieldNode = locInfo.item(k);
							if(locInfoFieldNode.getNodeType()==Node.ELEMENT_NODE) {
								//Turn node into an element
								Element locInfoField = (Element) locInfoFieldNode;
								//If the element is the location name then we set it
								if(locInfoField.getTagName().equals("name")) {
									locName = locInfoField.getTextContent();
								}
								//If the element is the x coordinate then we set it
								if(locInfoField.getTagName().equals("xCoordinate")) {
									locX = Integer.valueOf(locInfoField.getTextContent());
								}
								//If the element is the y coordinate then we set it
								if(locInfoField.getTagName().equals("yCoordinate")) {
									locY = Integer.valueOf(locInfoField.getTextContent());
								}
							}
						}
						//Create location object from variables and then add those to the array
						Location curLocation = new Location(locName, locX, locY);
						locations.add(curLocation);
					}
				}
				//If the element is the meals then we set them
				if(field.getTagName().equals("meals")) {
					//Grab all of the meals
					NodeList mealNodes = field.getElementsByTagName("meal");
					for(int j = 0; j < mealNodes.getLength(); j++) {
						//Grab all of the elements of a meal
						NodeList mealInfo = mealNodes.item(j).getChildNodes();
						Meal meal = new Meal(0);	//Temporary meal to be updated
						for(int k = 0; k < mealInfo.getLength(); k++) {
							//Grab all the info about one meal and save as a node to make sure its an element node
							Node mealInfoNode = mealInfo.item(k);
							if(mealInfoNode.getNodeType()==Node.ELEMENT_NODE) {
								//Turn node into an element 
								Element mealInfoElement = (Element) mealInfoNode;
								//If its the probability then make a new meal with the probability attribute
								if(mealInfoElement.getTagName().equals("probability")) {
									meal = new Meal(Double.valueOf(mealInfoElement.getTextContent()));
								}
								//If its the food items themselves then add the food items
								if(mealInfoElement.getTagName().equals("fooditems")) {
									//Grab all of the food item elements
									NodeList foodItemsList = mealInfoElement.getElementsByTagName("fooditem");
									for(int l = 0; l < foodItemsList.getLength(); l++) {
										//If there is a burger then we add a burger
										if(foodItemsList.item(l).getTextContent().equals("burger")) {
											meal.addFoodItem(meal.burgers);
										}
										//If there is fries then we add fries
										if(foodItemsList.item(l).getTextContent().equals("fries")) {
											meal.addFoodItem(meal.fries);
										}
										//If there is a coke then we add the coke
										if(foodItemsList.item(l).getTextContent().equals("coke")) {
											meal.addFoodItem(meal.coke);
										}
									}
								}
							}
						}
						//Add the meal back to the arraylist
						meals.add(meal);
					}
				}
				//If the element is the hours per shift then we set it
				if(field.getTagName().equals("hourspershift")) {
					hoursInShift = Integer.valueOf(field.getTextContent());
				}
				//If the element is the upper bound of orders per hour then we set it
				if(field.getTagName().equals("ordersperhourupper")) {
					upperOrdersPerHour = Integer.valueOf(field.getTextContent());
				}
				//If the element is the lower bound of orders per hour then we set it
				if(field.getTagName().equals("ordersperhourlower")) {
					lowerOrdersPerHour = Integer.valueOf(field.getTextContent());
				}
			}
		}
		//Build and return the object from the set variables
		return new SimulationSettings(simulationName, droneIDNumber, locations, meals, hoursInShift, upperOrdersPerHour, lowerOrdersPerHour);
	}
	
	/**
	 * Helper method that populates the temporaryMeals ArrayList with the simulation that is given to it
	 * @param id The id String of the simulation setting that is desired
	 */
	public static void populateDynamicMealArray(String id){
		temporaryMeals = buildSimulationSettingsFromXML(id).getMeals();
	}
	
	/**
	 * Helper method that populates the temporaryLocations ArrayList with the simulation that is given to it
	 * @param id The id String of the simulation setting that is desired
	 */
	public static void populateDynamicLocationArray(String id){
		temporaryLocations = buildSimulationSettingsFromXML(id).getLocations();
	}
	
	/**
	 * Method to remove a simulation from the simulationsettings XML file
	 * @param id ID String of the simulation we want to remove
	 * @throws Exception If the default simulation has an attempt on its life then we throw an exception
	 */
	public static void removeSimulationSettingFromXML(String id) throws Exception {
		//If someone tries to remove or edit the default settings
		if(id.equals("1")) {
			throw new Exception("Cannot remove default simulation");
		}
		
		//Remove the id from the list used to parse grab simulation names
		simulationSettingsIDs.remove(id);
		
		//Loop through all of the simulations to find the one to remove
		NodeList simulationSettingsList = simulationSettingsXML.getElementsByTagName("simulationsetting");
		for(int i = 0; i < simulationSettingsList.getLength(); i++) {
			Element simulationSettingElement = (Element) simulationSettingsList.item(i);
			//If the id attribute exists
			if(simulationSettingElement.hasAttribute("id")) {
				//If the id is the id that needs to be removed
				if(simulationSettingElement.getAttribute("id").equals(id)) {
					simulationSettingElement.getParentNode().removeChild(simulationSettingElement);
				}
			}
		}
		//Run the updater to update and write the actual file
		updateSimulationSettingsXML();
	}
	
	/**
	 * Helper method to update and write out to the simulation settings xml
	 */
	public static void updateSimulationSettingsXML() {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(simulationSettingsXML);
			StreamResult streamResult = new StreamResult(new File("simulationSettings.xml"));
			
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");	//Indents the XML file instead of just listing the nodes out
			transformer.transform(domSource, streamResult);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * A method to write simulation settings to an xml
	 * @param id The id of the new simulation or existing simulation that has new data
	 * @param simulationSetting A SimulationSettings object that contains all of the data needed to be put into the XML
	 * @throws Exception If someone tries to edit the first document
	 */
	public static void simulationSettingToXML(String id, SimulationSettings simulationSetting) throws Exception {
		//Remove the old one if were editing a simulation
		removeSimulationSettingFromXML(id);
		
		//Get the root of the file
		Element root = (Element) simulationSettingsXML.getElementsByTagName("simulationsettings").item(0);
		
		//Make a new simulationsetting element and add its id to its attributes and add it to the root
		Element currentSimulationDocument = simulationSettingsXML.createElement("simulationsetting");
		currentSimulationDocument.setAttribute("id", id);
		root.appendChild(currentSimulationDocument);
		
		//Add a name element to the current simulationsetting
		Element simulationNameElement = simulationSettingsXML.createElement("name");
		simulationNameElement.setTextContent(simulationSetting.getName());
		currentSimulationDocument.appendChild(simulationNameElement);
		
		//Add a droneID element to the current simulationsetting
		Element droneIDElement = simulationSettingsXML.createElement("droneID");
		droneIDElement.setTextContent(simulationSetting.getCurrentDrone().getDroneID());
		currentSimulationDocument.appendChild(droneIDElement);
		
		//Add locations element to current simulation setting
		Element locationsElement = simulationSettingsXML.createElement("locations");
		currentSimulationDocument.appendChild(locationsElement);
		for(Location location : simulationSetting.getLocations()) {
			//Add a location element to the locations
			Element locationElement = simulationSettingsXML.createElement("location");
			locationsElement.appendChild(locationElement);
			
			//Give the location a name
			Element locationNameElement = simulationSettingsXML.createElement("name");
			locationNameElement.setTextContent(location.getName());
			locationElement.appendChild(locationNameElement);
			
			//Give the location an x coordinate
			Element xCoordinateElement = simulationSettingsXML.createElement("xCoordinate");
			xCoordinateElement.setTextContent(String.valueOf(location.getX()));
			locationElement.appendChild(xCoordinateElement);
			
			//Give the location a y coordinate
			Element yCoordinateElement = simulationSettingsXML.createElement("yCoordinate");
			yCoordinateElement.setTextContent(String.valueOf(location.getY()));
			locationElement.appendChild(yCoordinateElement);
		}
		
		//Add meals element to the XML
		Element mealsElement = simulationSettingsXML.createElement("meals");
		currentSimulationDocument.appendChild(mealsElement);
		for(Meal meal : simulationSetting.getMeals()) {
			//Add meal element to the meals
			Element mealElement = simulationSettingsXML.createElement("meal");
			mealsElement.appendChild(mealElement);
			
			//Add the meal probability
			Element probabilityElement = simulationSettingsXML.createElement("probability");
			probabilityElement.setTextContent(String.valueOf(meal.getProbability()));
			mealElement.appendChild(probabilityElement);
			
			//Add the food items element
			Element foodItemsElement = simulationSettingsXML.createElement("fooditems");
			mealElement.appendChild(foodItemsElement);
			
			for(FoodItem foodItem : meal.getFoodItems()) {
				//Add the food items to fooditems element
				Element foodItemElement = simulationSettingsXML.createElement("fooditem");
				foodItemElement.setTextContent(foodItem.getName());
				foodItemsElement.appendChild(foodItemElement);
			}
		}
		
		//Add a hours per shift element to the current simulationsetting
		Element hoursPerShiftElement = simulationSettingsXML.createElement("hourspershift");
		hoursPerShiftElement.setTextContent(String.valueOf(simulationSetting.getHoursPerShift()));
		currentSimulationDocument.appendChild(hoursPerShiftElement);
		
		//Add a upper orders per hour element to the current simulationsetting
		Element upperOrdersPerHourElement = simulationSettingsXML.createElement("ordersperhourupper");
		upperOrdersPerHourElement.setTextContent(String.valueOf(simulationSetting.getOrderUpper()));
		currentSimulationDocument.appendChild(upperOrdersPerHourElement);
		
		//Add a lower orders per hour element to the current simulationsetting
		Element lowerOrdersPerHourElement = simulationSettingsXML.createElement("ordersperhourlower");
		lowerOrdersPerHourElement.setTextContent(String.valueOf(simulationSetting.getOrderLower()));
		currentSimulationDocument.appendChild(lowerOrdersPerHourElement);
		
		//Run the updater to update and write the actual file
		updateSimulationSettingsXML();
	}
	
	/**
	 * Find the next available ID for the SimulationSettings
	 * @return The next available ID for a SimulationSettings
	 */
	public static String findAvailableSimulationSettingID() {
		int counter = 1;
		
		//Find a value that is not in the simulation already
		while(simulationSettingsIDs.contains(String.valueOf(counter))) {
			counter++;
		}
		
		return String.valueOf(counter);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			simulationSettingsXML = documentBuilder.parse("simulationSettings.xml");
			simulationSettingsIDs = getSimulationSettingsIDs();
			currentSimulationSettingID = "1";
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//launch(args);
	}
	
}
