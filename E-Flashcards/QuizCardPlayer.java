import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class QuizCardPlayer {
    private ArrayList<QuizCard> cardList = new ArrayList<>();
    private int currentCardIndex = 0;
    private QuizCard currentCard;
    private JTextArea displayQuestion;
    private JFrame frame;
    private JButton nextButton;
    private boolean isShowAnswer;
    
    public static void main(String[] args) {
        QuizCardPlayerScore reader = new QuizCardPlayerScore();
        reader.go();
    }

    QuizCardPlayer() {
        // 
    }

    public void go() {
        frame = new JFrame("Quiz Card Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        JLabel quesionLabel = new JLabel("Question:");
        quesionLabel.setFont(bigFont);
        mainPanel.add(quesionLabel);

        displayQuestion = new JTextArea(7, 20);
        displayQuestion.setFont(bigFont);
        displayQuestion.setLineWrap(true);
        displayQuestion.setEditable(false);

        JScrollPane questionScroller = new JScrollPane(displayQuestion);
        questionScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        questionScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(questionScroller);

        JLabel answerLabel = new JLabel("Answer:");
        answerLabel.setFont(bigFont);
        mainPanel.add(answerLabel);

        nextButton = new JButton("Show Question");
        nextButton.addActionListener(e -> nextCard());
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
        frame.setVisible(true);
    }

    private void nextCard() {
        if(isShowAnswer) {
            // show the answer because they've seen the question
            displayQuestion.setText(currentCard.getAnswer());
            nextButton.setText("Next Card");
            isShowAnswer = false;
        } else { // show the next question
            if(currentCardIndex < cardList.size()) {
                showNextCard();
            } else {
                // there are no more cards!
                displayQuestion.setText("That was the last card");
                nextButton.setEnabled(false);
            }
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
        nextButton.setText("Show Answer");
        isShowAnswer = true;
    }

    private void saveFile(File file) {
        BufferedWriter writer =  null;
        try {
            writer = new BufferedWriter(new FileWriter(file));

            for(QuizCard card : cardList) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
        } catch(IOException e) {
            System.out.println("Couldn't write the cardList out: " + e.getMessage());
        } finally {
            try {
                writer.close();
            } catch(IOException e) {
                System.out.println("Couldn't close the writer: " + e.getMessage());
            }
        }
    }

    public void returnToMainMenu() {
        frame.dispose();
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.go();
    }
}
