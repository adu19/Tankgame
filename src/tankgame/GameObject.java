/**
 * GameObject.java
 * 
 * Keeps track of data shared by all objects in Tank Wars.
 * This includes location and speed.
 * 
 * Citing Airstrike.GameObject.java
 *
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2
 */
package tankgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Observable;
import java.util.Observer;

public class GameObject implements Observer{
    // Sprite for game object. 
    protected Image img;
   
    // (x,y) coord, size of object, and speed.
    protected int x, y, height, width, speed;      
    
    // Game object exploding.
    protected boolean boom;
    private boolean show;
    
    public GameObject(Image img, int x, int y, int speed) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.speed = speed;        
        this.width = img.getWidth(null);
        this.height = img.getHeight(null);
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }

    public void setX(int x){
        this.x = x;
    }
    
    public void setY(int y){
        this.y = y;
    }  
    
    public int getWidth() {
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }   
    
     public void draw(ImageObserver obs, Graphics2D g2d){
         g2d.drawImage(img, x, y, obs);    
    }
     

    @Override
    public void update(Observable o, Object arg) {}
}