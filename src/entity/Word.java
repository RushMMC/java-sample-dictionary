package entity;

/**
 * @author Ma Mengchang
 */
public class Word {

    /**单词*/
    private String word;
    /**翻译*/
    private String translation;

    public Word(String word, String translation) {
        this.word = word;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    /**
     * 精确匹配
     * 对于单词精确匹配，对于翻译模糊匹配
     * @param str 输入的字符串
     * @return 匹配到返回true，否则为false
     */
    public boolean isTarget(String str){
        return word.equals(str) || translation.contains(str);
    }

    /**
     * 模糊匹配
     * @param str 输入的字符串
     * @return 匹配到返回true，否则为false
     */
    public boolean isSimilar(String str){
        return word.startsWith(str) || translation.contains(str);
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
