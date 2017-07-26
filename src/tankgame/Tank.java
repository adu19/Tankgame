/**
 * Tank.java
 * 
 * The tank class keeps track of a tank's data such as spawn location, controls, etc. 
 * It also keeps track of a tank's bullets and life using arrayLists. 
 * 
 * Methods from Airstrike.PlayerPlane.java
 * Modified to work for Tank Wars.
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: Netbeans 8.2
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
    private int centerX = 20, centerY = 18;     // Distance to center of tank image. 
    private int angle = 0, fireCooldown, fireRate;       
    
    // Command keys.
    private int leftKey, rightKey, upKey, downKey, fireKey;    
    private boolean moveLeft, moveRight, moveUp, moveDown;

    private Image normalBullet, fastBullet, heartImg; 
    private ArrayList<Bullet> myBulletList;
    private ArrayList<GameObject> hearts;
    
    // Used for explosions. 
    private String soundFileName;
    private Sound sound;
    
    public Tank(Image img, int lives, int x, int y, int speed, int left, int right, 
            int up, int down, int fire, int ID) {
        super(img, x, y, speed);
        this.ID = ID;
        this.health = 1;
        this.lives = lives;
        this.fireRate = 65;
        
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
        
        // Player 1's tank will spawn at the top left corner.
        if(this.ID == 1) {
            for(int i = 0; i < this.lives; i++) {
                hearts.add(new GameObject(heartImg, i * 32, 545, 0) {});
            }
        }
        // Player 2's tank will spawn at the bottom right corner.
        else if(this.ID == 2) {
            for(int i = 0; i < this.lives; i++) {
                hearts.add(new GameObject(heartImg, 865 - (i * 32), 545, 0) {});
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
    
    /**
     * The addExplosion() method is called once a tank hits 0 health.
     * An explosion will occur at the tank's location upon death. 
     */
    public void addExplosion() {
        TankGame.explosions.add(new Explosions(x, y, TankGame.bigExplosion));
    }
    
    public ArrayList<Bullet> getBulletList(){
         return this.myBulletList;
    } 
    
    /**
     * addHeart() is called when a player receives a red life power up.
     * An additional heart will be drawn to represent the additional life. 
     */
    public void addHeart() {
        if(this.ID == 1) {
            hearts.add(new GameObject(heartImg, hearts.size() * 32, 545, 0) {});            
        }
        else if(this.ID == 2) { 
            hearts.add(new GameObject(heartImg, 865 - (hearts.size() * 32), 545, 0) {});
        }
    }

    /**
     * removeHeart() is called once a tank's health reaches 0.
     */
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
     * @param obs Tank. 
     * @param g2d Tank drawn on g2d. 
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
            double rotationRequired = Math.toRadians(-6 * angle);   // had to use -6 instead of 6 to work.       
            
            AffineTransform at = AffineTransform.getTranslateInstance(x, y);
            at.rotate(rotationRequired, bimg.getWidth() / 2, bimg.getHeight() / 2);        
            g2d.drawImage(bimg, at, obs);
        }
        
        // Case 3: Tank has no health, but still has lives. 
        else if(boom == true && lives > 0 && spawnCD == 0) {
            // Remove a life and any bullet powerup. Set respawn timer.
            spawnCD = 150;
            bulletPowerUp = false;
            lives--;
            
            // Respawn only if tank still has lives. 
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
    
    /**
     * updatePosition() changes the tank's location depending on the 
     * angle and movement key pressed. 
     * 
     * Left and Right movement keys rotate the tank.
     * Up and Down movement keys move the tank forward and backward. 
     * 
     * Cited from: http://www.java-gaming.org/index.php?topic=29407.0
     * Modified to work for Tank Wars.
     */
    public void updatePosition(){
        // Rotates tank left (counter-clockwise).
        if (moveLeft == true) {
            angle += 1;
	}
        
        // Rotates tank right (clockwise).
	if (moveRight == true) {
            angle -= 1;
	}
                
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

