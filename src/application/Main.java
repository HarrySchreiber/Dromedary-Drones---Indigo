package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
import javafx.geometry.Bounds;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import javafx.application.Application; 
import javafx.scene.Group; 
import javafx.scene.Scene; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  
import javafx.stage.Stage; 

import javafx.scene.shape.Circle; 

import javafx.scene.layout.StackPane;

public class Main extends Application{
	
	//global variables
	private static Scene simulationScreen; //scenes 
	private static Scene settingsScreen;
	private static Scene addDroneScreen;
	private static Scene addLocationScreen;
	private static int weightPerOrder[] = new int[50]; //int ary for the weights we need to display
	private static int percentageSum= 100; //a total 
	private static Document simulationSettingsXML;
	private static Document droneSettingsXML;
	private static ArrayList<String> simulationSettingsIDs;
	private static ArrayList<String> droneSettingsIDs;
	private static String currentSimulationSettingID;
	private static String currentDroneSettingID;
	private static ArrayList<Location> temporaryLocations;
	private static ArrayList<Meal> temporaryMeals;
	private static int hoursInShift;
	private static int upperOrdersPerHour;
	private static int lowerOrdersPerHour;
	private static String simulationName;
	private static GridPane simulationScreenLayout;
	private static GridPane settingsScreenLayout;
	private static ArrayList<Location> tempLocations = new ArrayList<Location>(); //edit as it goes and clear when finished
	private static ArrayList<Location> finalLocations = new ArrayList<Location>(); //final set of locations to use in sim
	private Map<Integer,Integer> fifoData;
	private Map<Integer,Integer> knapsackData;
	
	//global variables
	private static Scene  mapScreen, uploadMapScreen; //scenes 

	private double sumPercent= 100; //a total 
	private String realFileContents = ""; //what we want to print to the file
	private ArrayList<Location> locations = new ArrayList<Location>(); //edit as it goes and clear when finished
	private SimulationSettings currentSettings = new SimulationSettings();
	Map<Integer, Integer> knapData;
	double fifoAverage;
	double knapAverage;
	double fifoWorst;
	double knapWorst;
	String fifoText;
	String knapText;
	ArrayList<Integer> CSV = new ArrayList<Integer>();

	static ArrayList<Location> groveCityLocations = new ArrayList<Location>();

	Image mapImage = null;
	double realHeight, realWidth, imageHeight, imageWidth;
	static ArrayList<Location> clickedLocations = new ArrayList<Location>();
	double homeX, homeY, currentX, currentY;

	private static String readLocationsFrom = "UNCHANGED";
	private static String chosenAlgorithm = "UNCHANGED";
	
