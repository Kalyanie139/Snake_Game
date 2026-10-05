package sgame;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.*; //Since colour Class is in this package
import java.awt.event.*;//Has Action listener interface which is used to track actions like click or key press

//We want thhe Board to be the component of Swing for that reason we made it extend Panel class
//Panel is components of frame

public class Board extends JPanel implements ActionListener {
    // Creating 3 object for images by using Image an inbuilt class in Java
    private Image apple;
    private Image dot;
    private Image head;

    // variable that is used in collision case to make it false
    public boolean inGame = true;

    // array for dots placement
    private final int ALL_DOTS = 2500;
    private final int DOT_SIZE = 20;
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    // Random number generation ke liye Random position ka variable
    private final int RANDOM_POSITION = 25; // Ensuring apple is in frame (Width/Dot sie)

    // Variable to decide location of apple
    private int apple_x;
    private int apple_y;

    private int dots;
    private Timer timer;

    // Restart button
    private JButton restartButton;

    // Variables for movement
    private boolean left_direction = false;
    private boolean right_direction = true;// Bydefault we are eeping right direction
    private boolean up_direction = false;
    private boolean down_direction = false;

    public Board() {

        addKeyListener(new TAdapter());// Jab class ka constructor call hoga tabhi keyListener ka fuction
                                       // addkeylistener invoke hoga to handle key interupt;
        setBackground(Color.BLACK);
        // Immediately game shuru hone par frame focus pe lane ke liye
        setFocusable(true);

        // Using null layout so that we can position restart button manually
        setLayout(null);

        loadImages();

        // Creating Restart Button
        restartButton = new JButton("RESTART GAME");
        restartButton.setSize(160, 40);
        restartButton.setVisible(false);

        // When button is clicked, restartGame() function will be called
        restartButton.addActionListener(e -> restartGame());

        add(restartButton);

        initGame();
    }

    // Function to Load images these images run one after the other
    public void loadImages() {

        apple = new ImageIcon(
                getClass().getResource("/sgame/icons/apple.png")).getImage();

        dot = new ImageIcon(
                getClass().getResource("/sgame/icons/dot.png")).getImage();

        head = new ImageIcon(
                getClass().getResource("/sgame/icons/head.png")).getImage();
    }

    // Function to initialize game
    public void initGame() {
        dots = 3;
        inGame = true;

        // Resetting direction when game starts/restarts
        left_direction = false;
        right_direction = true;
        up_direction = false;
        down_direction = false;

        // This loop insures where these dots will be placed
        for (int i = 0; i < dots; i++) {
            y[i] = 100;
            x[i] = 100 - i * DOT_SIZE; // Multiplying dotsize with x coodinate to ensure different x cordinate in
                                       // allignment everytime
        }

        // Since when game starts at that time we also have to display apple we will
        // write function to display that apple
        LocateApple();

        timer = new Timer(140, this);
        timer.start();

        // Hide restart button while game is running
        restartButton.setVisible(false);

        // Give focus back to Board so arrow keys work
        requestFocusInWindow();
    }

    // Function to restart game
    public void restartGame() {

        // Stop old timer before creating a new one
        if (timer != null) {
            timer.stop();
        }

        // Initialize everything again
        initGame();

        // Refresh the frame
        repaint();
    }

    // Actual code for Locate apple
    public void LocateApple() {
        // Generating random position by using both x and y coordinate.
        int r = (int) (Math.random() * RANDOM_POSITION); // Typecasting random position gererated bu using math class
                                                         // function which gives number of type float
        apple_x = r * (DOT_SIZE);
        // We multiply by dotsize Snake and apple can perfectly overlap Collision logic
        // becomes simple
        r = (int) (Math.random() * RANDOM_POSITION);
        apple_y = r * (DOT_SIZE);

    }

