package app;

import entity.Word;
import io.DictionaryHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 入口
 * @author Ma Mengchang
 */
public class MainFrame {

    private JFrame jFrame;
    private TextField inputField;
    private TextArea inputArea;

    public MainFrame() {
        init();
    }

    private void init() {
        jFrame = new JFrame("电子英汉词典");
        jFrame.setSize(400, 300);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 菜单
        JMenuBar jMenuBar = new JMenuBar();
        JMenu optionMenu = new JMenu("操作");
        JMenuItem addWordItem = new JMenuItem("添加单词");
        JMenuItem deleteWordItem = new JMenuItem("删除单词");

        addWordItem.addActionListener(e -> {
            String word = JOptionPane.showInputDialog("输入添加的单词");
            if (word==null ||"".equals(word.trim())) {
                return;
            }
            String translation = JOptionPane.showInputDialog("输入添加的单词的意思");
            Word word1 = new Word(word, translation);
            System.out.println(word1);
            try {
                DictionaryHandler.addWord(word1);
                showSuccessDialog("添加成功");
            } catch (IOException ioException) {
                showErrorDialog("添加单词异常");
            }
        });
        deleteWordItem.addActionListener(e -> {
            String word = JOptionPane.showInputDialog("输入你要删除的单词");
            if (word==null || "".equals(word.trim())){
                return;
            }
            try {
                DictionaryHandler.deleteWord(word);
                showSuccessDialog("删除成功");
            } catch (IOException ioException) {
                ioException.printStackTrace();
                showErrorDialog("删除单词异常");
            }
        });
        optionMenu.add(addWordItem);
        optionMenu.add(deleteWordItem);
        jMenuBar.add(optionMenu);
        jFrame.setJMenuBar(jMenuBar);

        // 设置输入区
        jFrame.setLayout(new GridLayout(2,1));
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel header = new JLabel("　　电子英汉词典　　");
        header.setFont(new Font("华文行楷", Font.BOLD, 30));
        header.setLocation(100,30);
        panel.add(header);

        inputField = new TextField(15);
        inputField.addKeyListener(new InputListener());
        JButton searchBut = new JButton("查找");
        searchBut.addActionListener(e -> {
            List<Word> search = find(inputField.getText());
            inputArea.setText("");
            if (search.size()==0){
                inputArea.append("查找失败");
            }else{
                for (Word word:search){
                    appendWord(word);
                }
            }
        });
        panel.add(inputField);
        panel.add(searchBut);
        jFrame.add(panel);

        // 设置显示区
        inputArea = new TextArea(50,50);
        JScrollPane scrollPane = new JScrollPane(inputArea,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jFrame.add(scrollPane);
        jFrame.setVisible(true);
    }

    /**鼠标监听器*/
    class InputListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            inputArea.setText("");
            int total = 0;
            // 只显示10个
            List<Word> search = search(inputField.getText());
            for (Word word : search){
                appendWord(word);
                if (total++>10){
                    break;
                }
            }
        }
    }

    private void appendWord(Word word){
        String s = String.format("%-20s%s%n",word.getWord(),word.getTranslation());
        inputArea.append(s);
    }


    /**
     * 对应于模糊搜素
     * @param string 输入字符串
     * @return 搜索到的单词列表
     */
    private ArrayList<Word> search(String string) {
        ArrayList<Word> list = new ArrayList<>();
        try {
            for (Word word : DictionaryHandler.getWordList()) {
                if (word.isSimilar(string)) {
                    list.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对应于精确搜素
     * @param string 输入字符串
     * @return 搜索到的单词列表
     */
    private ArrayList<Word> find(String string) {
        ArrayList<Word> result = new ArrayList<>();
        try {
            for (Word word : DictionaryHandler.getWordList()) {
                if (word.isTarget(string)) {
                    result.add(word);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(jFrame, message, "提示",JOptionPane.INFORMATION_MESSAGE);
    }
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(jFrame, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}