	@Override
	public void start(Stage primaryStage) {
		try {
			SimulationSettings baseToCallFunctions = new SimulationSettings();
			groveCityLocations = baseToCallFunctions.populateLocations("src/application/gccLocationPoints.csv");
			//opens the file with the default values
			Scanner sc = new Scanner(new File("NewSimData.txt")); 
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
		     
			//change the 4 later
		    //But gets the weight so we can use at the beginning...
		   for(int i  = 0; i < 4; i++) {
			   weightPerOrder[i] += (burgerCountsB[i] * 6);
			   weightPerOrder[i] += (fryCountsB[i] * 4);
			   weightPerOrder[i] += (cokeCountsB[i] * 14);
		   }

					
			//Simulation Screen Layout
			simulationScreenLayout = buildSimulationScreen();
			
			
			//Scroll Pane for Radio Buttons Section
			ScrollPane simulationSelectorPane = new ScrollPane();
			
			//Sets which group of radio buttons this simulation is a part of
			ToggleGroup simulationSelectorButtons = new ToggleGroup();
			//Make and populate the radio buttons
			VBox simulationSelectorVBox = new VBox();
			for(String idNumber : simulationSettingsIDs) {
				//Make a radio button with the name of the SimulationSetting
				RadioButton radioButton = new RadioButton(getSimulationNameFromID(idNumber));
				//Upon clicking on a radio button the currentSimulationID is set to the current ID
				radioButton.setOnAction(e->{
					currentSimulationSettingID = idNumber;
				});
				//Start the simulation with the default settings
				if(idNumber.equals(currentSimulationSettingID)) {
					radioButton.setSelected(true);
				}
				//Add radio button to toggle group and add it to the screen
				radioButton.setToggleGroup(simulationSelectorButtons);
				simulationSelectorVBox.getChildren().add(radioButton);
			}
			//Add the content to the screen
			simulationSelectorPane.setContent(simulationSelectorVBox);
			simulationScreenLayout.add(simulationSelectorPane, 0, 0);
			
			
			
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
			
		     final NumberAxis xAxisFifo = new NumberAxis(); // we are gonna plot against time
		     final NumberAxis yAxisFifo = new NumberAxis();
		     xAxisFifo.setLabel("Time Between Order and Delivery (min)");
		     xAxisFifo.setAnimated(false); // axis animations are removed
	         yAxisFifo.setLabel("Number of Orders Delivered");
		     yAxisFifo.setAnimated(false); // axis animations are removed

		     //creating the line chart with two axis created above
		     final LineChart<Number, Number> fifoLineChart = new LineChart<>(xAxisFifo, yAxisFifo);
		     fifoLineChart.setTitle("FIFO Results");
		     fifoLineChart.setAnimated(false); // disable animations
		     
		     final NumberAxis xAxisKnapsack = new NumberAxis(); // we are gonna plot against time
		     final NumberAxis yAxisKnapsack = new NumberAxis();
		     xAxisKnapsack.setLabel("Time Between Order and Delivery (min)");
		     xAxisKnapsack.setAnimated(false); // axis animations are removed
		     yAxisKnapsack.setLabel("Number of Orders Delivered");
		     yAxisKnapsack.setAnimated(false); // axis animations are removed

			
			fifoResultsBox.getChildren().addAll(fifoLabel, fifoAvgLabel,fifoWrstLabel, fifoLineChart);
			
			//Knapsack results section
			VBox knapsackResultsBox = new VBox();
			knapsackResultsBox.setAlignment(Pos.TOP_LEFT);	//Left Align
			
			Label knapsackLabel = new Label("Knapsack");
			Label knapsackAvgLabel = new Label("Average Delivery Time: ");	//TODO: Add variable here
			Label knapsackWrstLabel = new Label("Worst Delivery Time: ");	//TODO: Add variable here
			
			//TODO: Add Knapsack results graph here
			final LineChart<Number, Number> knapLineChart = new LineChart<>(xAxisKnapsack, yAxisKnapsack);
		     knapLineChart.setTitle("Knapsack Results");
		     knapLineChart.setAnimated(false); // disable animations
			
			knapsackResultsBox.getChildren().addAll(knapsackLabel,knapsackAvgLabel,knapsackWrstLabel, knapLineChart);
			
			//Add results boxes to results grid
			resultsBox.add(fifoResultsBox, 0, 1);
			resultsBox.add(knapsackResultsBox, 1, 1);
			

			//Add box to the main grid in the second column stretching three rows if need be			
			simulationScreenLayout.add(resultsBox, 1, 0, 1, 3);
			
			Simulation s = new Simulation();
			//Edit Simulation Button on Simulation Screen
			Button runSimulationBtn = new Button("Run Simulation");
			runSimulationBtn.setId("allbuttons");
			runSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			runSimulationBtn.setOnAction(e -> {
				s.setAlgorithm(chosenAlgorithm);
				SimulationSettings newSimulation = buildSimulationSettingsFromXML(currentSimulationSettingID);
				if (readLocationsFrom.contains("Map")) {
					newSimulation.setLocations(clickedLocations);
					//s.runSimulation(clickedLocations);
				}
				else if (readLocationsFrom.contains("File")) {
					newSimulation.setLocations(finalLocations);
					//s.runSimulation(finalLocations);
				}
				else {
					newSimulation.setLocations(groveCityLocations);
					//s.runSimulation(groveCityLocations);
				}

				s.runSimulation(newSimulation , buildDroneFromXML(currentDroneSettingID));
				
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
				
				System.out.println(fifoData);
				System.out.println(knapsackData);
				//TODO:Remove from testing
				System.out.println("FIFO: " + s.getFifoData() + " Average Time: " + s.findAverage(fifoData) + " Worst Time: " + s.findWorst(fifoData));
				System.out.println("Knapsack: " + s.getKnapsackData()  + " Average Time: " + s.findAverage(knapsackData) + " Worst Time: " + s.findWorst(knapsackData));
			}); //TODO: Add some logic to run the simulation
			simulationScreenLayout.add(runSimulationBtn, 0, 1);	//Add the button to the screen
			
			//Edit Simulation Button on Simulation Screen
			Button editSimulationBtn = new Button("Edit Simulation");
			editSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			editSimulationBtn.setOnAction(e -> {
				populatedSettingsScreen(currentSimulationSettingID, primaryStage);
				
				//removes the radio buttons so we can update them
				simulationScreenLayout.getChildren().remove(simulationSelectorPane);
				simulationScreen.setRoot(simulationScreenLayout);
				primaryStage.setScene(settingsScreen);
				
			});	//Adds function to the button TODO: Expand function to grab which simulation we need to edit from the radio buttons
			simulationScreenLayout.add(editSimulationBtn, 0, 2);	//Add the button to the screen
			
			//Create Simulation Button on Simulation Screen
			Button createSimulationBtn = new Button("Create New Simulation");
			createSimulationBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); //Fit button to fill grid box
			createSimulationBtn.setOnAction(e -> {
				populatedSettingsScreen("1", primaryStage);
				
				//removes the radio buttons so we can update them
				simulationScreenLayout.getChildren().remove(simulationSelectorPane);
				simulationScreen.setRoot(simulationScreenLayout);
				
				primaryStage.setScene(settingsScreen);
			});	//Adds function to the button TODO: Expand function to grab which simulation we need to edit from the radio buttons
			simulationScreenLayout.add(createSimulationBtn, 0, 3);	//Add the button to the screen
			
			//A Horizontal Stack Box to put buttons for save data and open data
			HBox dataButtonsBox = new HBox();
			dataButtonsBox.setAlignment(Pos.CENTER);	//Center the elements
			dataButtonsBox.setSpacing(100);	//Set the spacing of the buttons
			
			//Button for loading a data file
			Button loadDataFileBtn = new Button("Load a Results File");
			loadDataFileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			loadDataFileBtn.setOnAction(e ->{
				knapsackData = new HashMap<Integer, Integer>();
				fifoData = new HashMap<Integer,Integer>();
				FileChooser fileSelect = new FileChooser();
				fileSelect.setTitle("Load Data File");
				fileSelect.getExtensionFilters().addAll(
						new ExtensionFilter("CSV Files", "*.csv"));
				File selectedFile = fileSelect.showOpenDialog(primaryStage);
				if(selectedFile != null) {
					System.out.println("Selected File: " + selectedFile.getAbsolutePath());
					String csvFile = selectedFile.getAbsolutePath();
					
					try {
						Scanner lineScanner = new Scanner(new File(csvFile));
						//read file line by line
						int timeDelivered = 0;
						int timeOrdered = 0;
						int minutesTaken = 0;
						String simType = "";
						String line = "";
						Scanner scanner = null;
						int index = 0;
						if(lineScanner.hasNextLine()) {
							lineScanner.nextLine();	//will read and skip the first line
						}
						while(lineScanner.hasNextLine()) {

							scanner = new Scanner(lineScanner.nextLine());	
							scanner.useDelimiter(",");
							while(scanner.hasNext()) {
								String data = scanner.next();
								if(index == 0) {
									simType = data;
									//System.out.println(simType);
								}
								else if(index == 5) {
									//timeOrdered = Integer.parseInt(data);
									timeOrdered = Integer.parseInt(data);
								}
								else if(index == 7) {
									//timeDelivered = Integer.parseInt(data);
									timeDelivered = Integer.parseInt(data);
								}
								//System.out.println("Data " + data);
								index++;
							}index = 0;

							
							if(simType.equals("FIFO")){
								if(fifoData.containsKey(timeDelivered-timeOrdered)) {
									fifoData.put((timeDelivered-timeOrdered), fifoData.get(timeDelivered-timeOrdered)+1);
								}else {
									fifoData.put((timeDelivered-timeOrdered), 1);
								}
								
							}
							else if(simType.equals("Knapsack")) {
								if(knapsackData.containsKey(timeDelivered-timeOrdered)) {
									knapsackData.put((timeDelivered-timeOrdered), knapsackData.get(timeDelivered-timeOrdered)+1);
								}else {
									knapsackData.put((timeDelivered-timeOrdered), 1);
								}
							}
							else {
								System.out.println("Simulation Type not valid");
							}
						}
						//close reader
						scanner.close();
						lineScanner.close();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						System.out.println("File Not Found");
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				//loadDataFileBtn.setText("CurrentFile: " + selectedFile.getName());
				
				//TODO: Do we want to truncate this?
				//System.out.println(knapsackData);
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
			});
			
			//Button for saving a data file
			Button saveDataFileBtn = new Button("Save Data File");
			saveDataFileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveDataFileBtn.setOnAction(e ->{
				FileChooser fileLocation = new FileChooser();
				
				//Set extension filter for text files
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
				fileLocation.getExtensionFilters().add(extFilter);
				
				//Show save file dialog
				File selectedLocation = fileLocation.showSaveDialog(primaryStage);
				
				if( selectedLocation != null) {

					try {
						PrintWriter writer;
						writer = new PrintWriter(selectedLocation);
						writer.println("Method , Simulation Number ,, Chance ,, Minute Ordered ,, Minute Delivered ,, Location ,, Items in a meal");
						for(Order order : s.getFifoData()) {
							writer.print("FIFO,");
							writer.print(order.getSimulationNumber()+ ",");
							writer.print("," + order.getMeal().getProbability() +",");
							writer.print("," + order.getTimeStampOrder() + ",");
							writer.print("," + order.getTimeStampDelivered() + ",");
							writer.print("," + order.getDeliveryPoint() + ",");
							writer.print("," + order.getMeal() + ",");
							
							writer.println();
							
						}
						for(Order knap : s.getKnapsackData()) {
							writer.print("Knapsack,");
							writer.print(knap.getSimulationNumber() + ",");
							writer.print("," + knap.getMeal().getProbability() +",");
							writer.print("," + knap.getTimeStampOrder() + ",");
							writer.print("," + knap.getTimeStampDelivered() + ",");
							writer.print("," + knap.getDeliveryPoint() + ",");
							writer.print("," + knap.getMeal() + ",");
							
							writer.println();
							
						}
						writer.close();
						//TODO Delete this print statement
						System.out.println("Successfully wrote to the file");

					} catch (IOException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
						System.out.println("File could not be accessed");
					}
				}	//TODO: Add the logic here
			});
			dataButtonsBox.getChildren().add(saveDataFileBtn);	//Add button to screen
			dataButtonsBox.getChildren().add(loadDataFileBtn);	//Add button to screen
			
			//Add the box to the grid layout
			simulationScreenLayout.add(dataButtonsBox, 1, 3);
			
			
			
			//-----------------------------------------------------------------------------------

			
			//------------------------------------------------------------------------------------------------

			
			
			GridPane mapScreenLayout = new GridPane();
			Group deliveryPointsGroup = new Group();
			//StackPane mapScreenLayoutTotal = new StackPane();
			mapScreenLayout.setAlignment(Pos.CENTER);
			mapScreenLayout.setHgap(10);
			mapScreenLayout.setVgap(10);

			Label uploadMapPrompt = new Label("If you are not able to view a map, upload one.");
			Button uploadMapButton = new Button("Upload Map");

			Button cancelButton = new Button("Cancel");

			Button saveButton = new Button("Save");

			saveButton.setOnAction(e -> {
				primaryStage.setScene(settingsScreen);
			});

			cancelButton.setOnAction(e -> {
				clickedLocations.clear();
				primaryStage.setScene(settingsScreen);
			});

			uploadMapButton.setOnAction(e -> {
				primaryStage.setScene(uploadMapScreen);
			});

			if (mapImage == null) {
				mapScreenLayout.add(uploadMapPrompt,0,4);
				mapScreenLayout.add(uploadMapButton,1,4);
				mapScreenLayout.add(cancelButton, 0, 5);

			}
			else {
				ImageView image = new ImageView(mapImage);
				image.setX(0);
				image.setY(0);

				//setting the fit height and width of the image view 
				image.setFitHeight(500); 
				image.setFitWidth(300); 
				
				//Setting the preserve ratio of the image view 
				image.setPreserveRatio(true);  

				mapScreenLayout.add(image, 0, 0);
			}



			GridPane uploadMapScreenLayout = new GridPane();
			uploadMapScreenLayout.setAlignment(Pos.CENTER);
			uploadMapScreenLayout.setHgap(10);
			uploadMapScreenLayout.setVgap(10);

			Label fileChosenLabel = new Label("No File Chosen");
			Button chooseMapButton = new Button("Choose file");

			Button saveMapButton = new Button("Save Map");

			Label realImageWidthPrompt = new Label("Please enter the real width of the image in feet: ");
			Label realImageHeightPrompt = new Label("Please enter the real height of the image in feet: ");
			TextField realImageWidth = new TextField();
			TextField realImageHeight = new TextField();

			Label promptForName = new Label("Enter name for location before clicking: ");
			TextField pointName = new TextField();
			Label clickedLocationsList = new Label();

			chooseMapButton.setOnAction(e -> {
				FileChooser fileChooser = new FileChooser();
				File selectedFile = fileChooser.showOpenDialog(primaryStage);
				fileChosenLabel.setText("Current File: " + selectedFile.getName());
				try {
					FileInputStream inputFile = new FileInputStream(selectedFile.getAbsolutePath());
					mapImage = new Image(inputFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});

			saveMapButton.setOnAction(e-> {

				mapScreenLayout.add(promptForName, 10, 0);
				mapScreenLayout.add(pointName, 10, 1);
				mapScreenLayout.add(clickedLocationsList, 10, 2);
				uploadMapPrompt.setText("Change Map: ");
				mapScreenLayout.add(saveButton, 2, 5);

				primaryStage.setScene(mapScreen);

				if (mapImage == null) {
					//THROW AN ERROR
				}
				else {
					if (!realImageHeight.getText().isEmpty() && !realImageWidth.getText().isEmpty()){
						realHeight = Double.parseDouble(realImageHeight.getText());
						realWidth = Double.parseDouble(realImageWidth.getText());
						imageHeight = mapImage.getHeight();
						imageWidth = mapImage.getWidth();
					}


					ImageView image = new ImageView(mapImage);
					image.setX(0);
					image.setY(0);
	
					//setting the fit height and width of the image view 
					image.setFitHeight(500); 
					image.setFitWidth(300); 
					
					//Setting the preserve ratio of the image view 
					image.setPreserveRatio(true);  

					Bounds bounds = image.getLayoutBounds();
					double xScale = bounds.getWidth() / image.getImage().getWidth();
					double yScale = bounds.getHeight() / image.getImage().getHeight();

					double xToRealScale = realWidth/image.getImage().getWidth();
					double yToRealScale = realHeight/image.getImage().getHeight();

					image.setOnMouseClicked(click -> {

						if (clickedLocations.isEmpty()) {
							homeX = click.getX();
							homeY = click.getY();
						}
						currentX = ((click.getX() - homeX)/xScale)*xToRealScale;
						currentY = (((click.getY() - homeY)*-1)/yScale)*yToRealScale;
						Location current = new Location(pointName.getText(), (int)currentX, (int)currentY);
						clickedLocations.add(current);
						//clickedLocationsList.setText(clickedLocationsList.getText() + ", [" + currentX + ", " + currentY + "]");

						Circle circle = new Circle(click.getX(), click.getY(), 3);
						circle.setFill(javafx.scene.paint.Color.RED);
						deliveryPointsGroup.getChildren().add(circle);

					});

					deliveryPointsGroup.getChildren().add(image);
				}

			});

			uploadMapScreenLayout.add(fileChosenLabel, 0, 4);
			uploadMapScreenLayout.add(chooseMapButton, 2, 4);
			uploadMapScreenLayout.add(realImageWidthPrompt, 0, 6);
			uploadMapScreenLayout.add(realImageWidth, 0, 8);
			uploadMapScreenLayout.add(realImageHeightPrompt, 4, 6);
			uploadMapScreenLayout.add(realImageHeight, 4, 8);
			uploadMapScreenLayout.add(saveMapButton, 2, 10);



			mapScreenLayout.add(deliveryPointsGroup, 0, 0);
			//mapScreenLayoutTotal.getChildren().addAll(mapScreenLayout, deliveryPointsGroup);
			
			//------------------------------------------------------------------------------------------------
			
			
			//TODO: Decide on default
			simulationScreen = new Scene(simulationScreenLayout,1000,600);
			//TODO: Are we going to use this ever?
			simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			//TODO: Decide on default 
			//settingsScreen = new Scene(settingsScreenLayout,1000,500);
			
			


			mapScreen = new Scene(mapScreenLayout, 1000, 650);

			uploadMapScreen = new Scene(uploadMapScreenLayout, 1000, 650);

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
	public static void saveNewWValue(int elem, int plus) {
		//Checker to make sure the element exists
		if(weightPerOrder.length - 1 < elem) 
			throw new IndexOutOfBoundsException("Index " + elem + " is out of bounds!");
		
		else {//adds the parameter onto the int value 
			int temp = weightPerOrder[elem];
			weightPerOrder[elem] =  temp + plus;
		}
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

	/**
	 * Populates an ArrayList with simulation setting IDs from the simualtionSettings.xml
	 * @return ArrayList of simulation IDs
	 */
	public static ArrayList<String> getSimulationSettingsIDs() {
		ArrayList<String> ret = new ArrayList<String>();
		//Grab all of the elemets with the simulationsetting tag name
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
	 * Populates an ArrayList with the drone setting IDs from the droneSettings.xml
	 * @return ArrayList of drone IDs
	 */
	public static ArrayList<String> getDroneSettingsIDs(){
		ArrayList<String> ret = new ArrayList<String>();
		//Grab all of the elements with the drone tag name
		NodeList droneSettingsList = droneSettingsXML.getElementsByTagName("drone");
		for(int i = 0; i < droneSettingsList.getLength(); i++) {
			//Grab each drone item
			Element currentSettings = (Element) droneSettingsList.item(i);
			//Grab the id of that drone
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
				if(otherSimulationSettingElement.getAttribute("id").equals(id)) {
					currentSimulationSettingElement = otherSimulationSettingElement;
				}
			}	
		}
	
		
		//Simulation Variables for later use in building the simulationsetting object
		String simulationName = "";
		String droneIDNumber = "";
		ArrayList<Location> locations = new ArrayList<Location>();
		String deliveryPointChoice = "";
		String algorithmChoice = "";
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
				//If the element is the algorithm choice
				if(field.getTagName().equals("algorithmchoice")) {
					algorithmChoice = field.getTextContent();
				}
				//If the element is the delivery choice
				if(field.getTagName().equals("deliverypointchoice")) {
					deliveryPointChoice = field.getTextContent();
				}
			}
		}
		//Build and return the object from the set variables
		return new SimulationSettings(simulationName, buildDroneFromXML(droneIDNumber), locations, deliveryPointChoice, algorithmChoice, meals, hoursInShift, upperOrdersPerHour, lowerOrdersPerHour);
	}
	
	/**
	 * Build a drone object from an XML document
	 * @param id The id of the drone that is needed
	 * @return a Drone object with data from the XML
	 */
	public static Drone buildDroneFromXML(String id) {
		//Gets a list of all of the drone elements in the XML
		NodeList droneSettingList = droneSettingsXML.getElementsByTagName("drone");
		
		//Find which drone matches the ID and update it
		Element currentDroneElement = (Element) droneSettingList.item(0);
		for(int i = 0; i < droneSettingList.getLength(); i++) {
			Element otherDroneSettingElement = (Element) droneSettingList.item(i);
			if(otherDroneSettingElement.hasAttribute("id")) {
				//Find the drone settings by id and then update the current drone element
				if(otherDroneSettingElement.getAttribute("id").equals(id)) {
					currentDroneElement = otherDroneSettingElement;
				}
			}	
		}
		
		//Variables to store the xml data in
		String droneID = "";
		String name = "";
		double maxCargo = 0;
		double avgCruisingSpeed = 0;
		double maxFlightTime = 0;
		double turnAroundTime = 0;
		double unloadTime = 0;
		
		//Get the ID of the drone that we actually got
		droneID = currentDroneElement.getAttribute("id");
		//Flip through all of the drone fields
		NodeList currentDroneFields = currentDroneElement.getChildNodes();
		for(int i = 0; i < currentDroneFields.getLength(); i++) {
			//We need a node to check the type before turning into an element
			Node droneNodeField = currentDroneFields.item(i);
			if(droneNodeField.getNodeType()==Node.ELEMENT_NODE) {
				//Cast the node to Element
				Element droneElementField = (Element) droneNodeField;
				//If the tag is the name then set the name field for the drone
				if(droneElementField.getTagName().equals("name")) {
					name = droneElementField.getTextContent();
				}
				//If the tag is the maxcargo then set the maxCargo field for the drone
				if(droneElementField.getTagName().equals("maxcargo")) {
					maxCargo = Double.valueOf(droneElementField.getTextContent());
				}
				//If the tag is the avgcruisingspeed then set the avgCruisingSpeed field for the drone
				if(droneElementField.getTagName().equals("avgcruisingspeed")) {
					avgCruisingSpeed = Double.valueOf(droneElementField.getTextContent());
				}
				//If the tag is the maxflighttime then set the maxFlightTime field for the drone
				if(droneElementField.getTagName().equals("maxflighttime")) {
					maxFlightTime = Double.valueOf(droneElementField.getTextContent());
				}
				//If the tag is the turnaroundtime then set the turnAroundTime field for the drone
				if(droneElementField.getTagName().equals("turnaroundtime")) {
					turnAroundTime = Double.valueOf(droneElementField.getTextContent());
				}
				//If the tag is the unloadtime then set the unloadtime field for the drone
				if(droneElementField.getTagName().equals("unloadtime")) {
					unloadTime = Double.valueOf(droneElementField.getTextContent());
				}
			}
		}
		//Build and return the Drone object
		return new Drone(droneID,name,maxCargo,avgCruisingSpeed,maxFlightTime,turnAroundTime,unloadTime);
	}
	
	public static ObservableList<Location> getLocationsObservable(ArrayList<Location> list) {
		ObservableList<Location> data = FXCollections.observableArrayList();
		data.addAll(list);
		return data;
	}
	
	/*
	 * Method for when we need to build a setting screen with the id of the simulation settings object
	 * builds the screen here
	 * 
	 */
	public static void populatedSettingsScreen(String id, Stage primaryStage) {
		
		
		SimulationSettings sim =  buildSimulationSettingsFromXML(id);
		
		
		//Settings Screen Layout
		settingsScreenLayout = buildSettingsScreen();
		
		//HBox for the naming components of the simulation settings
		HBox simNameBox = new HBox();
		simNameBox.setAlignment(Pos.CENTER);	//Center the box
		simNameBox.setSpacing(5);	//Space between elements is 5
		
		//Label and TextField for the simulation TODO: Formatting
		Label simNameLabel = new Label("Simulation Name: ");
		TextField simNameField = new TextField(sim.getName());
		
		
		//Add Items to the HBox
		simNameBox.getChildren().addAll(simNameLabel,simNameField);
		//Add HBox to the grid and stretch it over 3 columns
		settingsScreenLayout.add(simNameBox, 0, 0, 3, 1);
		simulationName = sim.getName();
                		
		
		//VBox for things in column 1
		VBox columnOne = new VBox(20);
		columnOne.setAlignment(Pos.TOP_LEFT);
		
		//TODO: Use this to add things in column one
		//DRONE STUFF
	
		
		Label dronesL = new Label ("Drones: ");
		
		ScrollPane droneSelectorPane = new ScrollPane();
		
		//Sets which group of radio buttons this simulation is a part of
		ToggleGroup droneSelectorButtons = new ToggleGroup();
		//Make and populate the radio buttons
		VBox droneSelectorVBox = new VBox();

		for(String idNumber : droneSettingsIDs) {
			//Make a radio button with the name of the SimulationSetting
			RadioButton radioButton = new RadioButton(buildDroneFromXML(idNumber).getName());
			//Upon clicking on a radio button the currentSimulationID is set to the current ID
			radioButton.setOnAction(e->{
				currentDroneSettingID = idNumber;
				
			});
			//Start the simulation with the default settings
			if(idNumber.equals(currentDroneSettingID)) {
				radioButton.setSelected(true);
			}
			//Add radio button to toggle group and add it to the screen
			radioButton.setToggleGroup(droneSelectorButtons);
			droneSelectorVBox.getChildren().add(radioButton);
		}
		droneSelectorVBox.setSpacing(10);
		//Add the content to the screen
		HBox addAndEditButtonsBox = new HBox();
		addAndEditButtonsBox.setAlignment(Pos.CENTER);
		addAndEditButtonsBox.setSpacing(50);
		
		Button addNewDroneButton = new Button("Add New Drone");
		Button editDroneButton = new Button("Edit Drone");
		
		addAndEditButtonsBox.getChildren().add(addNewDroneButton);
		addAndEditButtonsBox.getChildren().add(editDroneButton);
		
		droneSelectorPane.setContent(droneSelectorVBox);
		
		//adds to the layout
		settingsScreenLayout.setVgap(0);
		settingsScreenLayout.add(droneSelectorPane, 0, 0);
		//settingsScreenLayout.add(addAndEditButtonsBox, 0, 1);
		
		
		
		//TODO: Add method for upload new campus map
		
		//TODO: Add method for add new drone
		
		//TODO: Add method for upload new campus map
		//Add Locations Buttons
		Label locationPointsLabel = new Label("Add Delivery Points");
		
		HBox addAndClearButtonsBox = new HBox();
		addAndEditButtonsBox.setAlignment(Pos.CENTER);
		//addAndEditButtonsBox.setSpacing(50);
		
		Button addDeliveryPointsBtn = new Button("Add Delivery Point(s)");
		Button clearDeliveryPointsBtn = new Button("Clear Delivery Points");
		
		addAndClearButtonsBox.getChildren().add(addDeliveryPointsBtn);
		addAndClearButtonsBox.getChildren().add(clearDeliveryPointsBtn);

    	//LOCATIONS STUFF
    	
    	//Logic for setting up the add a location screen
		GridPane addLocationScreenLayout = new GridPane();
		addLocationScreenLayout.setAlignment(Pos.CENTER);
		addLocationScreenLayout.setHgap(10);	//Set some spacing for horizontal
		addLocationScreenLayout.setVgap(10);	//Set some spacing for vertical
		
		//Add Delivery point name
		Label addNameLabel = new Label("Add Delivery Point Name:");
		TextField addNameTextField = new TextField();
		addLocationScreenLayout.add(addNameLabel, 0, 0);
		addLocationScreenLayout.add(addNameTextField, 1, 0);
		
		//Add Delivery point X value
		Label addXLabel = new Label("Value for X:");
		TextField addXTextField = new TextField();
		addLocationScreenLayout.add(addXLabel, 0, 1);
		addLocationScreenLayout.add(addXTextField, 1, 1);

		
		//Add Delivery point Y value
		Label addYLabel = new Label("Value for Y:");
		TextField addYTextField = new TextField();
		addLocationScreenLayout.add(addYLabel, 0, 2);
		addLocationScreenLayout.add(addYTextField, 1, 2);

		//Just a label to give the person the option
		Label orLabel = new Label("or");
		GridPane.setHalignment(orLabel, HPos.CENTER);
		addLocationScreenLayout.add(orLabel, 0, 3, 2, 1);
		
		//Add delivery points via file upload
		Label uploadLocationFileLabel = new Label("Upload Delivery Points:");
		Button uploadLocationFileBtn = new Button("Browse");
		GridPane.setHalignment(uploadLocationFileBtn, HPos.RIGHT);
		
		//TODO: Add logic for file upload/selection, probably throw exceptions here if something is wrong
		uploadLocationFileBtn.setOnAction(e->{
			FileChooser fileChooser = new FileChooser();
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			uploadLocationFileLabel.setText("CurrentFile: " + selectedFile.getName());
			try {
				tempLocations.clear();
				tempLocations = sim.populateLocations(selectedFile.getAbsolutePath());
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
		addLocationScreenLayout.add(uploadLocationFileLabel,0,4);
		addLocationScreenLayout.add(uploadLocationFileBtn,1,4);
		
		//Button for saving points
		Button saveLocationFileBtn = new Button("Save");
		saveLocationFileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		saveLocationFileBtn.setOnAction(e->{
			//TODO: Add logic for saving points
			if (!addNameTextField.getText().isEmpty() && !addXTextField.getText().isEmpty() && !addYTextField.getText().isEmpty()) {
				Location newLocation = new Location(addNameTextField.getText(), Integer.parseInt(addXTextField.getText()), Integer.parseInt(addYTextField.getText()));
				tempLocations.add(newLocation);
				
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText("Please make sure you do not leave any fields blank. Thanks!");
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, an Error Dialog");
				alert.showAndWait();
			}
			
			
			for(int i = 0; i > tempLocations.size(); i++)
				finalLocations.add(tempLocations.get(i));
			
			
			//locations.clear();
			//TODO: I can foresee this getting dicy with us leaving the edit screen and then losing the data that was in the file so lets be careful
			primaryStage.setScene(settingsScreen); 	
		});
		
		//Button for canceling the points
		Button cancelLocationFileBtn = new Button("Cancel");
		cancelLocationFileBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		cancelLocationFileBtn.setOnAction(e->{
			//DO NOTHING
			tempLocations.clear();
			//TODO: I can foresee this getting dicy with us leaving the edit screen and then losing the data that was in the file so lets be careful
			primaryStage.setScene(settingsScreen);
		});
		
		addLocationScreenLayout.add(saveLocationFileBtn, 0, 5);
		addLocationScreenLayout.add(cancelLocationFileBtn, 1, 5);
		addLocationScreen = new Scene(addLocationScreenLayout, 1000, 650);
		
		
		//-----------------------------------------------------------------------------------
		
		
		addDeliveryPointsBtn.setOnAction(e->{
			 
			//resets text field just in add.
			 addNameTextField.setText("");
			 addXTextField.setText("");
			 addYTextField.setText("");
			 primaryStage.setScene(addLocationScreen);
		});
		
		//TODO: Clear the delivery points
		
		clearDeliveryPointsBtn.setOnAction(e->{
			tempLocations.clear();
						
		});
		
		//More Locations Stuff
		
		
		Button viewMapButton = new Button("View Delivery Map");

		ChoiceBox deliveryPointsChoices = new ChoiceBox();
		deliveryPointsChoices.getItems().add("Default Grove City Points");
		deliveryPointsChoices.getItems().add("Points from file");
		deliveryPointsChoices.getItems().add("Points from map");

		ChoiceBox algoChoice = new ChoiceBox();
		algoChoice.getItems().add("Greedy Algorithm");
		algoChoice.getItems().add("Genetic Algorithm");
		
		deliveryPointsChoices.setValue("Default Grove City Points");
		algoChoice.setValue("Greedy Algorithm");
		

		viewMapButton.setOnAction(e ->{
			primaryStage.setScene(mapScreen);
		});
		
		Label locationTableView = new Label("Location Table");
		
//		FlowPane root = new FlowPane();
//		root.setAlignment(Pos.CENTER_LEFT);
		
//		Scene scene = new Scene(root, 450, 450);
		
		ObservableList<Location> locationList = buildSimulationSettingsFromXML(currentSimulationSettingID).getObservable();
		System.out.println(locationList);
		
		TableView<Location> locationTable;

		System.out.println("Test");
		//locationList = getLocationsObservable(clickedLocations);
		//locationList = getLocationsObservable(finalLocations);
		System.out.println("Delivery Choices" + deliveryPointsChoices.getValue().toString());
		if (deliveryPointsChoices.getValue().toString().contains("Map")) {
			locationList.clear();
			locationList = getLocationsObservable(clickedLocations);
			System.out.println(locationList);
		//	refresh(locationTable);

		}
		else if (deliveryPointsChoices.getValue().toString().contains("File")) {
			locationList.clear();
			locationList = getLocationsObservable(finalLocations);

		}
		else {
			locationList = getLocationsObservable(groveCityLocations);

		}
		locationTable = new TableView<Location>(locationList);
		
		TableColumn<Location, String> title = new TableColumn<>("Location");
		title.setCellValueFactory(new PropertyValueFactory<>("name"));
		locationTable.getColumns().add(title);
		
		TableColumn<Location, Integer> xVal = new TableColumn<>("X");
		xVal.setCellValueFactory(new PropertyValueFactory<>("x"));
		locationTable.getColumns().add(xVal);
		
		TableColumn<Location, Integer> yVal = new TableColumn<>("Y");
		yVal.setCellValueFactory(new PropertyValueFactory<>("y"));
		locationTable.getColumns().add(yVal);
		
deliveryPointsChoices.getValue();
		
		//set height and width
		locationTable.setPrefWidth(200);
		locationTable.setPrefHeight(200);

		
		//add everything to column one
		columnOne.getChildren().addAll(   locationPointsLabel, algoChoice, 
				addAndClearButtonsBox,viewMapButton, deliveryPointsChoices, locationTable);
		
		settingsScreenLayout.add(columnOne,0,1);
		
		//Add new drone button action
		addNewDroneButton.setOnAction(e -> {
			settingsScreenLayout.getChildren().remove(columnOne);
			columnOne.getChildren().remove(2);
			columnOne.getChildren().add(buildAddDroneScreen("1", primaryStage));
			settingsScreenLayout.add(columnOne,0,1);
			primaryStage.setScene(addDroneScreen);

		});
		
		//Add new drone button action
		editDroneButton.setOnAction(e -> {
				
			settingsScreenLayout.getChildren().remove(columnOne);
			columnOne.getChildren().remove(2 );
			columnOne.getChildren().add(buildAddDroneScreen(currentDroneSettingID, primaryStage));
			settingsScreenLayout.add(columnOne,0,1);
			primaryStage.setScene(addDroneScreen);

		});
		
		
		
	
		
		//VBox for things in column 2
		VBox columnTwo = new VBox();
		columnTwo.setAlignment(Pos.TOP_LEFT);
		
    	//Slider orderSlider = new Slider();
		Label perUsedL = new Label("Percentage Used: "  + String.valueOf(100));		
	    columnTwo.getChildren().add(perUsedL);
	    Label perLeftL = new Label("Percentage Left: " + String.valueOf(0));
	    columnTwo.getChildren().add(perLeftL);
		
		GridPane grid = new GridPane();
        grid.setVgap(1);
        grid.setHgap(1);
        
        ScrollPane scrollPaneOrders = new ScrollPane();
        
        
        //NodeList mealList = simulationSettingsXML.getElementsByTagName("meal");
        
        int amountOfOrders = sim.getMeals().size();
		//int amountOfOrders =  mealList.getLength();
		String orderLabelNums [] = new String [amountOfOrders];
		int burgerCounts[] = new int[amountOfOrders];
		int fryCounts[] = new int[amountOfOrders];
		int cokeCounts[] = new int[amountOfOrders];
		double perCountsB[] = new double [amountOfOrders];
		hoursInShift = sim.getHoursPerShift();
		upperOrdersPerHour = sim.getOrderUpper();
		lowerOrdersPerHour = sim.getOrderLower();
		ArrayList<Location> locations = sim.getLocations();
		ArrayList<Meal> meals = sim.getMeals();
		//resets total weight
		weightPerOrder = new int [amountOfOrders];
		finalLocations = sim.getLocations();
		//System.out.println("Read Meals: " + meals.get(2).toString());
		
		
		
		//intialize all the variables we need
		for(int i = 0; i < amountOfOrders; i++) {
			orderLabelNums[i] = "Order " + String.valueOf(i+1) + ":";
			perCountsB[i] = meals.get(i).getProbability() * 100;
			
			for(FoodItem foodItem: meals.get(i).getFoodItems()) {
				
				if(foodItem.toString().equals("burger")) {
					burgerCounts[i] += 1 ;
					weightPerOrder[i] += 6;
				}
				else if(foodItem.toString().equals("fries")) {
					fryCounts[i] += 1;
					weightPerOrder[i] += 4;
				}
				else if(foodItem.toString().equals("coke")) {
					cokeCounts[i] += 1 ;
					weightPerOrder[i] += 14;
				}	
				
			}
		}
       
        
        for(int i = 0; i < amountOfOrders; i++){
        	
        	//variable dictionary for the for loop 
        	int curLoopVal = i;
        	Label orderLabel = new Label(orderLabelNums[i]);
        	//Label orderLabel = new Label(orderNames[i]);
        	Slider orderSlider = new Slider();
        	Label currentSliderVal = new Label("");
        	Label burgerLabel = new Label("Burger");
        	final Spinner<Integer> spinnerBurger = new Spinner<Integer>();
        	Label burgerWeight = new Label(String.valueOf(burgerCounts[i]  *6) + " oz");
        	Label friesLabel = new Label("Fries");
        	final Spinner<Integer> spinnerFries = new Spinner<Integer>();
        	Label friesWeight = new Label(String.valueOf(fryCounts[i] * 4) + " oz");
        	Label cokeLabel = new Label("Coke");
        	final Spinner<Integer> spinnerCoke = new Spinner<Integer>();
        	Label cokeWeight = new Label(String.valueOf(cokeCounts[i] * 14) + " oz");
        	Label totalWeightLabel = new Label("Total:");
        	Label weightPerOrderLabel = new Label(String.valueOf(weightPerOrder[i]) + " oz");
        	
        	
        	
        	//Spinner val that doesn't go up to 16 burgers (to not go over 192 oz)	        	
        	SpinnerValueFactory<Integer> valueFactoryB = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 132, burgerCounts[i]);
        	spinnerBurger.setValueFactory(valueFactoryB);
        	spinnerBurger.setMaxWidth(50);
        	spinnerBurger.setEditable(true);

        	//Spinner val that doesn't go up to 24 fries (to not go over 192 oz)
        	SpinnerValueFactory<Integer> valueFactoryF = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 48, fryCounts[i]);
        	spinnerFries.setValueFactory(valueFactoryF);
        	spinnerFries.setMaxWidth(50);
        	spinnerFries.setEditable(true);

        	//Spinner val that doesn't go up to 13 cokes (to not go over 192 oz)
        	SpinnerValueFactory<Integer> valueFactoryC = //
        			new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 13, cokeCounts[i]);
        	spinnerCoke.setValueFactory(valueFactoryC);
        	spinnerCoke.setMaxWidth(50);
        	spinnerCoke.setEditable(true);

        	
        	//slider for percentages
        	orderSlider.setMin(0);
        	orderSlider.setMax(100);
        	orderSlider.setValue(perCountsB[i]);
        	//orderSlider.setShowTickLabels(true);
        	orderSlider.setShowTickMarks(true);
        	orderSlider.setMajorTickUnit(50);
        	orderSlider.setMaxWidth(300);
        	
        	//order probability percentages
        	orderSlider.valueProperty().addListener((obs, oldVal, newVal)
        			->{
        				//difference we can add to the percentage label
        				int difference = Math.round(newVal.intValue()) - Math.round(oldVal.intValue()) ;
        				int  perLeftNum = 100 - percentageSum;
        				
        				//prints lables here
        				percentageSum =  percentageSum + difference;
        				perUsedL.textProperty().bind(Bindings.format("%s %d %s", "Percentage Used: ", percentageSum, "%"));
        				perLeftL.textProperty().bind(Bindings.format("%s %d %s", "Percentage Used: ", perLeftNum , "%"));
        				
        			
        				meals.get(curLoopVal).setProbability((double)Math.round(newVal.doubleValue()) / 100.0);
        				
        			});
        	
        	//sends label here
        	totalWeightLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
        	
        	//curent val of slider percentage
        	currentSliderVal.textProperty().bind(Bindings.format("%.0f %s ", orderSlider.valueProperty(), "%"));
        	
        	
        	//burger spinner per each order with a listener
        	spinnerBurger.valueProperty().addListener((obs, oldVal, newVal) 
        			->{
        				FoodItem burger = new FoodItem("burger", 0.375);
        				if(oldVal > newVal){ // so we can remove weight
        					saveNewWValue(curLoopVal, -6);
        					meals.get(curLoopVal).removeFoodItem(burger);
        				}
        				else { //standard add
        					saveNewWValue(curLoopVal, 6);
        					meals.get(curLoopVal).addFoodItem(burger);
        				}
        				
        				//updates the burger label
        				weightPerOrderLabel.textProperty().bind(Bindings.format("%d %s", weightPerOrder[curLoopVal], "oz"));
        				burgerWeight.textProperty().bind(Bindings.format("%d %s", newVal * 6, "oz"));
        				
        			
        				
        			});
        	
        	//fries spinner per each order with a listener
        	spinnerFries.valueProperty().addListener((obs, oldVal, newVal) 
        			->{
        				FoodItem fries = new FoodItem("fries", 0.25);
        				if(oldVal > newVal) {//so we remove weight
        					saveNewWValue(curLoopVal, -4);
        					meals.get(curLoopVal).removeFoodItem(fries);
        				}
        				else { //standard add
        					saveNewWValue(curLoopVal, 4);
        					meals.get(curLoopVal).addFoodItem(fries);
        				}
        				
        				//updates fries labels
        				weightPerOrderLabel.textProperty().bind(Bindings.format("%d %s", weightPerOrder[curLoopVal], "oz"));
        				friesWeight.textProperty().bind(Bindings.format("%d %s", newVal * 4, "oz"));
        				
        			});
        	
        	//coke spinner per each order with a listener
        	spinnerCoke.valueProperty().addListener((obs, oldVal, newVal) 
        			-> {
        				FoodItem coke = new FoodItem("coke", 0.875);
        				if(oldVal > newVal) { //so we remove weight 
        					saveNewWValue(curLoopVal, -14);
        					meals.get(curLoopVal).removeFoodItem(coke);
        				}
        				else{ //standard add
        					saveNewWValue(curLoopVal, 14);
        					meals.get(curLoopVal).addFoodItem(coke);
        				}
        				
        				
        				//updates the coke labels
        				weightPerOrderLabel.textProperty().bind(Bindings.format("%d %s", weightPerOrder[curLoopVal], "oz"));
        				cokeWeight.textProperty().bind(Bindings.format("%d %s", newVal * 14, "oz"));
        				

        			});
        		        	
			
        	
        	//adds all labels and spinners to the grid pane here
        	GridPane.setConstraints(orderLabel, 0, 1 + (5*i));
        	grid.getChildren().add(orderLabel);
        	GridPane.setConstraints(currentSliderVal, 2, 1 + (5*i));
        	grid.getChildren().add(currentSliderVal);
        	GridPane.setConstraints(orderSlider, 1, 1 + (5*i));
        	grid.getChildren().add(orderSlider);
        	GridPane.setConstraints(burgerLabel, 0, 2 + (5*i));
        	grid.getChildren().add(burgerLabel);
        	GridPane.setConstraints(spinnerBurger, 1, 2 + (5*i));
        	grid.getChildren().add(spinnerBurger);
        	GridPane.setConstraints(burgerWeight, 2, 2 + (5*i));
        	grid.getChildren().add(burgerWeight);
        	GridPane.setConstraints(friesLabel, 0, 3 + (5*i));
        	grid.getChildren().add(friesLabel);
        	GridPane.setConstraints(spinnerFries, 1, 3 + (5*i));
        	grid.getChildren().add(spinnerFries);
        	GridPane.setConstraints(friesWeight, 2, 3 + (5*i));
        	grid.getChildren().add(friesWeight);
        	GridPane.setConstraints(cokeLabel, 0, 4 + (5*i));
        	grid.getChildren().add(cokeLabel);
        	GridPane.setConstraints(spinnerCoke, 1, 4 + (5*i));
        	grid.getChildren().add(spinnerCoke);
        	GridPane.setConstraints(cokeWeight, 2, 4 + (5*i));
        	grid.getChildren().add(cokeWeight);
        	GridPane.setConstraints(totalWeightLabel, 0, 5 + (5*i));
        	grid.getChildren().add(totalWeightLabel);
        	GridPane.setConstraints(weightPerOrderLabel, 1, 5 + (5*i));
        	grid.getChildren().add(weightPerOrderLabel);
        	
        	
        	
	}
        	        	        
		//TODO: Use this to add things in column two
		columnTwo.getChildren().addAll(grid);
		scrollPaneOrders.setContent(grid);
		columnTwo.getChildren().addAll(scrollPaneOrders);
		
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
		
		Label upperHoursLabel = new Label("Upper Bound:");
		final Spinner<Integer> spinnerUpperHours = new Spinner<Integer>();
    	Label lowerHoursLabel = new Label("Lower Bound:");
    	
    	final Spinner<Integer> spinnerLowerHours = new Spinner<Integer>();
    	
		//Hours Per Shift can't got lower than 1 or higher than 8
    	SpinnerValueFactory<Integer> valueFactoryHours = //
    			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 8, hoursInShift);
    	spinnerHours.setValueFactory(valueFactoryHours);
    	spinnerHours.setMaxWidth(70);
    	spinnerHours.setEditable(true);
    	

		//adds everything to grid pane 3
    	GridPane.setConstraints(hoursLabel, 0, 1);
    	gridC3.getChildren().add(hoursLabel);
    	GridPane.setConstraints(spinnerHours, 1, 1);
    	gridC3.getChildren().add(spinnerHours);
    	
    	
    	orderPerHourL.setAlignment(Pos.BASELINE_RIGHT);
    	GridPane.setConstraints(orderPerHourL, 0, 5);
    	gridC3.getChildren().add(orderPerHourL);

    	
    	
    	
    	//NOTE: spinner can't go higher than 30 orders but can change this 
    	SpinnerValueFactory<Integer> valueFactoryUpperHours = //
    			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 61, upperOrdersPerHour);
    	spinnerUpperHours.setValueFactory(valueFactoryUpperHours);
    	spinnerUpperHours.setMaxWidth(70);
    	spinnerUpperHours.setEditable(true);
    	

    	//spinner for the lower limits of thr orders per hour
    	//NOTE: spinner can't go higher than 30 orders but can change this         	
    	SpinnerValueFactory<Integer> valueFactoryLowerHours = //
    			new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 61, lowerOrdersPerHour);
    	spinnerLowerHours.setValueFactory(valueFactoryLowerHours);
    	spinnerLowerHours.setMaxWidth(70);
    	spinnerLowerHours.setEditable(true);

    	
    	
    	//adds all this stuff to grid pane 3 
    	GridPane.setConstraints(upperHoursLabel, 0, 8);
    	gridC3.getChildren().add(upperHoursLabel);
    	GridPane.setConstraints(spinnerUpperHours, 1, 8);
    	gridC3.getChildren().add(spinnerUpperHours);
    	
    	GridPane.setConstraints(lowerHoursLabel, 0, 10);
    	gridC3.getChildren().add(lowerHoursLabel);
    	GridPane.setConstraints(spinnerLowerHours, 1, 10);
    	gridC3.getChildren().add(spinnerLowerHours);
    	
		
		
		
    	
    
    	
		
		//TODO: Use this to add things in column three
		columnThree.getChildren().addAll(gridC3, dronesL, droneSelectorPane, addAndEditButtonsBox);
		
		settingsScreenLayout.add(columnThree,2,1);
		
		//HBox for the save and Cancel Buttons
		HBox saveAndCancelButtonsBox = new HBox();
		saveAndCancelButtonsBox.setAlignment(Pos.CENTER);
		saveAndCancelButtonsBox.setSpacing(100);
		
		//Save Simulation Settings button on Simulation settings screen
		Button saveSimulationSetngsBtn = new Button("Save Settings");
		//listener so we can go back to the simulation screen and write to the file
		saveSimulationSetngsBtn.setOnAction(e  ->  {
			
			readLocationsFrom = (String) deliveryPointsChoices.getValue();
			chosenAlgorithm = (String) algoChoice.getValue();
			Alert alert = new Alert(AlertType.ERROR);
			lowerOrdersPerHour = valueFactoryLowerHours.getValue();
			upperOrdersPerHour = valueFactoryUpperHours.getValue();
			
			Boolean canSaveSettings = true;
			for(int i = 0; i < weightPerOrder.length; i++) {
				if(weightPerOrder[i] > 192) {
					canSaveSettings = false;
					alert.setContentText("Please make sure your food orders are not heavier than 12lbs (192 oz)! Thanks!");
					
				}
			}
			
			if(percentageSum != 100) {
				canSaveSettings = false;
				alert.setContentText("Please make sure that your percentage add up to 100. Thanks!");
			}
			
			if(lowerOrdersPerHour > upperOrdersPerHour) {
				canSaveSettings = false;
				alert.setContentText("Please make sure the Lower Bound of Orders Per Hour is not higher than the Higher Bound. Thanks!");
			}
			
			if(canSaveSettings == false) {
				
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Look, an Error Dialog");
				alert.showAndWait();
			}
			else {
				//getting the most current variables 
				simulationName = simNameField.getText();
				hoursInShift = valueFactoryHours.getValue();
				
				for(int i = 0; i < tempLocations.size(); i++)
					finalLocations.add(tempLocations.get(i));
				tempLocations.clear();

				SimulationSettings newSimulation = new SimulationSettings(simulationName, buildDroneFromXML("1") , finalLocations, /*TODO: Populate with deliveryPointChoice*/,  /*TODO: Populate with algorithmChoice*/, meals , hoursInShift, upperOrdersPerHour, lowerOrdersPerHour);
				try {
					if(id == "1") {
						simulationSettingToXML(findAvailableSimulationSettingID(), newSimulation);
					}
					else {
						simulationSettingToXML(id, newSimulation);
					}

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}


				//Rebuilds the radio button
				//Scroll Pane for Radio Buttons Section
				ScrollPane simulationSelectorPane = new ScrollPane();
				//Sets which group of radio buttons this simulation is a part of
				ToggleGroup simulationSelectorButtons = new ToggleGroup();
				//Make and populate the radio buttons
				VBox simulationSelectorVBox = new VBox();
				simulationSettingsIDs = getSimulationSettingsIDs();
				for(String idNumber : simulationSettingsIDs) {

					//Make a radio button with the name of the SimulationSetting
					RadioButton radioButton = new RadioButton(getSimulationNameFromID(idNumber));
					//Upon clicking on a radio button the currentSimulationID is set to the current ID
					radioButton.setOnAction(f ->{
						currentSimulationSettingID = idNumber;
					});
					//Start the simulation with the default settings
					if(idNumber.equals(currentSimulationSettingID)) {
						radioButton.setSelected(true);
					}
					//Add radio button to toggle group and add it to the screen
					radioButton.setToggleGroup(simulationSelectorButtons);
					simulationSelectorVBox.getChildren().add(radioButton);
				}
				//Add the content to the screen
				simulationSelectorPane.setContent(simulationSelectorVBox);

				simulationScreenLayout.add(simulationSelectorPane, 0, 0);

				simulationScreen.setRoot(simulationScreenLayout);

				primaryStage.setScene(simulationScreen); //saves the stage
			}
			});	//Adds function to the button TODO: Expand function to not save if the user has not inputed correct values, possibly able to be done with throwing exceptions in a constructor
		
			saveSimulationSetngsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveAndCancelButtonsBox.getChildren().add(saveSimulationSetngsBtn);

			//Cancel Simulation Settings button on Simulation settings screen	
			Button cancelSimulationSetngsBtn = new Button("Cancel");
			cancelSimulationSetngsBtn.setOnAction(e -> {

				//Rebuilds the radio button
				//Scroll Pane for Radio Buttons Section
				ScrollPane simulationSelectorPane = new ScrollPane();
				//Sets which group of radio buttons this simulation is a part of
				ToggleGroup simulationSelectorButtons = new ToggleGroup();
				//Make and populate the radio buttons
				VBox simulationSelectorVBox = new VBox();
				simulationSettingsIDs = getSimulationSettingsIDs();
				for(String idNumber : simulationSettingsIDs) {

				//Make a radio button with the name of the SimulationSetting
					RadioButton radioButton = new RadioButton(getSimulationNameFromID(idNumber));
					//Upon clicking on a radio button the currentSimulationID is set to the current ID
					radioButton.setOnAction(f ->{
						currentSimulationSettingID = idNumber;
					});
					//Start the simulation with the default settings
					if(idNumber.equals(currentSimulationSettingID)) {
						radioButton.setSelected(true);
					}
					//Add radio button to toggle group and add it to the screen
					radioButton.setToggleGroup(simulationSelectorButtons);
					simulationSelectorVBox.getChildren().add(radioButton);
				}
			//Add the content to the screen
				simulationSelectorPane.setContent(simulationSelectorVBox);

				simulationScreenLayout.add(simulationSelectorPane, 0, 0);

				simulationScreen.setRoot(simulationScreenLayout);

				primaryStage.setScene(simulationScreen);
			});	//Adds function to the button TODO: Idk if there's anything else were gonna have to do here cause as long as we dont save anything we should be good
			cancelSimulationSetngsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
			saveAndCancelButtonsBox.getChildren().add(cancelSimulationSetngsBtn);


			settingsScreenLayout.add(saveAndCancelButtonsBox,0,2,3,1);

			settingsScreen = new Scene(settingsScreenLayout,1000,600);
			

			primaryStage.setScene(simulationScreen);
			//TODO: Decide if we want to be able to resize
			primaryStage.setResizable(false);
			primaryStage.setTitle("Dromedary Drones Simulation");
			primaryStage.show();
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
	 * Method to remove a drone from the droneSettings XML file
	 * @param id ID String of the drone we want to remove
	 * @throws Exception If the default drone has an attempt on its life then we throw an exception
	 */
	public static void removeDroneSettingFromXML(String id) throws Exception {
		//If someone tries to remove or edit the default settings
		if(id.equals("1")) {
			throw new Exception("Cannot remove default drone");
		}
		
		//Remove the id from the list used to parse grab drone names
		droneSettingsIDs.remove(id);
		
		//Loop through all of the drones to find the one to remove
		NodeList droneSettingsList = droneSettingsXML.getElementsByTagName("drone");
		for(int i = 0; i < droneSettingsList.getLength(); i++) {
			Element droneSettingElement = (Element) droneSettingsList.item(i);
			//If the id attribute exists
			if(droneSettingElement.hasAttribute("id")) {
				//If the id is the id that needs to be removed
				if(droneSettingElement.getAttribute("id").equals(id)) {
					droneSettingElement.getParentNode().removeChild(droneSettingElement);
				}
			}
		}
		//Run the updater to update and write the actual file
		updateDroneSettingsXML();
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
			
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");	//Uncomment for tabs, Indents the XML file instead of just listing the nodes out
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
	 * Helper method to update and write out to the drone settings xml
	 */
	public static void updateDroneSettingsXML() {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(droneSettingsXML);
			StreamResult streamResult = new StreamResult(new File("droneSettings.xml"));
			
			//transformer.setOutputProperty(OutputKeys.INDENT, "yes");	//Uncomment for tabs, Indents the XML file instead of just listing the nodes out
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
		
		//Add delivery point choice element to the current simulationsetting
		Element deliveryPointChoiceElement = simulationSettingsXML.createElement("deliverypointchoice");
		deliveryPointChoiceElement.setTextContent(simulationSetting.getDeliveryPointChoice());
		currentSimulationDocument.appendChild(deliveryPointChoiceElement);
		
		//Add delivery point choice element to the current simulationsetting
		Element algorithmChoiceElement = simulationSettingsXML.createElement("algorithmchoice");
		algorithmChoiceElement.setTextContent(simulationSetting.getAlgorithmChoice());
		currentSimulationDocument.appendChild(algorithmChoiceElement);
		
		//Run the updater to update and write the actual file
		updateSimulationSettingsXML();
	}
	
	public static void droneSettingToXML(String id, Drone droneSetting) throws Exception {
		removeDroneSettingFromXML(id);
		
		//Get the root of the file
		Element root = (Element) droneSettingsXML.getElementsByTagName("drones").item(0);
		
		//Make a new drone element and add its id to its attributes and add it to the root
		Element currentDroneDocument = droneSettingsXML.createElement("drone");
		currentDroneDocument.setAttribute("id", id);
		root.appendChild(currentDroneDocument);
		
		//Add a name element to the current drone
		Element currentDroneName = (Element) droneSettingsXML.createElement("name");
		currentDroneName.setTextContent(droneSetting.getName());
		currentDroneDocument.appendChild(currentDroneName);
		
		//Add a max cargo element to the current drone
		Element currentDroneMaxCargo = (Element) droneSettingsXML.createElement("maxcargo");
		currentDroneMaxCargo.setTextContent(String.valueOf(droneSetting.getMaxCargo()));
		currentDroneDocument.appendChild(currentDroneMaxCargo);
				
		//Add a drone average cruising speed element to the current drone
		Element currentDroneAverageCruisingSpeed = (Element) droneSettingsXML.createElement("avgcruisingspeed");
		currentDroneAverageCruisingSpeed.setTextContent(String.valueOf(droneSetting.getAvgCruisingSpeed()));
		currentDroneDocument.appendChild(currentDroneAverageCruisingSpeed);
		
		//Add a max flight time element to the current drone
		Element currentDroneMaxFlightTime = (Element) droneSettingsXML.createElement("maxflighttime");
		currentDroneMaxFlightTime.setTextContent(String.valueOf(droneSetting.getMaxFlightTime()));
		currentDroneDocument.appendChild(currentDroneMaxFlightTime);
		
		//Add a turn around time element to the current drone
		Element currentDroneTurnAroundTime = (Element) droneSettingsXML.createElement("turnaroundtime");
		currentDroneTurnAroundTime.setTextContent(String.valueOf(droneSetting.getTurnAroundTime()));
		currentDroneDocument.appendChild(currentDroneTurnAroundTime);
		
		//Add a turn around time element to the current drone
		Element currentDroneUnloadTime = (Element) droneSettingsXML.createElement("unloadtime");
		currentDroneUnloadTime.setTextContent(String.valueOf(droneSetting.getUnloadTime()));
		currentDroneDocument.appendChild(currentDroneUnloadTime);
		
		updateDroneSettingsXML();
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
	
	/**
	 * Find the next available ID for the Drone settings
	 * @return The next available ID for the drone settings
	 */
	public static String findAvailableDroneSettingID() {
		int counter = 1;
		
		//Find a value that is not in the drone settings already
		while(droneSettingsIDs.contains(String.valueOf(counter))) {
			counter++;
		}
		
		return String.valueOf(counter);
	}
	
	
	/**
	 * @param id The ID of the simulation in question
	 * @return The Name of the simulation
	 */
	public static String getSimulationNameFromID(String id) {
		return buildSimulationSettingsFromXML(id).getName();
	}
	
	public static ScrollPane buildAddDroneScreen (String id ,Stage primaryStage) {
		
		ScrollPane droneSelectorPane = new ScrollPane(); //what we return
		GridPane addDroneLayout = new GridPane();

		Drone drone = buildDroneFromXML(id);
		
		Label droneName = new Label("Drone Name: ");
		TextField droneNameTextField = new TextField(drone.getName());
		Label maxCargo = new Label("Max Cargo: ");
		TextField maxCargoTextField = new TextField(String.valueOf(drone.getMaxCargo()));
		Label poundsLabel = new Label(" lbs");
		Label avgCruisingSpeed = new Label("Average Cruising Speed: ");
		TextField avgCruisingSpeedTextField = new TextField(String.valueOf(drone.getAvgCruisingSpeed()));
		Label mphLabel = new Label(" mph");
		Label maxFlighTime = new Label("Max Flight Time: ");
		TextField maxFlightTimeTextField = new TextField(String.valueOf(drone.getMaxFlightTime()));
		Label minutesLabel = new Label(" minutes");
		Label minutesLabel1 = new Label(" minutes");
		Label minutesLabel2 = new Label(" minutes");
		Label turnAroundTime = new Label("Turn-Around Time: ");
		TextField turnAroundTimeTextField = new TextField(String.valueOf(drone.getTurnAroundTime()));
		Label unloadTime = new Label("Unload Time: ");
		TextField unloadTimeTextField = new TextField(String.valueOf(drone.getUnloadTime()));
		Button saveDrone = new Button("Save Drone");
		saveDrone.setMaxWidth(100);
		Button cancelButton = new Button ("Cancel");
		cancelButton.setMaxWidth(100);
		
		addDroneLayout.setAlignment(Pos.CENTER);
		addDroneLayout.add(droneName, 0, 0);
		addDroneLayout.add(droneNameTextField, 1, 0);
		addDroneLayout.add(maxCargo, 0, 1);
		addDroneLayout.add(maxCargoTextField, 1, 1);
		addDroneLayout.add(poundsLabel, 2, 1);
		addDroneLayout.add(avgCruisingSpeed, 0, 2);
		addDroneLayout.add(avgCruisingSpeedTextField, 1, 2);
		addDroneLayout.add(mphLabel, 2, 2);
		addDroneLayout.add(maxFlighTime, 0, 3);
		addDroneLayout.add(maxFlightTimeTextField, 1, 3);
		addDroneLayout.add(minutesLabel, 2, 3);
		addDroneLayout.add(turnAroundTime, 0, 4);
		addDroneLayout.add(turnAroundTimeTextField, 1, 4);
		addDroneLayout.add(minutesLabel1, 2, 4);
		
		addDroneLayout.add(unloadTime, 0, 5);
		addDroneLayout.add(unloadTimeTextField, 1, 5);
		addDroneLayout.add(minutesLabel2, 2, 5);
		

		addDroneLayout.add(saveDrone, 0, 7);
		addDroneLayout.add(cancelButton, 1, 7);
		
		saveDrone.setOnAction(e ->{
			
			
			//gets all variables that we need to write to xml
			String name = droneNameTextField.getText();
			String droneID = drone.getDroneID();		
			if(droneID.equals("1")) {
				droneID = findAvailableDroneSettingID();
				System.out.println(findAvailableDroneSettingID());
			}
			
			double maxCargoVal = Double.valueOf(maxCargoTextField.getText());
			double avgCruise = Double.valueOf(avgCruisingSpeedTextField.getText());
			double maxFlight = Double.valueOf(maxFlightTimeTextField.getText());
			double turnAround = Double.valueOf(turnAroundTimeTextField.getText());
			double unload =  Double.valueOf(unloadTimeTextField.getText());
			

				//builds the data here
				Drone returnDrone = new Drone(droneID, name, maxCargoVal, avgCruise, maxFlight, turnAround, unload);    
						
				//try catch to write to the xml
				try {
					droneSettingToXML(droneID, returnDrone);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				droneSettingsIDs = getDroneSettingsIDs();
	
				
				//Sets which group of radio buttons this simulation is a part of
				ToggleGroup droneSelectorButtons = new ToggleGroup();
				//Make and populate the radio buttons
				VBox droneSelectorVBox = new VBox();
				
				for(String idNumber : droneSettingsIDs) {
					//Make a radio button with the name of the SimulationSetting
					RadioButton radioButton = new RadioButton(buildDroneFromXML(idNumber).getName());
					//Upon clicking on a radio button the currentSimulationID is set to the current ID
					radioButton.setOnAction(f->{
						currentDroneSettingID = idNumber;
					});
					//Start the simulation with the default settings
					if(idNumber.equals(currentDroneSettingID)) {
						radioButton.setSelected(true);
					}
					//Add radio button to toggle group and add it to the screen
					radioButton.setToggleGroup(droneSelectorButtons);
					droneSelectorVBox.getChildren().add(radioButton);
				}
				droneSelectorVBox.setSpacing(10);
				//Add the content to the screen
				droneSelectorPane.setContent(droneSelectorVBox);
				
	
				primaryStage.setScene(settingsScreen);
			
		});
		
		cancelButton.setOnAction(e -> {
			droneSettingsIDs = getDroneSettingsIDs();
			
			
			//Sets which group of radio buttons this simulation is a part of
			ToggleGroup droneSelectorButtons = new ToggleGroup();
			//Make and populate the radio buttons
			VBox droneSelectorVBox = new VBox();
			
			for(String idNumber : droneSettingsIDs) {
				//Make a radio button with the name of the SimulationSetting
				RadioButton radioButton = new RadioButton(buildDroneFromXML(idNumber).getName());
				//Upon clicking on a radio button the currentSimulationID is set to the current ID
				radioButton.setOnAction(f->{
					currentDroneSettingID = idNumber;
				});
				//Start the simulation with the default settings
				if(idNumber.equals(currentDroneSettingID)) {
					radioButton.setSelected(true);
				}
				//Add radio button to toggle group and add it to the screen
				radioButton.setToggleGroup(droneSelectorButtons);
				droneSelectorVBox.getChildren().add(radioButton);
			}
			droneSelectorVBox.setSpacing(10);
			//Add the content to the screen
			droneSelectorPane.setContent(droneSelectorVBox);

			primaryStage.setScene(settingsScreen);
			
		});
		addDroneScreen = new Scene (addDroneLayout, 400, 400);
		return droneSelectorPane;
		
		
		
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			simulationSettingsXML = documentBuilder.parse("simulationSettings.xml");
			simulationSettingsIDs = getSimulationSettingsIDs();
			currentSimulationSettingID = "1";
			droneSettingsXML = documentBuilder.parse("droneSettings.xml");
			droneSettingsIDs = getDroneSettingsIDs();
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
		launch(args);
	}
	
}
