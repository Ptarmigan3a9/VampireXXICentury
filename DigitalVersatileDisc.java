/**
 * @authors Lazaro Junior, Lucas Laet and Matheus Giovanny.
 * @version 1.0 of 2019.
 */
package vampiroseculoxxi;

import java.io.*;
import javax.sound.sampled.*;

public class DigitalVersatileDisc {
    
    public DigitalVersatileDisc() {
    }

    public void playContent() {
        try {
            File audio = new File(".\\src\\vampiroseculoxxi\\A-Thousand-Years-Remix.wav");
            Clip toPlay = AudioSystem.getClip();
            toPlay.open(AudioSystem.getAudioInputStream(audio));
            toPlay.start();
            //Thread.sleep(10000);
        } catch (Exception exceptionName) {
            System.out.println(exceptionName);
        }
    }
}
