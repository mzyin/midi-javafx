// Maggie Yin 
// 150 Midterm 2: Midi.java
// 
// JavaFX Midi Keyboard 
//  - play notes by typing (mimicking a physical piano) or clicking, 
// 	- save what you played by exporting the note sequence to a file,
//	- display bar displays the current (and previous) notes you have played,
// 	- choose from a selection of synthesizer instruments to play from,
// 	- button to change the octave for greater range,
//	- button for sustaining notes (like pedal) 

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Midi extends Application {

	   @Override
	   public void start(Stage stage) throws Exception {
	      Parent root = 
	         FXMLLoader.load(getClass().getResource("Midi.fxml"));

	      Scene scene = new Scene(root); // attach scene graph to scene
	      stage.setTitle("Midi"); // displayed in window's title bar
	      stage.setScene(scene); // attach scene to stage
	      stage.show(); // display the stage
	   }
	  
	   public static void main(String[] args) {
	      launch(args); 
	   }  
}