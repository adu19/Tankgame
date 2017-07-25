/**
 * TankGame.java
 * 
 * Description: 
 * Tank Wars is a two player tank shooter game. Each player starts with 3 lives.
 * Lives are represented by hearts at the bottom of the game window. 
 * Tanks have 1 health per life and die upon being hit by a bullet (one-hit, dead). 
 * The game ends once either player runs out of lives. 
 * 
 * Controls:
 * Player 1 : WASD movement, SPACE to fire.
 * Player 2 : Arrow key movement (number pad arrow keys also work), ENTER to fire. 
 * 
 * Power Ups: 
 * Green bullet power ups and red health power ups will spawn during the game. 
 * Green bullets allow players to fire faster traveling bullets. 
 * 
 * Red life power ups grant 1 additional life. Only 2 red life power ups
 * will spawn over the duration of the game. 
 * 
 * @author Albert Du
 * @date July 25, 2017
 * IDE: NetBeans 8.2
 */
package tankgame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;

public class TankGame extends JApplet implements Runnable{
    private Thread thread;
    public static GameEvents gameEvent1, gameEvent2;    // Will observe the two players' tanks.   
    public static Tank player1, player2;  
    public int playerID1 = 1, playerID2 = 2, buffTimer = 1;
    private String winMsg, loseMsg; // Display on game over screen. 
    private boolean cleared;        // Determines if game window cleared before drawing game over screen.
    
    Image background, tankP1, tankP2, normalBullet, fastBullet, strongWall, weakWall, end,
                    healthBuff;
    BufferedImage life;
    
    // Stores explosion animations. 
    static Image[] bigExplosion = new Image[7];    
    static Image[] smallExplosion = new Image[6];
    
    private BufferedImage bimg;
    Graphics2D g2;
 
    boolean gameOver = false;  
    CollisionDetector CD;
    Sound sp;  
    public InputStream wallMap; // Game map drawn from a text file.
    
    static ArrayList<Explosions> explosions = new ArrayList<Explosions>(100000);
    static ArrayList<Wall> wall = new ArrayList<Wall>();
    static ArrayList<PowerUp> powerUps = new ArrayList<PowerUp>();  
    
    int width = 900, height = 605;  
    private Dimension contentWindowSize;   // Real size of the content window. 
    
    @Override
    public void init() {
        this.setFocusable(true);
        setBackground(Color.gray);
        
        try {
            background = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/Background.bmp"));
            tankP1 = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Tank1.png")));
            tankP2 = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Tank2.png")));
            strongWall = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Wall1.png")));
            weakWall = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/Wall2.png"));
            wallMap = this.getClass().getClassLoader().getResourceAsStream("Resources/WallMap.txt");
            end = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/Title.bmp"));
            normalBullet = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Rocket.png")));            
            fastBullet = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/FastBullet.png"));
            healthBuff = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/HealthBuff.png"));
	    life = ImageIO.read(this.getClass().getClassLoader().getResource("Resources/Life.png"));            
            
            bigExplosion[0] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large1.png")));
            bigExplosion[1] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large2.png")));
            bigExplosion[2] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large3.png")));
            bigExplosion[3] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large4.png"))); 
            bigExplosion[4] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large5.png")));  
            bigExplosion[5] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large6.png")));
            bigExplosion[6] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_large7.png"))); 
            
            smallExplosion[0] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_small1.png")));
            smallExplosion[1] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_small2.png")));
            smallExplosion[2] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_small3.png")));
            smallExplosion[3] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_small4.png")));
            smallExplosion[4] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_small5.png")));
            smallExplosion[5] = ImageIO.read(this.getClass().getClassLoader().getResource(("Resources/Explosion_small6.png")));
            
            // Both tanks have 3 lives and a movement speed of 4. 
            // WASD movement. Space fire.
            player1 = new Tank(tankP1, 3, 35, 35, 4, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_SPACE, playerID1);

            // Arrow movement. Enter fire.
            player2 = new Tank(tankP2, 3, 795, 475, 4, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER, playerID2);
         
            if(player1 == null)
                System.out.println("Player 1 is null");   
            if(player2 == null)
                System.out.println("Player 2 is null");
        
            // Observe the two tanks for drawing. 
            gameEvent1 = new GameEvents();
            gameEvent2 = new GameEvents();
            gameEvent1.addObserver(player1);
            gameEvent2.addObserver(player2); 
            
            // Key listener for the two tanks. 
            KeyControl key1 = new KeyControl(gameEvent1);
            KeyControl key2 = new KeyControl(gameEvent2);
            addKeyListener(key1);  
            addKeyListener(key2);
            
            CD = new CollisionDetector(gameEvent1, gameEvent2);
            sp = new Sound(1, "Resources/Music.mid");               
        }
        catch (Exception e) {
            System.out.println("Init: RESOURCE NOT FOUND");
        }
        
        mapWalls();
    }
    
