/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.imageio.ImageIO;

/**
 *
 * @author Albert
 */
class CollisionDetector {
    GameEvents gameEvent1, gameEvent2;
    Image life;
    
    public CollisionDetector(GameEvents ge1, GameEvents ge2){
        this.gameEvent1 = ge1;
        this.gameEvent2 = ge2;
        
        try {
	    life = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/Life.png")); 
        }
        catch(Exception e) {
            System.out.println("Collision: Resource not found");
        }
    }     

    public void playerVSPlayer(Tank t1, Tank t2) {
        // Tank1 (x, y)
        int tank1X = t1.getX();
        int tank1Y = t1.getY();
 
        // Tank2 (x, y)
        int tank2X = t2.getX();
        int tank2Y = t2.getY();
        
        Rectangle t1Box = new Rectangle(tank1X, tank1Y, t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(tank2X, tank2Y, t2.getWidth(), t2.getHeight());
        
        // If there's an intersection, update the tank positions.
        if(t1Box.intersects(t2Box) && !t1.isRespawning() && !t2.isRespawning()) {
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
    
    public void playerVSBullet(Tank t1, Tank t2) {
        Rectangle t1Box = new Rectangle(t1.getX(), t1.getY(), t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight()); 

        Bullet b1, b2;
        for(int i = 0; i < t1.getBulletList().size(); i++) {
            b1 = t1.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b1.getX(), b1.getY(), b1.getWidth(), b1.getHeight());
            
            if(t2Box.intersects(bBox) && !t2.isRespawning()) {
                b1.show = false;
                t2.setHealth(t2.getHealth() - 1);
                t2.removeHeart();
            }
        } 
        
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
    
    public void playerVSWall(Tank t1, Tank t2) {
        int tank1X = t1.getX();
        int tank1Y = t1.getY();
 
        // Tank2 (x, y)
        int tank2X = t2.getX();
        int tank2Y = t2.getY();
        
        Rectangle t1Box = new Rectangle(tank1X, tank1Y, t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(tank2X, tank2Y, t2.getWidth(), t2.getHeight());

        Wall w;
        
        for(int i = 0; i < TankGame.wall.size(); i++) {
            w = TankGame.wall.get(i);
            Rectangle wBox = new Rectangle(w.getX(), w.getY(), w.getWidth(), w.getHeight());
            
            if(t1Box.intersects(wBox) && (w.getCooldown() == 0)) {
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
            
            if(t2Box.intersects(wBox) && (w.getCooldown() == 0)) {
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
    
    public void playerVSPowerUp(Tank  t1, Tank t2) {        
        PowerUp pu;
        Rectangle t1Box = new Rectangle(t1.getX(), t1.getY(), t1.getWidth(), t1.getHeight());
        Rectangle t2Box = new Rectangle(t2.getX(), t2.getY(), t2.getWidth(), t2.getHeight());
        
        for(int i = 0; i < TankGame.powerUps.size(); i++) {
            pu = TankGame.powerUps.get(i);
            Rectangle puBox = new Rectangle(pu.getX(), pu.getY(), pu.getWidth(), pu.getHeight());
            
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
    
    public void bulletVSWall(Tank t1, Tank t2) {
        Bullet b1, b2;
        Wall w;
        
        // Get bullets.
        for(int i = 0; i < t1.getBulletList().size(); i++) {
            b1 = t1.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b1.getX(), b1.getY(), b1.getWidth(), b1.getHeight()); 
            
            for(int j = 0; j < TankGame.wall.size(); j++) {
                w = TankGame.wall.get(j);
                Rectangle wBox = new Rectangle(w.x, w.y, w.getWidth(), w.getHeight());
            
                if((bBox.intersects(wBox)) && w.getCooldown() == 0) {
                    b1.show = false;  
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
        
        for(int i = 0; i < t2.getBulletList().size(); i++) {
            b2 = t2.getBulletList().get(i);
            Rectangle bBox = new Rectangle(b2.getX(), b2.getY(), b2.getWidth(), b2.getHeight());

            for(int j = 0; j < TankGame.wall.size(); j++) {
                w = TankGame.wall.get(j);
                Rectangle wBox = new Rectangle(w.getX(), w.getY(), w.getWidth(), w.getHeight());
            
                if((bBox.intersects(wBox)) && w.getCooldown() == 0) {
                    b2.show = false;  
                    w.sound.play();
                    w.addExplosion(); 
                    w.sound = new Sound(2, w.getSoundFileName());

                    if(w.breakable == true) {
                        w.boom = true;                    
                        w.setCooldown(400);                        
                    };
                }
            }                      
        }      
    }
}

