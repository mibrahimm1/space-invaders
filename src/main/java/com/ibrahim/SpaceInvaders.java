package com.ibrahim;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener{

    // Custom Font
    Font customFont;

    // Object for Playing Sounds
    private SoundManager soundManager;

    // Board
    int tileSize = 32 ; // 32 px
    int rows = 16 ; // 16 px
    int columns = 16 ; // 16 px
    int boardWidth = tileSize * columns ; // 512 px
    int boardHeight = tileSize * rows ; // 512 px

    //-----------------------------------SHIP------------------------------
    Image shipImg ;
    Image alienGreenImg ;
    Image alienPinkImg ;
    Image alienBlueImg ;
    Image alienAquaImg ;
    ArrayList<Image> alienImages ;

    // Ship Instance Details
    int shipWidth = tileSize * 2 ; // 64px
    int shipHeight = tileSize ; // 32px

    //--------------- Determining coordinates of ship
    int shipX = tileSize * columns / 2 - tileSize ;
    int shipY = boardHeight - tileSize * 2 ;

    //--------------- Ship Movement Speed
    int shipVelocityX = tileSize ;
    Block ship ;

    //---------------------------------------------------ALIENS--------------------
    ArrayList<Alien> aliens ;
    int alienWidth = tileSize*2 ;
    int alienHeight = tileSize ;
    int alienX = tileSize ;
    int alienY = tileSize ;

    int alienRows = 2 ;
    int alienColumns = 3 ;
    int alienCount = 0 ;
    int alienVelocityX = 1 ;

    //-------------------------------------BULLETS---------------------------------
    ArrayList<Bullet> bullets ;
    int bulletwidth = tileSize / 8 ;
    int bulletHeight = tileSize / 2 ;
    int bulletVelocityY = -10 ;


    Timer gameLoop;
    int score = 0 ;
    boolean gameOver = false ;


    SpaceInvaders() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(new Color(6, 15, 37));
        setFocusable(true);
        addKeyListener(this);

        soundManager = new SoundManager();
        soundManager.playBackgroundMusic();

        // Importing Graphics
        try (InputStream is = getClass().getResourceAsStream("/fonts/8bit16.ttf")) {
            customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(32f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        shipImg = new ImageIcon(getClass().getResource("/graphics/battle-ship.png")).getImage();
        alienGreenImg = new ImageIcon(getClass().getResource("/graphics/alien-green.png")).getImage();
        alienPinkImg = new ImageIcon(getClass().getResource("/graphics/alien-pink.png")).getImage();
        alienBlueImg = new ImageIcon(getClass().getResource("/graphics/alien-blue.png")).getImage();
        alienAquaImg = new ImageIcon(getClass().getResource("/graphics/alien-aqua.png")).getImage();

        // Adding the aliens in an array list
        alienImages = new ArrayList<>();
        alienImages.add(alienGreenImg);
        alienImages.add(alienPinkImg);
        alienImages.add(alienBlueImg);
        alienImages.add(alienAquaImg);

        // Creating Ship Instance
        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg) ;
        aliens = new ArrayList<Alien>();
        bullets = new ArrayList<Bullet>();

        // Game Loop
        gameLoop = new Timer(1000/60, this) ;
        // Creating Aliens
        createAliens() ;
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Drawing Ship
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null) ;

        // Drawing Aliens
        for (int i = 0 ; i < aliens.size() ; i++) {
            Alien alien = aliens.get(i);
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        // Drawing Bullets
        g.setColor(Color.white);
        for (int i = 0 ; i < bullets.size() ; i++) {
            Bullet bullet = bullets.get(i) ;
            if (!bullet.used) {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }

        // Drawing Score
        g.setColor(new Color(149,243,86));
        g.setFont(customFont);
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        } else {
            g.drawString(String.valueOf(score),10, 35);
        }

    }

    public void move() {

        // Alien Movements
        for (int i = 0 ; i < aliens.size(); i++) {
            Alien alien = aliens.get(i);
            if (alien.alive ) {
                alien.x += alienVelocityX ;

                if (alien.x + alien . width > boardWidth || alien.x <= 0 ) {
                    alienVelocityX *= -1 ; // Switching Direction from left to right
                    alien.x += alienVelocityX * 2 ; // Adjusting Position

                    // Moving Down One Row
                    for (int j = 0 ; j < aliens.size() ; j++) {
                        aliens.get(j).y += alienHeight ;
                    }

                }

                if (alien.y >= ship.y) {
                    gameOver = true;
                }


            }
        }

        // Bullet Movement
        for (int i = 0 ; i < bullets.size() ; i++) {
            Bullet bullet = bullets.get(i) ;
            bullet.y += bulletVelocityY ;

            for (int j = 0 ; j < aliens.size() ; j++) {
                Alien alien = aliens.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) { // Checking for Hit
                    bullet.used = true;
                    alien.alive = false ;
                    alienCount-- ;
                    score += 100 ;
                    soundManager.playHitSound();
                }
            }
        }

        while (!bullets.isEmpty() && (bullets.get(0).used || bullets.get(0).y < 0)) {
            bullets.remove(0);
        }

        if (alienCount == 0) {
            score += alienColumns * alienRows * 100 ; // Bonus Points
            alienColumns = Math.min(alienColumns + 1, columns/2 - 2) ; // Max 6
            alienRows = Math.min(alienRows + 1, rows - 6) ; // Max 10
            aliens.clear() ;
            bullets.clear() ;
            createAliens();
            soundManager.playLevelUpSound();

        }
    }

    public void createAliens() {
        Random random = new Random() ;
        for (int r = 0 ; r < alienRows ; r++) {
            for (int c = 0 ; c < alienColumns ; c++) {
                int index = random.nextInt(alienImages.size());
                Alien alien = new Alien(
                        alienX + c * alienWidth,
                        alienY + r * alienHeight,
                        alienWidth,
                        alienHeight,
                        alienImages.get(index)) ;
                aliens.add(alien);
                alienCount++;
            }
        }
    }

    public boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&      // Top Left Corner (A) doesn't reach Top Right Corner(B)
                a.x + a.width > b.x &&     // Top Right Corner (A) passes Top Left Corner(B)
                a.y < b.y + b.height &&    // Top Left Corner (A) doesn' Top reach Top Left Corner(B)
                a.y + a.height > b.y ;     // Bottom Left Corner (A) passes Top Left Corner(B)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move() ;
        repaint();
        if (gameOver) {
            gameLoop.stop() ;
            soundManager.stopBackgroundMusic();
            soundManager.playGameOverSound();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >= 0) {
            ship.x -= shipVelocityX;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth + shipVelocityX <= boardWidth) {
            ship.x += shipVelocityX;
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Bullet bullet = new Bullet(ship.x + shipWidth * 15 / 32
                    , ship.y
                    , bulletwidth
                    , bulletHeight
                    , null);
            bullets.add(bullet);
            soundManager.playFireSound();
        }
    }
}
