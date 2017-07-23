/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 *
 * @author Albert
 */
public class Bullet extends GameObject {
    int angle, speed;
    boolean show;
    
    public Bullet(Image img, int x, int y, int angle, int speed) {
        super(img, x, y, speed);
        this.speed = speed;
        this.show = true;
        this.angle = angle;
    }

    public boolean getShow(){
        return this.show;
    }
    
    public void setShow(boolean s){
        this.show = s;
    }
    
    public void update() {
        x += speed * Math.cos(Math.toRadians(6 * angle));
	y -= speed * Math.sin(Math.toRadians(6 * angle));
    }
    
    @Override
    public void draw(ImageObserver obs, Graphics2D g2d) {
	if(show){
            BufferedImage bimg = (BufferedImage) img;
            double rotationRequired = Math.toRadians(-6 * angle);
            
            // newX = x + (offsetX * Math.cos(angle) - offsetY * Math.sin(angle);           
            int newX = (int) (x + (32 * Math.cos(rotationRequired)) - (0 * Math.sin(rotationRequired)));
            int newY = (int) (y + (32 * Math.sin(rotationRequired)) + (0 * Math.cos(rotationRequired)));

            AffineTransform at = AffineTransform.getTranslateInstance(newX, newY);
            at.rotate(rotationRequired, bimg.getWidth() / 2, bimg.getHeight() / 2);
        
            g2d.drawImage(bimg, at, obs);
	}       
    }
}
