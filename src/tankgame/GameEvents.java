/**
 * GameEvents.java 
 * 
 * The GameEvents class allows objects in Tank Wars to be observed. This is 
 * used to observe the two tanks and for collision detection. 
 * 
 * Cited from Airstrike.GameEvents.java.
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2
 */
package tankgame;

import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

/**
 *
 * @author Albert
 */
public class GameEvents extends Observable {
    public int type;        // 1 - key event, 2 - message.
    public Object event;   
    
    /**
     * Runs when a key was pressed or messaged typed. 
     * @param e 
     */
    public void setValue(KeyEvent e) {
        type = 1;
        event = e;
        setChanged();
        notifyObservers(this);
    }
    
    public void setValue(String msg) {
        type = 2;
        event = msg;
        setChanged();
        notifyObservers(this);
    }

    public int getType(){
        return this.type;
    }
    
    public Object getEvent(){
        return this.event;
    }    
}
