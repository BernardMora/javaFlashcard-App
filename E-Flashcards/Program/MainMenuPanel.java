import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuPanel extends JPanel {
    private JFrame frame;
    public static void main(String[] args) {
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.go();
    }

    public void go() {
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainMenuPanel = new JPanel(new BorderLayout());

        setLayout(new GridLayout(4, 1)); // Set the layout as a grid with 4 rows and 1 column

        //create a label "E-Flashcards" as the title of the main menu
        JLabel titleLabel = new JLabel("E-Flashcards");
        titleLabel.setFont(new Font("sanserif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainMenuPanel.add(titleLabel, BorderLayout.NORTH);

        // add an image and insert it into the panel
        ImageIcon image = new ImageIcon("images/flashcards.png");
        // resize the image
        Image img = image.getImage();
        Image newImg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        image = new ImageIcon(newImg);
        
        JLabel imageLabel = new JLabel(image);
        mainMenuPanel.add(imageLabel, BorderLayout.CENTER);

        //create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // create a button "Build Flashcards" and add it to the panel
        JButton buildButton = new JButton("Build Flashcards");
        buildButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action to perform when the "Build Flashcards" button is clicked
                frame.dispose();
                QuizCardBuilder quizCardBuilder = new QuizCardBuilder();
                quizCardBuilder.go();
            }
        });
        buttonPanel.add(buildButton);

        JButton showButton = new JButton("Show Flashcards");
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action to perform when the "Show Flashcards" button is clicked
                frame.dispose();
                QuizCardPlayerScore quizCardPlayer = new QuizCardPlayerScore();
                quizCardPlayer.go();
            }
        });
        buttonPanel.add(showButton);
        mainMenuPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(mainMenuPanel);
        frame.pack();
        frame.setLocationRelativeTo(showButton);
        frame.setVisible(true);
    }


}
