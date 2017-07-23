/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