    @Override 
    /**
     * Start() and run() cited from Airstrike.GameWorld.java
     */
    public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
    
    @Override  
    public void run() {
        Thread me = Thread.currentThread();
        
        // Repaint every 25ms.
        while(thread == me) {
            repaint();
 
            try {
                thread.sleep(25);
            } 
            catch (InterruptedException e) {
                break;
            } 
        }               
    }    
    
    @Override
    public void paint(Graphics g) {
        if(bimg == null) {
            Dimension windowSize = getSize();   
            bimg = (BufferedImage) createImage(windowSize.width, windowSize.height);
            g2 = bimg.createGraphics();
        }
        
        // Continues drawing the game until it ends. 
        if(gameOver == false) {
            drawDemo();
            g.drawImage(bimg, 0, 0, this);  // this is imageObserver.
        }
        else {
            if(cleared == false) {
                g.clearRect(0, 0, getWidth(), getHeight());
                cleared = true;
            }
            
            g.setFont(new Font("TimesRoman", Font.BOLD, 28));
            g.drawString(winMsg, 365, 245);
            g.drawString(loseMsg, 365, 295);            
        }
    }
    
    public void drawDemo() {

        drawBackGroundWithTileImage();        

        // Collision detection between game objects. 
        CD.playerVSPlayer(player1, player2);
        CD.playerVSBullet(player1, player2);
        CD.playerVSWall(player1, player2);
        CD.playerVSPowerUp(player1, player2);
        CD.bulletVSWall(player1, player2);
        
        
        // Spawns bullet power ups in intervals indefinitely. 
        if(buffTimer % 700 == 0) {           
            powerUps.add(new PowerUp(fastBullet, 350, 250, 0, 1));                          
        }          
        if((buffTimer % 700) == 0 && (buffTimer >= 2800)) {
            powerUps.add(new PowerUp(fastBullet, 435, 290, 0, 1));
        }
        
        // Only spawn 2 health powerups for the entire game.
        if((buffTimer % 1200 == 0) && (buffTimer <= 1201)) {
            powerUps.add(new PowerUp(healthBuff, 520, 250, 0, 2)); 
        }        
        if((buffTimer % 2400 == 0) && (buffTimer > 2399) && (buffTimer < 2501)) {
            powerUps.add(new PowerUp(healthBuff, 435, 225, 0, 2));
        }
        
        // Draw bullet and health powerUps. 
        if(!powerUps.isEmpty()) {
            
            for(int i = 0; i < powerUps.size(); i++) {
                if(!powerUps.get(i).showing()) {
                    powerUps.remove(i--);
                }
                else {
                    powerUps.get(i).draw(this, g2);
                }
            }
        }
        
        if (!wall.isEmpty()) {
            for (int i = 0; i < wall.size(); i++) {
                wall.get(i).draw(this, g2);
            }
        }     
        
        // Draw lives for both players.
        if(!player1.lose() && !player1.getHearts().isEmpty()) {
            for(int i = 0; i < player1.getHearts().size(); i++) {
                player1.getHearts().get(i).draw(this, g2);
            }
        }
        
        if(!player2.lose() && !player2.getHearts().isEmpty()) {
            for(int i = 0; i < player2.getHearts().size(); i++) {
                player2.getHearts().get(i).draw(this, g2);
            }
        }
        // Updates the bullet list for both players. 
        if(!player1.getBulletList().isEmpty()) {
            for(int i = 0; i < player1.getBulletList().size(); i++) {
                if(player1.getBulletList().get(i).getShow()) {
                    player1.getBulletList().get(i).draw(this, g2);
                    player1.getBulletList().get(i).update();    
                }
                else {
                    player1.getBulletList().remove(i);
                }
            }
        }

        if(!player2.getBulletList().isEmpty()) {
            for(int i = 0; i < player2.getBulletList().size(); i++) {
                if(player2.getBulletList().get(i).getShow()) {
                    player2.getBulletList().get(i).draw(this, g2);
                    player2.getBulletList().get(i).update();
                }
                else {
                    player2.getBulletList().remove(i);                  
                }
            }
        } 

        // Redraws the tanks after they've been moved. 
        player1.draw(this, g2);
        player1.updatePosition();
        player2.draw(this, g2);
        player2.updatePosition();
        
        // Explosion removed from arrayList after it's done. Won't be drawn
        // after being removed. 
        if(!explosions.isEmpty()) {
            for(int i = 0; i < explosions.size(); i++) {
                if(explosions.get(i).getFinished()) {
                    explosions.remove(i);
                    i--;
                }
                else{
                    explosions.get(i).update();
                }                
            }
        }
        
        if(!explosions.isEmpty()) {
            for(int i = 0; i < explosions.size(); i++){
                explosions.get(i).draw(this, g2);
            }
        }
        
        // Draws mini map on the top right corner of the game window.  
        Image miniMap = bimg.getScaledInstance(300, 300, Image.SCALE_FAST);
        g2.drawImage(miniMap, (contentWindowSize.width) - 180, 3, 190, 190, this);

        // Winner/loser messages when either player is out of lives.
        if(player1.lose()) {
            gameOver = true;
            winMsg = "Player 2 wins!";
            loseMsg = "Player 1 loses!";
        }

        else if(player2.lose()) {
            gameOver = true;
            winMsg = "Player 1 wins!";
            loseMsg = "Player 2 loses!";            
        } 
        
        buffTimer++;        
    }    

