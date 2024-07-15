import java.awt.*;
import java.awt.event.*;
import java.util.random.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    

    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //Snake
    Tile snakeHead;
    //Food
    Tile food;
    // Random
    Random random;

    //Game logic
    Timer gammeLoop;
    int velocityX;
    int velocityY;
    boolean Again = false;
    boolean  gameOver = false;
    ArrayList <Tile> snakeBody;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();
        food = new Tile(10, 10);
        random = new Random();
        placefood();

        velocityX = 0;
        velocityY = 0;

        gammeLoop = new Timer(110, this);
        gammeLoop.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Grid
        for (int i = 0; i < boardWidth/tileSize ; i ++ ) {
            g.drawLine(i*tileSize, 0,i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        // food
        g.setColor(Color.red);
        g.fill3DRect(food.x *tileSize, food.y * tileSize, tileSize, tileSize,true);

        // Snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Snake body
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }

        //Score 
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size())  , tileSize * 10, tileSize * 12);
            // g.drawString("Press Enter To Play Again!"  , tileSize * 8, tileSize * 13);
        }
        else {
            g.drawString("Score:" + String.valueOf(snakeBody.size()), tileSize - 16 , tileSize);
        }

    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void placefood (){
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);
    }
    
    public void move() {
        // ead food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y)); 
            placefood();
        } 

        for (int i = snakeBody.size()-1; i>=0 ; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prervSnakePart = snakeBody.get(i-1);
                snakePart.x = prervSnakePart.x;
                snakePart.y = prervSnakePart.y;
            }
        }

        // Snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            //collide with the snake haed
            if (collision(snakeHead, snakePart)) {
                gameOver = true;
            }
        }
        if (snakeHead.x *tileSize < 0 || snakeHead.x * tileSize > boardWidth || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight) {
            gameOver = true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver == true) {
            gammeLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ( (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if ( (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if ( (e.getKeyCode() == KeyEvent.VK_LEFT|| e.getKeyCode() == KeyEvent.VK_A) && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if ( (e.getKeyCode() == KeyEvent.VK_RIGHT|| e.getKeyCode() == KeyEvent.VK_D) && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }

        if (gameOver == true) {
            if ( e.getKeyCode() == KeyEvent.VK_ENTER) {
                // gammeLoop = new Timer(100, this);
                // gammeLoop.start();

            }
        }

    }

    // do not need
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
