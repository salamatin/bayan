import org.jfugue.theory.Note;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Andrey on 7/8/2016.
 */
public class BayanKeyListener implements NativeKeyListener {

    private static Map<Integer, Note> keyCodeToNote  = new HashMap<>();
    private Map<Integer, Boolean> keyCodeToPressed = new HashMap<>();

    private MidiPlayer player;

    public BayanKeyListener(MidiPlayer player) {
        this.player = player;
        initializeKeyCodeToNodeMap();
    }

    public void nativeKeyPressed(NativeKeyEvent event) {
        int keyCode = event.getKeyCode();

        if (keyCode == NativeKeyEvent.VC_ESCAPE) {
            exit();
        }

        if (!isKeyAlreadyDown(keyCode)) {
            keyCodeToPressed.put(keyCode, true);
            if (keyCodeToNote.containsKey(keyCode)) {
                player.startNote(keyCodeToNote.get(keyCode));
            }
        }
    }

    public void nativeKeyReleased(NativeKeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCodeToNote.containsKey(keyCode)) {
            player.stopNote(keyCodeToNote.get(keyCode));
        }
        keyCodeToPressed.put(keyCode, false);
    }

    public void nativeKeyTyped(NativeKeyEvent event) {}

    private void initializeKeyCodeToNodeMap() {
        File keysFile = new File(this.getClass().getClassLoader().getResource("keys.map").getFile());
        try (Scanner scanner = new Scanner(keysFile)) {
            while (scanner.hasNext()) {
                String[] keyCodeNodePair = scanner.nextLine().split("\t");
                keyCodeToNote.put(Integer.parseInt(keyCodeNodePair[0]), new Note(keyCodeNodePair[1]));
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find resource file keys.map.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private boolean isKeyAlreadyDown(int keyCode) {
        return keyCodeToPressed.getOrDefault(keyCode, false);
    }

    private void exit() {
        player.close();
        try {
            GlobalScreen.removeNativeKeyListener(this);
            GlobalScreen.unregisterNativeHook();
        }
        catch (NativeHookException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
}