    // using method (paintcomponent) from graphcics class to display images on the
    // frame;
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Calling parent class constructor;
        draw(g);
    }

    // using draw function which is used by paintcomponent to display images
    public void draw(Graphics g) {
        if (inGame) {
            // To display Apple on frame
            g.drawImage(apple, apple_x, apple_y, DOT_SIZE, DOT_SIZE, this);

            // Displays dots
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], DOT_SIZE, DOT_SIZE, this);
                } else {
                    g.drawImage(dot, x[i], y[i], DOT_SIZE, DOT_SIZE, this);
                }
            }
            Toolkit.getDefaultToolkit().sync();// Ensures it starts from default
        } else {
            gameover(g);
        }

    }

    // GameOverFunction
    public void gameover(Graphics g) {

        String msg = "GAME OVER"; // Message jo screen par display karna hai
        // Font set kar rahe hai taaki text clearly visible ho
        Font font = new Font("SANS_SERIF", Font.BOLD, 100);
        // FontMetrics ka use text ki exact width nikalne ke liye hota hai
        FontMetrics metrices = getFontMetrics(font);
        // Text ka colour set kar rahe hai (Background black hai isliye white)
        g.setColor(Color.WHITE);
        // Graphics object ko font bata rahe hai
        g.setFont(font);
        // Panel ki actual width le rahe hai aur text ki width minus karke
        // 2 se divide kar rahe hai taaki text horizontally center ho
        int x = (getWidth() - metrices.stringWidth(msg)) / 2;
        // Panel ki height ka half le rahe hai taaki vertically center ho
        int y = getHeight() / 2;
        // Finally message ko calculated position par draw kar rahe hai
        g.drawString(msg, x, y);

        // Position restart button below GAME OVER text
        int buttonX = (getWidth() - restartButton.getWidth()) / 2;
        int buttonY = y + 30;

        restartButton.setLocation(buttonX, buttonY);
        restartButton.setVisible(true);
    }

    // Code for move function
    public void move() {
        for (int i = dots - 1; i > 0; i--) {
            // The loop shifts every body part forward, and the head moves last.
            x[i] = x[i - 1];// Shifting previous dot on postion of head,and same for all preceding dot they
                            // occupy position occupied by next dot
            y[i] = y[i - 1];
        }

        // Now checking which direction is true and are accordingly moving snake
        if (left_direction) {
            x[0] = x[0] - DOT_SIZE; // Left mein coordinate decrese karte hai so '-'
        }
        if (right_direction) {
            x[0] = x[0] + DOT_SIZE; // Right mein coordinate increse karte hai so '+'
        }
        if (up_direction) {
            y[0] = y[0] - DOT_SIZE; // Up mein coordinate decrese karte hai for Frames in java so '-'
        }
        if (down_direction) {
            y[0] = y[0] + DOT_SIZE; // Down mein coordinate decrese karte hai so '-'
        }
    }

    // Function that increase the snake size when snake eats apple
    public void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++; // increase snake length
            // Immediately jaise dot increent honge draw ka loop ek aur baar chalega and
            // willl generate new dot
            // To make sure it takes position of Tail this is our effort
            x[dots - 1] = x[dots - 2];
            y[dots - 1] = y[dots - 2];

            LocateApple(); // place new apple
        }
    }

    // Overriding Abstract method of interface Action Listener
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();// It moves the snake
            checkApple();// Function that checks if snake has eaten the apple or not;
            checkCollision();// To check it Snake collides with itself
        }
        repaint();// ensures frame refreshes
    }

    public void checkCollision() {
        for (int i = dots; i > 0; i--) {
            // Snake collides with itself
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
            // Collides with frame edges
            if (x[0] > 500)
                inGame = false;
            if (x[0] < 0)
                inGame = false;
            if (y[0] > 500)
                inGame = false;
            if (y[0] < 0)
                inGame = false;

            if (!inGame)
                timer.stop();// Stop the timer if collision

        }
    }

    // Now we are writing one more class that will ensure that Key(interupt) are
    // addressed in proper way
    public class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // Assigning keycode to variable key
            int key = e.getKeyCode();

            // Immediately left mein mat modo if its travelling in right;
            if (key == KeyEvent.VK_LEFT && (!right_direction)) {
                left_direction = true;
                up_direction = false;
                down_direction = false;
            }

            if (key == KeyEvent.VK_RIGHT && (!left_direction)) {
                right_direction = true;
                up_direction = false;
                down_direction = false;
            }

            if (key == KeyEvent.VK_UP && (!down_direction)) {
                up_direction = true;
                right_direction = false;
                left_direction = false;
            }

            if (key == KeyEvent.VK_DOWN && (!up_direction)) {
                down_direction = true;
                right_direction = false;
                left_direction = false;
            }
        }
    }
}