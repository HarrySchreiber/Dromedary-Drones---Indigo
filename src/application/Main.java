package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private Scene simulationScreen, settingsScreen;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			simulationScreen = new Scene(root,400,400);
			simulationScreen.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(simulationScreen);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//TODO: Take out testing
		Simulation s = new Simulation();
		s.runSimulation();
		
		System.out.println("FIFO: " + s.getFifoData() + " Average Time: " + s.findAverage(s.getFifoData()) + " Worst Time: " + s.findWorst(s.getFifoData()));
		System.out.println("Knapsack: " + s.getKnapsackData()  + " Average Time: " + s.findAverage(s.getKnapsackData()) + " Worst Time: " + s.findWorst(s.getKnapsackData()));
		
		//launch(args);
	}
}
