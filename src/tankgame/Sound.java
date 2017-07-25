/**
 * Sound.java
 * 
 * The sound class stores data for two types of sounds: sounds that loop
 * continuously and sounds that only play once.
 * 
 * Cited from Airstrike.SoundPlayer.java
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2 
 */
package tankgame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    private AudioInputStream soundStream;
    private String soundFileName;
    private Clip clip; 
    
    // Type 1: sounds that loop.
    // Type 2: sounds that play only once.
    private int type;

    public Sound(int type, String soundFileName) {
        this.soundFileName = soundFileName;
        this.type = type;
        
        try {
            soundStream = AudioSystem.getAudioInputStream(Sound.class.getClassLoader().getResource(soundFileName));
            clip = AudioSystem.getClip();
            clip.open(soundStream);            
        }
        catch (Exception e) {
            System.out.println("Sound effect not found");
        }
        
        if(this.type == 1) {
            Runnable myRunnable = new Runnable() {                
                @Override
                public void run() {
                   while(true){
                       clip.start();
                       clip.loop(clip.LOOP_CONTINUOUSLY);
 
                       try {
                           Thread.sleep(10000);
                       }
                       catch (InterruptedException ex) {
                           Logger.getLogger(Sound.class.getName()).log(Level.SEVERE, null, ex);
                       }
                    } 
                }
            };
            
            Thread thread = new Thread(myRunnable);
            thread.start();
        }
    }
    
   public void play(){
       clip.start();
   }
   public void stop(){
       clip.stop();
   }      
}
