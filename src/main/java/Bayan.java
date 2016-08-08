import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.sound.midi.MidiUnavailableException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Andrey on 6/8/2016.
 */
public class Bayan {

    public static void main(String[] args) throws MidiUnavailableException {
        disableLogging();
        registerNativeHook();

        MidiPlayer player = new MidiPlayer();
        GlobalScreen.addNativeKeyListener(new BayanKeyListener(player));
        System.out.println("Put the keyboard vertically and play like bayan's melody keyboard. I.e. \"E\" is 5th octave C, \"D\" is C# etc. ");
        System.out.print("Press Esc to quit.");
    }

    private static void disableLogging() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        Logger.getGlobal().setLevel(Level.SEVERE);
        logger.setLevel(Level.OFF);
        logger.setUseParentHandlers(false);
        Logger.getLogger("java.util.prefs").setLevel(Level.OFF);
    }

    private static void registerNativeHook() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
