/**
 * Explosions.java 
 * 
 * This class stores Explosion objects. 
 * Explosions are a series of images stored in an Image array.
 * Explosion objects are added to an ArrayList in TankGame.java and 
 * Explosions are drawn whenever a bullet hits a tank or wall.
 * 
 * Cited from Airstrike.Explosions.java
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2
 */
package tankgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 *
 * @author Albert
 */
public class Explosions {
    private Image[] img;
    private int x, y;       // Coordinates.
    private int count;      // Explosion timer.
    private boolean finished;
    
    public Explosions(int x, int y, Image[] img){
        this.x = x;
        this.y = y;
        this.count = 0;
        this.finished = false;
        this.img = img;
    } 
    
    public boolean getFinished(){
        return this.finished;
    }
    
    /**
     * Changes which image (or frame) of the explosion will be draw next. 
     */
    public void update(){

        if(count < img.length-1){
            count ++;
        }else{
            finished = true;
        }
    }
    
    public void draw(ImageObserver obs, Graphics2D g2d) {
         if (!finished) {
             g2d.drawImage(img[count-1], x, y, obs);
         }
     }    
}
