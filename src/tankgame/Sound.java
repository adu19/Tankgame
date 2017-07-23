/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author Albert
 */
public class Sound {
    private AudioInputStream soundStream;
    private String soundFile;
    private Clip clip;
    
    // 1 - sounds that play continuously.
    // 2 - sounds that play once. 
    private int type;

    public Sound(int type, String soundFile) {
        this.soundFile = soundFile;
        this.type = type;
        
        try {
            soundStream = AudioSystem.getAudioInputStream(Sound.class.getClassLoader().getResource(soundFile));
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
