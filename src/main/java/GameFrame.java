import javax.swing.*;

public class GameFrame extends JFrame {

    // Constructor for our GameFrame
    GameFrame(){
        // We create a new panel
        GamePanel panel = new GamePanel();
        // And append it to this GameFrame
        this.add(panel);

        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

}
