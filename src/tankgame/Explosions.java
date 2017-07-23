/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
