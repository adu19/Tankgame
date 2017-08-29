/**
 * CollisionDetector.java 
 * 
 * The CollisionDetector class detects collision between game objects using rectangles. 
 * Rectangles are created around game objects. 
 * Collisions occur when two rectangles intersect. 
 * Subsequent actions are determined by which type of objects collided.
 * 
 * There are five types of collisions in Tank Wars: 
 *              Player and Player
 *              Player and Bullet
 *              Player and Power Up
 *              Player and Wall 
 *              Bullet and Wall
 * 
 * The method structure and logic is from Airstrike.CollisionDetector.java
 * Modified to work for Tank Wars.
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2 
 */

package tankgame;

import java.awt.Rectangle;

public class CollisionDetector {
    GameEvents gameEvent1, gameEvent2;
    
    public CollisionDetector(GameEvents ge1, GameEvents ge2){
        this.gameEvent1 = ge1;
        this.gameEvent2 = ge2;
    }     
    
    /**
     * Checks for collision between tanks.
     * @param t1 Player 1 tank.
     * @param t2 Player 2 tank.
     */
    public void playerVSPlayer(Tank t1, Tank t2) {
        // Tank1 (x, y)
        int tank1X = t1.getX();
        int tank1Y = t1.getY();
 
        // Tank2 (x, y)
        int tank2X = t2.getX();
        int tank2Y = t2.getY();
        
        Rectangle t1Box = new Rectangle(tank1X, tank1Y, t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(tank2X, tank2Y, t2.getWidth(), t2.getHeight());
        
        // If there's an intersection between tanks, update the tank positions.
        if(t1Box.intersects(t2Box) && !t1.isRespawning() && !t2.isRespawning()) {
            // Set new X position for both tanks.
            if(tank1X > tank2X) {
                tank1X = (tank1X + (t1.getSpeed() * 2));
                t1.setX(tank1X);
                
                tank2X = (tank2X - (t2.getSpeed() * 2));
                t2.setX(tank2X);
            }
            else if(tank1X < tank2X) {
                tank1X = (tank1X - (t1.getSpeed() * 2));
                t1.setX(tank1X);
                
                tank2X = (tank2X + (t2.getSpeed() * 2));
                t2.setX(tank2X);                
            }
            
            // Sets new Y position for both tanks.
            if(tank1Y > tank2Y) {
                tank1Y = (tank1Y + (t1.getSpeed() * 2));
                t1.setY(tank1Y);

                tank2Y = (tank2Y - (t1.getSpeed() * 2));
                t2.setY(tank2Y);                
            }
            else if(tank1Y < tank2Y) {
                tank1Y = (tank1Y - (t1.getSpeed() * 2));
                t1.setY(tank1Y);
                
                tank2Y = (tank2Y + (t2.getSpeed() * 2));
                t2.setY(tank2Y);                
            }
        }
    }
    
    /**
     * Checks for collision between players and bullets.
     * @param t1 Player 1 tank.
     * @param t2 Player 2 tank.
     */
    public void playerVSBullet(Tank t1, Tank t2) {
        Rectangle t1Box = new Rectangle(t1.getX(), t1.getY(), t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight()); 

        Bullet b1, b2;
        
        // Player 1 bullet vs Player 2 tank.
        // Retrieve the bullet from player 1's bullet list and check for intersection.
        for(int i = 0; i < t1.getBulletList().size(); i++) {
            b1 = t1.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b1.getX(), b1.getY(), b1.getWidth(), b1.getHeight());
            
            if(t2Box.intersects(bBox) && !t2.isRespawning()) {
                b1.show = false;    // Make bullet invisible after collision.
                t2.setHealth(t2.getHealth() - 1);
                t2.removeHeart();
            }
        } 

        // Player 2 bullet vs Player 1 tank.
        // Retrieve the bullet from player 2's bullet list and check for intersection.        
        for(int i = 0; i < t2.getBulletList().size(); i++) {
            b2 = t2.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b2.getX(), b2.getY(), b2.getWidth(), b2.getHeight());
            
            if(t1Box.intersects(bBox) && !t1.isRespawning()) {
                b2.show = false;
                t1.setHealth(t1.getHealth() - 1);
                t1.removeHeart();
            }
        }      
    }
    
    /**
     * Checks for collisions between tanks and walls.
     * @param t1 Player 1 tank.
     * @param t2 Player 2 tank.
     */
    public void playerVSWall(Tank t1, Tank t2) {
        // Tank1 (x, y)
        int tank1X = t1.getX();
        int tank1Y = t1.getY();
 
        // Tank2 (x, y)
        int tank2X = t2.getX();
        int tank2Y = t2.getY();
        
        Rectangle t1Box = new Rectangle(tank1X, tank1Y, t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(tank2X, tank2Y, t2.getWidth(), t2.getHeight());

        Wall w;
        
        // For each wall, check if there is a collision with a tank.
        for(int i = 0; i < TankGame.wall.size(); i++) {
            w = TankGame.wall.get(i);
            Rectangle wBox = new Rectangle(w.getX(), w.getY(), w.getWidth(), w.getHeight());
            
            // Player 1 vs Wall. Update player 1 tank position if it collides with a wall.
            if(t1Box.intersects(wBox) && (!w.isRespawning())) {
                if(tank1X > w.getX()) {
                    tank1X = tank1X + 3;
                    t1.setX(tank1X);
                }
                else if(tank1X < w.getX()) {
                    tank1X = tank1X - 3;
                    t1.setX(tank1X);
                }
                
                if(tank1Y > w.getY()) {
                    tank1Y = tank1Y + 3;
                    t1.setY(tank1Y);
                }
                else if(tank1Y < w.getY()) {
                    tank1Y = tank1Y - 3;
                    t1.setY(tank1Y);
                }                                
            }

            // Player 2 vs Wall. Update player 2 tank position if it collides with a wall.            
            if(t2Box.intersects(wBox) && (!w.isRespawning())) {
                if(tank2X > w.getX()) {
                    tank2X = tank2X + 3;
                    t2.setX(tank2X);
                }
                else if(tank2X < w.getX()) {
                    tank2X = tank2X - 3;
                    t2.setX(tank2X);
                }
                
                if(tank2Y > w.getY()) {
                    tank2Y = tank2Y + 3;
                    t2.setY(tank2Y);
                }
                else if(tank2Y < w.getY()) {
                    tank2Y = tank2Y - 3;
                    t2.setY(tank2Y);
                }                      
            }
        }
    }
    
    /**
     * Checks for collision between tanks and power ups.
     * 
     * Power ups disappear after collision with tanks.   
     * The power will be removed from powerUp ArrayList. See drawDemo() in TankGame.java.
     *
     * Type 1 power ups allow players to fire faster moving bullets.
     * Type 2 power ups grant 1 life.
     * 
     * @param t1 Player 1 tank.
     * @param t2 Player 2 tank.
     */
    public void playerVSPowerUp(Tank  t1, Tank t2) {        
        PowerUp pu;
        Rectangle t1Box = new Rectangle(t1.getX(), t1.getY(), t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight());
        
        for(int i = 0; i < TankGame.powerUps.size(); i++) {
            pu = TankGame.powerUps.get(i);
            Rectangle puBox = new Rectangle(pu.getX(), pu.getY(), pu.getWidth(), pu.getHeight());
            
            // Player 1 vs power up
            if(t1Box.intersects(puBox)) {
                pu.show = false;   
                
                if(pu.getType() == 1) {
                    t1.addFastBullet();                    
                }
                else if(pu.getType() == 2) {
                    t1.setLives(t1.getLives() + 1);
                    t1.addHeart();
                }
            }
            // Player 2 vs power up.
            else if(t2Box.intersects(puBox)) {
                pu.show = false;
                
                if(pu.getType() == 1) {
                    t2.addFastBullet();                    
                } 
                else if(pu.getType() == 2) {
                   t2.setLives(t2.getLives() + 1);
                   t2.addHeart();
                }
            }
        }
    }
    
    /**
     * Checks for collisions between bullets and walls.
     * 
     * Bullets disappear after colliding with a wall. Wall will explode.
     * 
     * If the wall is breakable, it will disappear after being hit by a bullet and respawn.
     * If the wall isn't breakable, it will remain intact. 
     * 
     * @param t1 Player 1 tank.
     * @param t2 Player 2 tank.
     */
    public void bulletVSWall(Tank t1, Tank t2) {
        Bullet b1, b2;
        Wall w;
        
        // Player 1 bullet vs wall.
        for(int i = 0; i < t1.getBulletList().size(); i++) {
            b1 = t1.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b1.getX(), b1.getY(), b1.getWidth(), b1.getHeight()); 
            
            // Check for collision between bullet and every wall.
            for(int j = 0; j < TankGame.wall.size(); j++) {
                w = TankGame.wall.get(j);
                Rectangle wBox = new Rectangle(w.getX(), w.getY(), w.getWidth(), w.getHeight());
            
                if((bBox.intersects(wBox)) && !w.isRespawning()) {
                    b1.show = false; 
                    w.sound.play();
                    w.addExplosion();                    
                    w.sound = new Sound(2, w.getSoundFileName());  // Explosion sound for wall.
                    
                    if(w.breakable == true) {
                        w.boom = true;                    
                        w.setCooldown(500);                        
                    };
                }
            }             
        }
        
        // Player 2 bullet vs wall.
        for(int i = 0; i < t2.getBulletList().size(); i++) {
            b2 = t2.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b2.getX(), b2.getY(), b2.getWidth(), b2.getHeight());

            for(int j = 0; j < TankGame.wall.size(); j++) {
                w = TankGame.wall.get(j);
                Rectangle wBox = new Rectangle(w.getX(), w.getY(), w.getWidth(), w.getHeight());
            
                if((bBox.intersects(wBox)) && !w.isRespawning()) {
                    b2.show = false;  
                    w.sound.play();
                    w.addExplosion(); 
                    w.sound = new Sound(2, w.getSoundFileName());

                    if(w.breakable == true) {
                        w.boom = true;                    
                        w.setCooldown(500);                        
                    };
                }
            }                      
        }      
    }
}

