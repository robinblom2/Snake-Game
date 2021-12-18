import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;   // How many objects can we fit on the screen!?
    int DELAY = 75;

    // X-coordinates of all the body-parts of the snake.
    final int x[] = new int[GAME_UNITS];
    // Y-coordinates of all the body-parts of the snake.
    final int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'R';

    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){

        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    // Method to start the game
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

        String fonts[] =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (int i = 0; i < fonts.length; i++) {
            System.out.println(fonts[i]);
        }

    }


    public void paintComponent(Graphics graphic){
        super.paintComponent(graphic);
        draw(graphic);
    }

    // Draws the components
    public void draw(Graphics graphic){
        if(running){

            // Graphics for the apple
            graphic.setColor(Color.red);
            graphic.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Graphics for the head and body of the snake
            for(int i = 0; i < bodyParts; i++){
                // Head of the snake
                if(i == 0){
                    graphic.setColor(Color.green);
                    graphic.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                // Tail of the snake
                else if(i == bodyParts - 1){
                    graphic.setColor(new Color(40, 79, 39));
                    graphic.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                // Body of the snake
                else{
                    graphic.setColor(new Color(92, 197, 10));
                    graphic.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Display High Score
            displayHighScore(graphic);
        }
        else{
            gameOver(graphic);
        }
    }

    // Method to populate the game with an apple. Generates the coordinates for a new apple whenever it's called.
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    // Moves the snake by iterating through the bodyparts of the snake.
    public void move(){
        for(int i = bodyParts; i > 0; i--){
            // Shift the bodyparts of the snake around
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        // Change the direction the snake is headed.
        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    // Check to see if the snake eats an apple
    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }

        // Depending on the amount of apples eaten, the pace of the game increases
        switch(applesEaten){
            case 10:
                DELAY = 70;
                setTimer(DELAY);
                break;
            case 20:
                DELAY = 65;
                setTimer(DELAY);
                break;
            case 30:
                DELAY = 60;
                setTimer(DELAY);
                break;
            case 40:
                DELAY = 55;
                setTimer(DELAY);
                break;
            case 50:
                DELAY = 50;
                setTimer(DELAY);
                break;
            case 60:
                DELAY = 45;
                setTimer(DELAY);
                break;
        }
    }

    public void checkCollisions(){
        for(int i = bodyParts; i > 0; i--){
            // Checks if head collides with the body
            if((x[0] == x[i]) && y[0] == y[i]){
                running = false;
            }
        }
        // Checks if head touches left border
        if(x[0] < 0){
            running = false;
        }
        // Check if head touches right border
        if(x[0] > SCREEN_WIDTH){
            running = false;
        }
        // Check if head touches top border
        if(y[0] < 0){
            running = false;
        }
        // Check if head touches bottom border
        if(y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics graphic){
        // Highscore text
        displayHighScore(graphic);

        // Game over text
        graphic.setColor(Color.red);
        graphic.setFont(new Font("Pristina", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(graphic.getFont());
        graphic.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2, SCREEN_HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // If the game is running
        if(running){
            // Move the snake
            move();
            // Check if we run into apples
            checkApple();
            // Check for collisions
            checkCollisions();
        }
        repaint();
    }

    // Method for displaying the Highscore Text
    public void displayHighScore(Graphics graphic){
        graphic.setColor(Color.white);
        graphic.setFont(new Font("Tahoma", Font.BOLD, 25));
        FontMetrics metrics = getFontMetrics(graphic.getFont());

        // Puts the text in the center of the screen-width
        graphic.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2, graphic.getFont().getSize());
    }

    // Method to set the delay
    public void setTimer(int delay){
        timer.stop();
        timer = new Timer(delay, this);
        System.out.println(delay);
        System.out.println(DELAY);
        timer.start();
    }

    // Keyboard controls
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent event){

            switch(event.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){   // We limit the user to a 90deg turn.
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){   // We limit the user to a 90deg turn.
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){   // We limit the user to a 90deg turn.
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){   // We limit the user to a 90deg turn.
                        direction = 'D';
                    }
                    break;
            }
        }
    }

}
