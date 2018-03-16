import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;

public class KeyboardPiano {

    private static final String APP_TITLE = "Keyboard Piano";

    private final JFrame frame = new JFrame(APP_TITLE);
    private final Canvas canvas = new Canvas();
    private Synthesizer synthesizer;
    private final MidiChannel[] midiChannels;
    private final Instrument[] instruments;
    private int instrumentIndex = 0;

    KeyboardPiano() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
        } catch (MidiUnavailableException ex) {
            ex.printStackTrace();
            System.exit(1);
        }   

        this.midiChannels = synthesizer.getChannels();

        Soundbank bank = synthesizer.getDefaultSoundbank();

        synthesizer.loadAllInstruments(bank);


        this.instruments = synthesizer.getAvailableInstruments();
        synthesizer.loadAllInstruments(synthesizer.getDefaultSoundbank());
        synthesizer.getChannels()[0].programChange(instrumentIndex);

        System.out.println("[STATE] MIDI channels: " + midiChannels.length);
        System.out.println("[STATE] Instruments: " + instruments.length);
    }

    private void init() {   
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(canvas);
        frame.setSize(100, 100);
        frame.setResizable(false);
        canvas.addKeyListener(new KeyboardPianoListener());
        canvas.setFocusable(true);
        canvas.requestFocus();
    }

    private void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        KeyboardPiano keyboardPiano = new KeyboardPiano();
        keyboardPiano.init();
        keyboardPiano.show();
    }

    private final class KeyboardPianoListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getExtendedKeyCode();
            int noteNumber = -1;

            switch (keyCode) {
                case KeyEvent.VK_1: {
                    noteNumber = 60;
                    break;
                }

                case KeyEvent.VK_2: {
                    noteNumber = 62;
                    break;
                }

                case KeyEvent.VK_3: {
                    noteNumber = 64;
                    break;
                }

                case KeyEvent.VK_4: {
                    noteNumber = 65;
                    break;
                }

                case KeyEvent.VK_5: {
                    noteNumber = 67;
                    break;
                }

                case KeyEvent.VK_6: {
                    noteNumber = 69;
                    break;
                }

                case KeyEvent.VK_7: {
                    noteNumber = 71;
                    break;
                }

                case KeyEvent.VK_8: {
                    noteNumber = 72;
                    break;
                }

                case KeyEvent.VK_LEFT: {
                    if (instrumentIndex == 0) {
                        instrumentIndex = instruments.length - 1;
                    } else {
                        instrumentIndex--;
                    }

                    synthesizer.getChannels()[0].programChange(instrumentIndex);
                    System.out.println("Switched to " + 
                                       instruments[instrumentIndex].getName());
                    break;
                }

                case KeyEvent.VK_RIGHT: {
                    if (instrumentIndex == instruments.length - 1) {
                        instrumentIndex = 0;
                    } else {
                        instrumentIndex++;
                    }

                    synthesizer.getChannels()[0].programChange(instrumentIndex);
                    System.out.println("Switched to " + 
                                       instruments[instrumentIndex].getName());
                    break;
                }
            }

            if (noteNumber != -1) {
                midiChannels[0].noteOn(noteNumber, 600);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            int keyCode = e.getExtendedKeyCode();
            int noteNumber = -1;

            switch (keyCode) {
                case KeyEvent.VK_1: {
                    noteNumber = 60;
                    break;
                }

                case KeyEvent.VK_2: {
                    noteNumber = 62;
                    break;
                }

                case KeyEvent.VK_3: {
                    noteNumber = 64;
                    break;
                }

                case KeyEvent.VK_4: {
                    noteNumber = 65;
                    break;
                }

                case KeyEvent.VK_5: {
                    noteNumber = 67;
                    break;
                }

                case KeyEvent.VK_6: {
                    noteNumber = 69;
                    break;
                }

                case KeyEvent.VK_7: {
                    noteNumber = 71;
                    break;
                }

                case KeyEvent.VK_8: {
                    noteNumber = 72;
                    break;
                }
            }

            if (noteNumber != -1) {
                midiChannels[0].noteOff(noteNumber, 600);
            }
        }
    }
}