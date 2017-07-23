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
public class Wall extends GameObject {
    boolean breakable;
    private int width, height, cooldown;    // Breakable walls can respawn.
    private String soundFileName;
    Sound sound;    
    
    public Wall(Image img, int x, int y, boolean weak) {
        super(img, x, y, 0);
        cooldown = 0;
        breakable = weak;
	this.width = img.getWidth(null);
	this.height = img.getHeight(null);
	boom = false; 
        this.soundFileName = "Resources/Explosion_small.wav";
        this.sound = new Sound(2, soundFileName);
    }
    
    public boolean isRespawning() {
        return (cooldown == 0);
    }
    
    public void setCooldown(int cd) {
        this.cooldown = cd;
    }
    
    public int getCooldown() {
        return this.cooldown;
    }
    
    public String getSoundFileName() {
        return this.soundFileName;
    }
    
    public void addExplosion() {
        TankGame.explosions.add(new Explosions(x, y, TankGame.smallExplosion));
    }
    
    @Override
    public void draw(ImageObserver obs, Graphics2D g2d) {
	if(cooldown == 0) {
            g2d.drawImage(img, x, y, obs);
            this.boom = false;
            
            if(this.boom == true) {
                addExplosion();
            }
	}
        else{
            cooldown--;
	}        
    }
    
    public void update() {
        
    }
}
