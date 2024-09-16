import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class QuizCardBuilder {
    private ArrayList<QuizCard> cardList = new ArrayList<>();
    private JTextArea question;
    private JTextArea answer;
    private JFrame frame;
    private int index = 0;

    public static void main(String[] args) {
        new QuizCardBuilder().go();
    }

    public void go() {
        frame = new JFrame("Quiz Card Builder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        Font bigFont = new Font("sanserif", Font.BOLD, 24);

        question = createTextArea(bigFont);
        question.setLineWrap(true);

        //change the background of the text area to an image
        question.setOpaque(false);
        question.setBackground(new Color(0, 0, 0, 0));

        JScrollPane qScroller = createScroller(question);
        answer = createTextArea(bigFont);
        JScrollPane aScroller = createScroller(answer);

        mainPanel.add(new JLabel("Question:"));
        mainPanel.add(qScroller);
        mainPanel.add(qScroller);
        mainPanel.add(new JLabel("Answer:"));
        mainPanel.add(aScroller);

        JButton previousButton = new JButton("Previous Card");
        previousButton.addActionListener(e -> previousCard());
        mainPanel.add(previousButton);

        JButton nextButton = new JButton("Next Card");
        nextButton.addActionListener(e -> nextCard());
        mainPanel.add(nextButton);

        JButton saveButton = new JButton("Save card");
        saveButton.addActionListener(e -> saveCard());
        mainPanel.add(saveButton);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem newMenuItem = new JMenuItem("New Card List");
        newMenuItem.addActionListener(e -> clearAll());

        JMenuItem saveMenuItem = new JMenuItem("Save Card List");
        saveMenuItem.addActionListener(e -> saveCardList());

        JMenuItem quitMenuItem = new JMenuItem("Return to main menu");
        quitMenuItem.addActionListener(e -> quit());


        fileMenu.add(newMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(quitMenuItem);
        menuBar.add(fileMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(500, 600);
        frame.setLocationRelativeTo(aScroller);
        frame.setVisible(true);
    }

    private JScrollPane createScroller(JTextArea textArea) {
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return scroller;
    }

    private JTextArea createTextArea(Font font) {
        JTextArea textArea;
        textArea = new MyTextArea(6, 20);
        textArea.setBackground(new Color(1,1,1, (float) 0.01));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(font);
        return textArea;
    }

    private void nextCard() {
        try {
            index++;
            QuizCard card = cardList.get(index);
            question.setText(card.getQuestion());
            answer.setText(card.getAnswer());
        } catch (IndexOutOfBoundsException e) {
            question.setText("There's no next card!");
            answer.setText("Replace this texts with your own question and answer.");
            index = cardList.size() - 1;
        }
        // System.out.println("index: " + index);
    }

    private void previousCard() {
        try {
            index--;
            QuizCard card = cardList.get(index);
            question.setText(card.getQuestion());
            answer.setText(card.getAnswer());
        } catch (IndexOutOfBoundsException e) {
            question.setText("There's no previous card!");
            answer.setText("Replace this texts with your own question and answer.");
            index = -1;
        }
    }

    private void saveCard() {
        QuizCard card = new QuizCard(question.getText(), answer.getText());
        cardList.add(card);
        clearCard();
        index = cardList.size();
    }

    private void saveCardList() {
        // QuizCard card = new QuizCard(question.getText(), answer.getText());
        // cardList.add(card);

        JFileChooser fileSave = new JFileChooser();
        fileSave.showSaveDialog(frame);
        saveFile(fileSave.getSelectedFile());
    }

    private void clearAll() {
        cardList.clear();
        clearCard();
    }

    private void clearCard() {
        question.setText("");
        answer.setText("");
        question.requestFocus();
    }

    private void saveFile(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for(QuizCard card : cardList) {
                writer.write(card.getQuestion() + "/");
                writer.write(card.getAnswer() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't write the cardlist out: " + e.getMessage());
        }
    }

    public void quit() {
        frame.dispose();
        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        mainMenuPanel.go();
    }
}
