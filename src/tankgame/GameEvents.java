/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
