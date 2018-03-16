// Maggie Yin 
// 150 Midterm 2: MidiController.java
// 
// JavaFX Midi Keyboard 
//  - play notes by typing (mimicking a physical piano) or clicking, 
// 	- save what you played by exporting the note sequence to a file,
//	- display bar displays the current (and previous) notes you have played,
// 	- choose from a selection of synthesizer instruments to play from,
// 	- button to change the octave for greater range,
//	- button for sustaining notes (like pedal) 


import javafx.fxml.FXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.io.*;


public class MidiController {

	//variables
    private Synthesizer synthesizer;
    private MidiChannel[] midiChannels;
    private Instrument[] instruments;
    private int instrumentIndex = 0;
    private int noteNumber = -1;
    private int octave = 4;
    private boolean sustain = false;
    private boolean export = false;
    private boolean keyTyped = false;
    private String fileName = "";
    private String notesPlayed = "";   
    	private static final NumberFormat format = 
			NumberFormat.getInstance();
    	
    private HashMap<KeyCode, Integer> map = new HashMap<KeyCode, Integer>();
    
    private String[] noteString = new String[] { "C", 
    		"C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };
    
    //FXML Components
    @FXML
    private GridPane gridPane;
    @FXML
    private Button sustainButton, keyA, KeyW, keyS, KeyE, 
    		keyD, keyF, KeyT, keyG, KeyY, keyH, KeyU, keyJ, keyK, 
    		KeyO, keyL, KeyP, keyCol, keyQuo;
    @FXML 
    private Label octaveLabel, instrumentLabel, notesPlayedLabel, 
    		currentNoteLabel,exportStatusLabel, displayLabel, displayLabel2;
    @FXML
    private MenuItem clearMenuItem, exportMenuItem;
    @FXML
    private TextField fileTextField;
    @FXML
    private Button[] keyArray = new Button[18];
    
    
    
    /*************************************************************************
    *                       Handlers for MOUSE Clicks                        *
    *************************************************************************/
    @FXML
    void APressed(ActionEvent event) { 
    		playNote(map.get(KeyCode.A));
    }
    @FXML
    void AposPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.QUOTE));
    }
    @FXML
    void ColonPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.SEMICOLON));
    }
    @FXML
    void DPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.D));
    }
    @FXML
    void EPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.E));
    }
    @FXML
    void FPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.F));
    }
    @FXML
    void GPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.G));
    }
    @FXML
    void HPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.H));
    }
    @FXML
    void JPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.J));
    }
    @FXML
    void KPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.K));
    }
    @FXML
    void LPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.L));
    }
    @FXML
    void OPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.O));
    }
    @FXML
    void PPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.P));
    }
    @FXML
    void SPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.S));
    }
    @FXML
    void TPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.T));
    }
    @FXML
    void UPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.U));
    }
    @FXML
    void WPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.W));
    }
    @FXML
    void YPressed(ActionEvent event) {
    		playNote(map.get(KeyCode.Y));
    }
    @FXML
    void nextInstrumentClicked(ActionEvent event) {
    		changeDevice(KeyCode.PERIOD);
    }
    @FXML
    void octaveDownClicked(ActionEvent event) {
    		changeOctave(KeyCode.Z);
    }
    @FXML
    void octaveUpClicked(ActionEvent event) {
    		changeOctave(KeyCode.X);
    }
    @FXML
    void previousInstrumentClicked(ActionEvent event) {
    		changeDevice(KeyCode.COMMA);
    }
    @FXML
    void sustainPressed(ActionEvent event) {
    		handleSustain();
    }
    
    
    
    /*************************************************************************
    *                          Sustain Button Clicked                        *
    *************************************************************************/
    public void handleSustain()
    {
    		if(!sustain)
		{
			sustainButton.setStyle("-fx-background-color: "
					+ "#662889;-fx-alignment: TOP_RIGHT;");
			sustainButton.setText("Sustain on");
			sustain = true;
		}
		else {
			
			sustainButton.setStyle("-fx-alignment: TOP_RIGHT; "
					+ "-fx-background-color: #cd8cff;");
			sustainButton.setText("Sustain off");
			sustain = false;
		}
    }
    
    
    /*************************************************************************
    *                            Change Key Colors                           *
    *************************************************************************/
    //change key color when key is pressed/released
    public void changeKeyColor(int noteNumber, boolean on)
    {
		if(keyArray[noteNumber-12].getId().charAt(0)=='K') //key is black key
		{
			if(on) //key is being played 
			{
				//change to dark grey
				keyArray[noteNumber-12].setStyle(
				"-fx-background-color: #2e2e2e; -fx-alignment: BOTTOM_LEFT");
			}
			else //key is being released
			{	
				//change to black 
				keyArray[noteNumber-12].setStyle(
					"-fx-background-color: #000000; -fx-alignment: BOTTOM_LEFT");
			}
		}
		else //key is white key
		{
			if(on) {
				//change to light grey
				keyArray[noteNumber-12].setStyle(
						"-fx-background-color: #bbbbbb; -fx-alignment: BOTTOM_LEFT");
			}
			else {
				//change to white
				keyArray[noteNumber-12].setStyle(
					 "-fx-background-color: #ffffff; -fx-alignment: BOTTOM_LEFT");
			}
		}
    }
    		
    
    
    
    /*************************************************************************
    *                            Find Note Number                            *
    *************************************************************************/
    //loops through hashmap to find note number that matches key
    public void findNoteNumber(KeyCode code)
    {
    		noteNumber = -1;

		for (KeyCode key : map.keySet()) 
		{
			if(key==code) {
				noteNumber = map.get(key);
				break;
			}
		}
    }
    
    
    
    /*************************************************************************
    *                               Play Note                                *
    *************************************************************************/
    public void playNote(int noteNumber)
    {
    		if(export)
    		{
    			clear();
    		}
    		
    		midiChannels[0].noteOn(noteNumber+12*octave,100); //play note
    		
    		String note = ""; 
    		if(noteNumber<24)
    		{
    			 note = noteString[noteNumber-12]; //get note letter from noteNumber
    			 note = note.concat(format.format(octave)); //add the octave
    		}
    		else
    		{
    			note = noteString[noteNumber-24];
    			note = note.concat(format.format(octave+1));
    		}

    		currentNoteLabel.setText(note); //display the note just played
    		notesPlayed = notesPlayed.concat(note); //add this note to notesPlayed
    		notesPlayed = notesPlayed.concat("   "); //add a space between notes
    		
    		notesPlayedLabel.setText(notesPlayed); //display all the notes played

    		if(keyTyped) {
    			changeKeyColor(noteNumber, true); //change the color when note typed
    			keyTyped = false;
    		}				
    }
    
    
    /*************************************************************************
    *                          Clear Display Area                            *
    *************************************************************************/
    public void clear()
    {
    		if(export)
    		{
    			displayLabel.setText("Sequence Played");
    			exportStatusLabel.setText("");
    			notesPlayedLabel.setVisible(true);
    			fileTextField.setVisible(false);
    			fileTextField.setDisable(true);
    			displayLabel2.setVisible(false);
    			export = false;
    		}
    		notesPlayedLabel.setText("");
    		currentNoteLabel.setText("");
    		notesPlayed = "";
    }

    
    
    /*************************************************************************
    *                              Export to File                            *
    *************************************************************************/
    public void export()
    {
    		export = true;
    		displayLabel.setText("Export Status");
    		fileTextField.setDisable(true);
    		fileTextField.setVisible(false);
    		displayLabel2.setVisible(false);
    		
    		try (PrintWriter out = new PrintWriter(fileName+".txt")) //use user's file name
    		{ 
    			out.println(notesPlayed); //write notesPlayed string to file 
    			exportStatusLabel.setText("Export complete: "+ fileName +".txt");
    		}
    		catch(IOException e)
    		{
    			exportStatusLabel.setText("Error exporting to file");
    		}
    		notesPlayed = "";
    }

    
    
    /*************************************************************************
    *                              Change Instrument                         *
    *************************************************************************/
    public void changeDevice(KeyCode code)
    {
    		switch (code) 
    		{
    			case COMMA: //decrement 
    				 if (instrumentIndex == 0) {
                     instrumentIndex = instruments.length - 1;
                 } else {
                	 	instrumentIndex--;
                 }

    				 synthesizer.getChannels()[0].programChange(instrumentIndex);
    				 instrumentLabel.setText(
    						 instruments[instrumentIndex].getName()); //update label
    				 break;
    				 
    			case PERIOD: //increment
    				if (instrumentIndex == instruments.length - 1) {
                        instrumentIndex = 0;
    				} else {
    					instrumentIndex++;
    				}

    				synthesizer.getChannels()[0].programChange(instrumentIndex);
    				instrumentLabel.setText(
    						instruments[instrumentIndex].getName()); //update label
    				break;
    		}
    }
    
    
    
    /*************************************************************************
    *                              Change Octave                             *
    *************************************************************************/
    public void changeOctave(KeyCode code)
    {
    		String text ="C";
    		switch (code)
    		{
    			case X: //increment 
    				if(octave<9){
    					octave++;
    				}
    				break;
    			case Z: //decrement
    				if(octave>0){
    					octave--;
    				}
    				break;
    		}
    		octaveLabel.setText(text.concat(format.format(octave))); //update label
    }
    

    /*************************************************************************
    *                                Initialize                              *
    *************************************************************************/
    public void initialize() {
    	
    		gridPane.requestFocus();

    	 /************************************************
    	 *             Put objects in HashMap            *
     ************************************************/    		
    		map.put(KeyCode.A, 12);
    		map.put(KeyCode.W, 13);
    		map.put(KeyCode.S, 14);
    		map.put(KeyCode.E, 15);
    		map.put(KeyCode.D, 16);
    		map.put(KeyCode.F, 17);
    		map.put(KeyCode.T, 18);
    		map.put(KeyCode.G, 19);
    		map.put(KeyCode.Y, 20);
    		map.put(KeyCode.H, 21);
    		map.put(KeyCode.U, 22);
    		map.put(KeyCode.J, 23);
    		map.put(KeyCode.K, 24);
    		map.put(KeyCode.O, 25);
    		map.put(KeyCode.L, 26);
    		map.put(KeyCode.P, 27);
    		map.put(KeyCode.SEMICOLON, 28);
    		map.put(KeyCode.QUOTE, 29);
    		map.put(KeyCode.Z, -2);
    		map.put(KeyCode.X, -2);
    		map.put(KeyCode.COMMA, -3);
    		map.put(KeyCode.PERIOD, -3);
    		map.put(KeyCode.CAPS, -4);
    		

  	 /************************************************
  	 *           Put buttons in button array         *
  	 ************************************************/
    		keyArray[0] = keyA;
    		keyArray[1]= KeyW;
    		keyArray[2] = keyS;
    		keyArray[3] = KeyE;
    		keyArray[4] = keyD;
    		keyArray[5] = keyF;
    		keyArray[6] = KeyT;
    		keyArray[7] = keyG;
    		keyArray[8] = KeyY;
    		keyArray[9] = keyH;
    		keyArray[10] = KeyU;
    		keyArray[11] = keyJ;
    		keyArray[12] = keyK;
    		keyArray[13] = KeyO;
    		keyArray[14] = keyL;
    		keyArray[15] = KeyP;
    		keyArray[16] = keyCol;
    		keyArray[17] = keyQuo;
    		
    		
    	 /************************************************
     *       Get Instruments and Midi Channels       *
     ************************************************/
    		try {
    			synthesizer = MidiSystem.getSynthesizer();
    			synthesizer.open();
    		} 
    		catch (MidiUnavailableException ex) {
    			ex.printStackTrace();
    			System.exit(1);
    		}   

    		midiChannels = synthesizer.getChannels();
    		Soundbank bank = synthesizer.getDefaultSoundbank();
    		synthesizer.loadAllInstruments(bank);

    		instruments = synthesizer.getAvailableInstruments();
    		synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());
    		synthesizer.getChannels()[0].programChange(instrumentIndex);

    		System.out.println("MIDI channels: " + midiChannels.length);
    		System.out.println("Instruments: " + instruments.length);
    
    
    		
     /************************************************
     *               KeyPressed Handler              *
     ************************************************/
    		gridPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
    			@Override
    			public void handle(KeyEvent event) {
    				keyTyped = true;
    				KeyCode code = event.getCode(); //get KeyCode of key pressed
    				findNoteNumber(code); //get corresponding midi note number
        		
    				if(noteNumber==-2) //change octave key pressed
    				{	
    					changeOctave(code);
    				}
    				else if(noteNumber== -3) //change instrument key pressed
    				{
    					changeDevice(code);
    				}
    				else if(noteNumber == -4) //sustain key pressed
    				{
    					handleSustain();
    				}
    				else if(noteNumber!=-1) {
    					playNote(noteNumber); //play the note 
    				}
    			}
    		});
    		

    	 /************************************************
    	 *               KeyReleased Handler             *
    	 ************************************************/
    		gridPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
    			@Override
    			public void handle(KeyEvent event) {
    				KeyCode code = event.getCode(); //get KeyCode of key pressed
    				findNoteNumber(code); //get corresponding midi note number
            
    				if(noteNumber>=0) { //if note found
    					if(!sustain) {
    						midiChannels[0].noteOff(
    								noteNumber+12*octave, 90); //release note 
    					}
    					changeKeyColor(noteNumber, false); //change back key color		
    				}
    			}
    		});
    		
    		
    		
    	 /************************************************
    	 *         File TextField KeyEvent Handler       *
    	 ************************************************/
    		fileTextField.setOnKeyPressed(new EventHandler<KeyEvent>() {
    			@Override
    			public void handle(KeyEvent event) {
    				if(event.getCode()==KeyCode.ENTER) //if enter is pressed
    				{
    					//update fileName variable
    					fileName = fileTextField.getText();
    					export(); //call export function
    				}
    			}		
    		});
    		
    		
    		
    	 /************************************************
    	 *     ChangeListener for NotesPlayed String     *
    	 ************************************************/   
    		notesPlayedLabel.textProperty().addListener(new ChangeListener<String>() {
    			@Override
    			public void changed(ObservableValue<? extends String> 
    					observable, String oldValue, String newValue) {
    				//update preferred width so the label grows with the string
    				notesPlayedLabel.setPrefWidth(
    						notesPlayedLabel.getText().length()*7);
    			}
    		});
    
    
    		
    	 /************************************************
    	 *              Clear MenuItem Clicked           *
    	 ************************************************/
    		clearMenuItem.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				clear(); //call clear function
    			}
    		});
    
    		
    		
    	/************************************************
    *             Export MenuItem Clicked           *
    ************************************************/
    		exportMenuItem.setOnAction(new EventHandler<ActionEvent>() {
    			@Override
    			public void handle(ActionEvent event) {
    				export = true;
    				notesPlayedLabel.setVisible(false);
    				
    				if(notesPlayed=="") //don't export if nothing played
    				{ 
    					exportStatusLabel.setText(
    							"Nothing to export. Play something.");
    				}
    				else //change the display bar for user to input file name
    				{ 
    					notesPlayedLabel.setText("");
    					displayLabel2.setVisible(true);
    					displayLabel.setText("Name your file");
    					fileTextField.setDisable(false);
    					fileTextField.setVisible(true);
    					fileTextField.requestFocus();
    				}
    			}
    		});
    	}
    
}
        

