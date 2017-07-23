/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Albert
 */
package tankgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.Observable;
import javax.imageio.ImageIO;

public class Tank extends GameObject{
    private boolean bulletPowerUp;     
    private int spawnCD = 0, health, lives, ID; // Start: 3 lives, 1 health per life.
    private int spawnX, spawnY;                 // Spawn point.
    private int centerX = 20, centerY = 18;     // Center of tank image. 
    private int angle = 0, fireCooldown, fireRate;       
    
    // Command keys.
    private int leftKey, rightKey, upKey, downKey, fireKey;    
    private boolean moveLeft, moveRight, moveUp, moveDown;

    private Image normalBullet, fastBullet, heartImg; 
    private ArrayList<Bullet> myBulletList;
    private ArrayList<GameObject> hearts;
    private String soundFileName;
    private Sound sound;
    
    public Tank(Image img, int lives, int x, int y, int speed, int left, int right, 
            int up, int down, int fire, int ID) {
        super(img, x, y, speed);
        this.ID = ID;
        this.health = 1;
        this.lives = lives;
        this.fireRate = 60;
        
        boom = false;
        bulletPowerUp = false;
        
        this.leftKey = left;
        this.rightKey = right;
        this.upKey = up;
        this.downKey = down;
        this.fireKey = fire;
        
        this.spawnX = x;
        this.spawnY = y;       
        
        this.myBulletList = new ArrayList<Bullet>();   
        this.hearts = new ArrayList<GameObject>();
        this.soundFileName = "Resources/Explosion_large.wav";
        this.sound = new Sound(2, soundFileName);       

        try {
            normalBullet = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Rocket.png")));           
            fastBullet = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/FastBullet.png"));
            heartImg = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Life.png")));                  
        }
        catch (Exception e) {
            System.out.println("Tank class: Resource not found");
        }
        
        if(this.ID == 1) {
            for(int i = 0; i < this.lives; i++) {
                hearts.add(new GameObject(heartImg, i * 32, 545, 0));
            }
        }
        else if(this.ID == 2) {
            for(int i = 0; i < this.lives; i++) {
                hearts.add(new GameObject(heartImg, 865 - (i * 32), 545, 0));
            }
        }
    } 
    
    public int getSpeed() {
        return this.speed;
    } 
    
    public void setHealth(int h) {
        this.health = h;
        
    }
    public int getHealth() {
        return this.health;
    }
    
    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getLives() {
        return this.lives;
    }
    
    public boolean lose() {
        if(this.lives <= 0) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void addExplosion() {
        TankGame.explosions.add(new Explosions(x, y, TankGame.bigExplosion));
    }
    
    public ArrayList<Bullet> getBulletList(){
         return this.myBulletList;
    } 

    public void addHeart() {
        if(this.ID == 1) {
            hearts.add(new GameObject(heartImg, hearts.size() * 32, 545, 0));            
        }
        else if(this.ID == 2) { 
            hearts.add(new GameObject(heartImg, 865 - (hearts.size() * 32), 545, 0));
        }
    }
    
    public void removeHeart() {
        hearts.remove(hearts.size() - 1);
    } 
    
    public ArrayList<GameObject> getHearts() {
        return this.hearts;
    }   
     
    public boolean isRespawning() {
        return (spawnCD != 0);
    }

    public void addFastBullet() {
        this.bulletPowerUp = true;
    }
    
    public void fire() {
        Bullet b;
        
        // Adds x, y offset to the tank's center to ensure 
        // bullet fires from the tank's turret from all angles.
        // Normal bullets have 7 speed. Fast bullets have 10 speed.
        if(bulletPowerUp == true) {
            b = new Bullet(fastBullet, x + centerX, y + centerY, angle, 10); 
            myBulletList.add(b);            
        }
        else {
            b = new Bullet(normalBullet, x + centerX, y + centerY, angle, 7); 
            myBulletList.add(b);            
        }
    }
    
    /**
     * Redraws tank on the game window. 
     * @param obs
     * @param g2d
     */
    @Override
    public void draw(ImageObserver obs, Graphics2D g2d) {
        fireCooldown--;    
        
        // Four scenarios for tank. 
        // Case 1: Tank is out of health. Explode whenever this happens.
        if(health <= 0) {
            boom = true;
            sound.play();
            addExplosion();
        }
        
        // Case 2: Tank isn't respawning, has health, and has lives. Keep drawing.
        if(health > 0 && lives > 0 && spawnCD == 0) {
            boom = false;
            
            // Credit to JavaTutorials101 on YouTube for rotation algorithm. 
            // https://www.youtube.com/watch?v=vHfGiTFWoc4&t=222s        
            BufferedImage bimg = (BufferedImage) img;
            double rotationRequired = Math.toRadians(-6 * angle);        
            
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            at.rotate(rotationRequired, bimg.getWidth() / 2, bimg.getHeight() / 2);        
            g2d.drawImage(bimg, at, obs);
        }
        
        // Case 3: Tank has lives, but no health. Respawn.
        else if(boom == true && lives > 0 && spawnCD == 0) {
            // add explosoion image.
            spawnCD = 150;
            bulletPowerUp = false;
            lives--;
            
            if(lives >= 0) {
                boom = false;
                
                // Reset health and spawn point.
                health = 1;
                this.angle = 0;
                this.x = spawnX;
                this.y = spawnY;
                this.sound = new Sound(2, soundFileName);                
            }
        }
        
        // Case 4: Tank is still respawning. 
        else {
            spawnCD--;
        }
    }
    
    /**
     * Update the tank's action status when a move or fire key has been pressed
     * or released. 
     * @param obs observable object
     * @param arg argument passed to notifyObservers method.
     */
    @Override
    public void update(Observable obs, Object arg) {
        GameEvents ge = (GameEvents) arg;
        
        if(ge.type == 1) {
            KeyEvent e = (KeyEvent) ge.event;
            int keyCode = e.getKeyCode();
            
            if(keyCode == leftKey) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveLeft = true;
		} 
                else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveLeft = false;
		}
            }          
            else if (keyCode == rightKey) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveRight = true;
		} 
                else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveRight = false;
                }
            }
            else if(keyCode == upKey) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveUp = true;
		} 
                else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveUp = false;
                }
            }
            else if(keyCode == downKey) {
		if (e.getID() == KeyEvent.KEY_PRESSED) {
                    moveDown = true;
		} 
                else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    moveDown = false;
                }
            }
            
            else if(keyCode == fireKey && fireCooldown <= 0) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    fireCooldown = fireRate;    // Resets the CD after firing. 
                    fire();
                }
            }
        }
    }
    
    public void updatePosition(){
        // Rotates tank left (counter-clockwise).
        if (moveLeft == true) {
            angle += 1;
	}
        
        // Rotates tank right (clockwise).
	if (moveRight == true) {
            angle -= 1;
	}
                
        // Allows tank to move NW/NE/SW/SE depending on the 
        // tank's angle and move key pressed.
	if (moveUp == true) {
            x += speed * Math.cos(Math.toRadians(6 * angle));
            y -= speed * Math.sin(Math.toRadians(6 * angle));

	}
	if (moveDown == true) {
            x -= speed * Math.cos(Math.toRadians(6 * angle));
            y += speed * Math.sin(Math.toRadians(6 * angle));
	}   
        
        // 0 <= (6 * angle) <= 360. 
	if(angle == -1) 
            angle = 59;
	else if(angle == 60) 
            angle = 0;		
    }  
}

