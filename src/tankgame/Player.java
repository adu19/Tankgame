/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Albert
 */
public class Player {
    private Tank myTank;
    private BufferedImage tankImg;
    private int ID, lives, up, down, left, right, fire; 

    public Player(int ID, int lives, int left, int right, int up, int down, int fire) {
        this.ID = ID;
        this.lives = lives;
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.fire = fire;
        
        if(this.ID == 1) {
            try {
                tankImg = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Tank1.png")));                
                myTank = new Tank(tankImg, this.lives, 35, 35, 4, left, right, up, down, fire, this.ID);
            }
            catch (Exception e) {
                System.out.println("Player: No resource found in tank class");
            }
        }        
        else if(this.ID == 2) {
            try {
                tankImg = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Tank2.png")));                
                myTank = new Tank(tankImg, this.lives, 795, 475, 4, left, right, up, down, fire, this.ID);                        
            }
            catch (Exception e) {
                System.out.println("Player: No resource found");
            } 
        }
    }
    
    public Tank getTank() {
        return this.myTank;
    }
    
    public boolean lose(){
        if(myTank.getLives() <= 0)
            return true;
        else
            return false;
    }    
}
