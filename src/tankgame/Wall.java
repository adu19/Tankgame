/**
 * Wall.java 
 * 
 * The Wall class keeps track of two types of walls: breakable and non-breakable.
 * Walls explode when hit by a bullet.
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: Netbeans 8.2
 */
package tankgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;

public class Wall extends GameObject {
    boolean breakable;
    private int respawnCD;    // Breakable walls can respawn.
    private final String soundFileName;           // Explosion sound.
    Sound sound;    
    
    public Wall(Image img, int x, int y, boolean weak) {
        super(img, x, y, 0);
        respawnCD = 0;
        breakable = weak;   // Breakable if a wall is weak.
	this.width = img.getWidth(null);
	this.height = img.getHeight(null);
	boom = false; 
        this.soundFileName = "Resources/Explosion_small.wav";
        this.sound = new Sound(2, soundFileName);
    }
    
    /**
     * 
     * @return True if respawn cooldown != 0. False if respawn cd is 0.
     */
    public boolean isRespawning() {
        return (respawnCD != 0);
    }
    
    public void setCooldown(int cd) {
        this.respawnCD = cd;
    }
    
    public int getCooldown() {
        return this.respawnCD;
    }
    
    public String getSoundFileName() {
        return this.soundFileName;
    }
    
    public void addExplosion() {
        TankGame.explosions.add(new Explosions(x, y, TankGame.smallExplosion));
    }
    
    @Override
    public void draw(ImageObserver obs, Graphics2D g2d) {
        // Wall doesn't explode if it isn't respawning.
	if(respawnCD == 0) {
            g2d.drawImage(img, x, y, obs);
            this.boom = false;
            
            // Explodes upon being hit by a bullet (which resets spawn time).
            if(this.boom == true) {
                addExplosion();
            }
	}
        else{
            respawnCD--;
	}        
    }
    
    public void update() {
        
    }
}
