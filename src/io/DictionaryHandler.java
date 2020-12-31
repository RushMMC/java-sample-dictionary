package io;

/**
 * @author Ma Mengchang
 */
import entity.Word;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DictionaryHandler {

    public static List<Word> list;

    static {
        try {
            initWordList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取所有的单词
     * @return 包含字典文件中的所有单词
     * @throws IOException 输入输出异常
     */
    public static List<Word> getWordList() throws IOException {
        if (list==null){
            initWordList();
        }
        return list;
    }

    /**
     * 初始化单词列表
     */
    private static void initWordList() throws IOException {
        list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(IoConstant.DICTIONARY_PATH))){
            String line;
            while((line=reader.readLine())!=null){
                int index = line.indexOf(IoConstant.TRANSLATION_START_FLAG);
                if (index != -1){
                    String word = line.substring(0,index).trim();
                    String translation = line.substring(index+1,line.length()-1);
                    list.add(new Word(word,translation));
                }
            }
        }
    }

    /**
     * 增加一个单词
     */
    public static void addWord(Word word) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(IoConstant.DICTIONARY_PATH))){
            writer.write(format(word));
            for (Word w:getWordList()){
                writer.write(format(w));
            }
            writer.flush();
            initWordList();
        }
    }

    /**
     * 格式化输出
     * @param w 单词对象
     * @return 格式化后的字符串
     */
    private static String format(Word w) {
        return String.format("%s %c%s%c%n",
                w.getWord(),
                IoConstant.TRANSLATION_START_FLAG,
                w.getTranslation(),
                IoConstant.TRANSLATION_END_FLAG
                );
    }

    public static void deleteWord(String word) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(IoConstant.DICTIONARY_PATH))){
            for (Word w:getWordList()){
                if (!w.getWord().equals(word)){
                    writer.write(format(w));
                }
            }
            writer.flush();
            initWordList();
        }
    }
}
