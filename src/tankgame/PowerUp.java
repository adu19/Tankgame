/**
 * PowerUp.java
 * 
 * The PowerUp class stores two types of power ups: green bullet and red life.
 * 
 *      Type
 *      1 - Green bullet
 *      2 - Red life
 * 
 * Cited from Airstrike.PowerUp.java
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2 
 */
package tankgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class PowerUp extends GameObject{
    boolean show = false;
    int type;
    
    public PowerUp(Image img, int x, int y, int speed, int type) {
        super(img, x, y, speed);
        this.show = true;
        this.type = type;
    }

    public void setShow(boolean show) {
        this.show = show;
    }
    public boolean showing() {
        return this.show;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void draw(ImageObserver obs, Graphics g2d) {
        if(show) {
            g2d.drawImage(this.img, this.x, this.y, obs);
        }
    }
    
    public void update() {
        
    }
}
