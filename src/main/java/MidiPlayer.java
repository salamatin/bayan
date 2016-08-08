import org.jfugue.realtime.RealtimePlayer;
import org.jfugue.theory.Note;

import javax.sound.midi.MidiUnavailableException;

/**
 * Created by Andrey on 7/8/2016.
 */
public class MidiPlayer {

    private static final int ACCORDION_CODE = 21;

    private RealtimePlayer player;

    public MidiPlayer() {
        try {
            player = new RealtimePlayer();
        } catch (MidiUnavailableException e) {
            System.err.println("Could not create a midi player.");
            e.printStackTrace();
            System.exit(1);
        }
        player.changeInstrument(ACCORDION_CODE);
    }

    public void startNote(Note note) {
        player.startNote(note);
    }

    public void stopNote(Note note) {
        player.stopNote(note);
    }

    public void close() {
        player.close();
    }
}