    private void drawBackGroundWithTileImage() {
        int tileWidth = background.getWidth(this);  // 320
        int tileHeight = background.getHeight(this);// 240
        
        int NumberX = 3;  
        int NumberY = 3;  
        
        // Draws the background 9 times, 3x3. 
        for(int i = 0; i < NumberY; i++) {
            for(int j = 0; j < NumberX; j++) {
                g2.drawImage(background, j * tileWidth, i * tileHeight, 
                                tileWidth, tileHeight, this);
            }
        }
    }

    /**
     * The mapWalls() method reads a text file to draw walls on the game window.
     * Text file for this game is WallMap.txt.
     * 0 - Draw nothing. 
     * 1 - Draw strong, unbreakable walls.
     * 2 - Draw weak, breakable walls. 
     */
    private void mapWalls() {
        BufferedReader line = new BufferedReader(new InputStreamReader(wallMap));
        String currentLine;
        int position = 0;
        
        try {
            // Reads all characters on each line and map walls accordingly. 
            while ((currentLine = line.readLine()) != null) {
                for(int i = 0; i < currentLine.length(); i++) {
                    if(currentLine.charAt(i) == '1') {
                        wall.add(new Wall(strongWall, (position % 28) * 32, (position / 28) * 32, false));
                    }
                    else if(currentLine.charAt(i) == '2') {
   			wall.add(new Wall(weakWall, (position % 28) * 32, (position / 28) * 32, true));                        
                    }
                    
                    position++;
                }
            }
        }
        catch (Exception e) {
            System.out.println("Walls: couldn't build");
        }
    }
    
    public static void main(String[] args) {
        final TankGame tankgame = new TankGame(); 
        tankgame.init();
        
        // Window for the game.
        JFrame frame = new JFrame("Tank Wars");  
        frame.addWindowListener(new WindowAdapter() {           
        });
        
        // Puts tankgame in the contentpane of the game window.
        frame.getContentPane().add("Center", tankgame);         
        frame.pack(); 
        frame.setSize(new Dimension(tankgame.width, tankgame.height));        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
        
        // Get content window size
        // Cited from: https://stackoverflow.com/questions/13474795/get-the-real-size-of-a-jframe-content
        tankgame.contentWindowSize = frame.getContentPane().getSize();
        
        // Centers the game window on the computer's screen.
        // Cited from: http://www.java-made-easy.com/java-jframe.html        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (screenSize.width - w)/2;
        int y = (screenSize.height - h)/2;       
        frame.setLocation(x, y);
        
        tankgame.start();
    }    
}


