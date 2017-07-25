/**
 * KeyControl.java
 * 
 * The KeyControl class is used to receive keyboard events
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2
 */
package tankgame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * From Airstrike.KeyControl.java
 * @author Albert
 */
public class KeyControl extends KeyAdapter {
    private GameEvents gameEvents;
    
    public KeyControl () {
        
    }

    public KeyControl(GameEvents ge){
        this.gameEvents = ge;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        gameEvents.setValue(e);
    }  
    
    @Override
    public void keyReleased(KeyEvent e) {
	gameEvents.setValue(e);
    }    
}
