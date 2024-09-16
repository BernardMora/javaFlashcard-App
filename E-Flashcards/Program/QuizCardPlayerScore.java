import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class QuizCardPlayerScore {
    private ArrayList<QuizCard> cardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private QuizCard currentCard;
    private MyTextArea displayQuestion;
    private MyTextArea displayAnswer;
    private JFrame frame;
    private JButton nextButton;
    private boolean confirmedAnswer = false;
    private int score;
    
    public static void main(String[] args) {
        QuizCardPlayerScore reader = new QuizCardPlayerScore();
        reader.go();
    }

    QuizCardPlayerScore() {
        // 
    }

    public void go() {
        score = 0;
        frame = new JFrame("Quiz Card Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        JLabel quesionLabel = new JLabel("Question:");
        quesionLabel.setFont(bigFont);
        mainPanel.add(quesionLabel);

        displayQuestion = new MyTextArea(7, 20);
        displayQuestion.setFont(bigFont);
        displayQuestion.setBackground(new Color(1,1,1, (float) 0.01));
        displayQuestion.setLineWrap(true);
        displayQuestion.setEditable(false);

        JScrollPane questionScroller = new JScrollPane(displayQuestion);
        questionScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        questionScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(questionScroller);

        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setFont(bigFont);
        mainPanel.add(answerLabel);

        displayAnswer = new MyTextArea(7, 20);
        displayAnswer.setFont(bigFont);
        displayAnswer.setBackground(new Color(1,1,1, (float) 0.01));
        displayAnswer.setLineWrap(true);

        JScrollPane answerScroller = new JScrollPane(displayAnswer);
        answerScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        answerScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(answerScroller);

        nextButton = new JButton("Confirm Answer");
        nextButton.addActionListener(e -> confirmOrNext());
        mainPanel.add(nextButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem loadMenuItem = new JMenuItem("Load card set");
        loadMenuItem.addActionListener(e -> open());
        fileMenu.add(loadMenuItem);

        JMenuItem menuItem = new JMenuItem("Return to main menu");
        menuItem.addActionListener(e -> returnToMainMenu());
        fileMenu.add(menuItem);

        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 700);
        frame.setLocationRelativeTo(answerScroller);
        frame.setVisible(true);
    }

    private void confirmOrNext() {
        if(!confirmedAnswer) {
            confirmAnswer();
        } else {
            nextCard();
        }
    }

    private void nextCard() {
        // show the next question
        if(currentCardIndex < cardList.size()) {
            showNextCard();
        } else {
            // there are no more cards!
            showScore();
            displayQuestion.setText("That was the last card");
            displayAnswer.setText("");
            nextButton.setEnabled(false);
        }
    }

    private void confirmAnswer() {
        nextButton.setText("Next Card");
        confirmedAnswer = true;
        try {
            String givenAnswer = displayAnswer.getText().toLowerCase();
            String correctAnswer = currentCard.getAnswer().toLowerCase();
            if(givenAnswer.equals(correctAnswer)) {
                displayQuestion.setText("Correct!");
                score++;
            } else {
                displayQuestion.setText("Incorrect!");
                displayAnswer.setText("The right answer was: " + currentCard.getAnswer());
            }
        } catch(NullPointerException e) {
            displayQuestion.setText("Please load a card set first");
        }
    }

    private void open() {
        JFileChooser fileOpen = new JFileChooser();
        fileOpen.showOpenDialog(frame);
        loadFile(fileOpen.getSelectedFile());
    }

    private void loadFile(File file) {
        cardList = new ArrayList<>();
        currentCardIndex = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null) {
                makeCard(line);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Couldn't write the cardList out: " + e.getMessage());
        }
        showNextCard();
    }

    private void makeCard(String lineToParse) {
        String[] result = lineToParse.split("/");
        QuizCard card = new QuizCard(result[0], result[1]);
        cardList.add(card);
        System.out.println("made a card");
    }

    private void showNextCard() {
        currentCard = cardList.get(currentCardIndex);
        currentCardIndex++;
        displayQuestion.setText(currentCard.getQuestion());
        displayAnswer.setText("");
        nextButton.setText("Confirm Answer");
        confirmedAnswer = false;
    }

    public void returnToMainMenu() {
        frame.dispose();
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.go();
    }

    public void showScore() {
        // show the score in a new window
        float percentage = (float) score / cardList.size() * 100;
        String message = "You score is: " + score + " out of " + cardList.size() + " correct (" + percentage + "%)";
        frame.dispose();
        frame = new JFrame("Score");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel scorePanel = new JPanel();
        JLabel scoreLabel = new JLabel(message);
        scorePanel.add(scoreLabel);


        // create a button to return to the main menu
        JButton returnButton = new JButton("Return to main menu");
        returnButton.addActionListener(e -> {   
            frame.dispose();
            returnToMainMenu();
        });
        scorePanel.add(returnButton);

        // create a button to open another card set
        JButton openButton = new JButton("Open another card set");
        openButton.addActionListener(e -> {
            frame.dispose();
            go();
            open();
        });
        scorePanel.add(openButton);

        frame.getContentPane().add(BorderLayout.CENTER, scorePanel);
        frame.setSize(300, 170);
        frame.setVisible(true);
    }
}
