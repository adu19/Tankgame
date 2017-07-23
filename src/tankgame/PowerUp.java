/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

/**
 *
 * @author Albert
 */
public class PowerUp extends GameObject{
    boolean show = false;
    int type;
    
    public PowerUp(Image img, int x, int y, int speed, int type) {
        super(img, x, y, speed);
        this.show = true;
        this.type = type;
    }

    public boolean showing() {
        return show;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void draw(ImageObserver obs, Graphics g2d) {
        if(show) {
            g2d.drawImage(this.img, this.x, this.y, obs);
        }
    }
}
