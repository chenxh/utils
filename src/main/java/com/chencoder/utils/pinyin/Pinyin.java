package com.chencoder.utils.pinyin;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rex on 2015/4/27.
 */
public class Pinyin {
    private String charsContent = "";
    private String wordsContent = "";
    private Map<String,String> wordsDict = new HashMap<String,String>();

    private int WORD_MAX_LEN = 10;
    private char SEP = ',';
    private char REP = 7;

    public Pinyin(){
        try {
            this.loadContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.parseChars();
        this.parseWords();
    }

    private void loadContent() throws IOException {
        InputStream isChars = this.getClass().getResourceAsStream("chars.csv");
        this.charsContent = this.inputStream2String(isChars).trim();

        InputStream isWords = this.getClass().getResourceAsStream("words.csv");
        this.wordsContent = this.inputStream2String(isWords).trim();
    }

    private String inputStream2String(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for(int n; (n = in.read(b)) != -1;){
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    private void parseChars(){
        String[] charsArr = this.charsContent.split("\n");
        for (String charStr:charsArr){
            String[] charEle = charStr.split(",");
            this.wordsDict.put(charEle[0].trim(), SEP + charEle[1].trim());
        }
    }

    private void parseWords(){
        String[] wordsArr = this.wordsContent.split("\n");
        for (String wordStr:wordsArr){
            String[] wordEle = wordStr.split(",");
            String pinyin = "";

            for (int i = 1; i < wordEle.length; i++){
                pinyin = pinyin + SEP + wordEle[i];
            }

            this.wordsDict.put(wordEle[0].trim(), pinyin.trim());
        }
    }

    private String translatePinyin(String content){
        content = content.replaceAll(this.SEP + "", this.REP + "");

        String result = "";

        int len = 0;
        int tailStartIdx = 0;
        int tailEndIdx = 0;
        int leftEndIdx = 0;
        String tail = "";
        String left = content;

        for (;left.length() > 0;) {
            // outer
            if(tail.length() == 0){
                len = left.length();
                int cutLen = (len > this.WORD_MAX_LEN) ? this.WORD_MAX_LEN : len;
                tailStartIdx = len - cutLen;
                tailEndIdx = len;
                leftEndIdx = tailStartIdx;
                tail = content.substring(tailStartIdx, tailEndIdx);
                left = content.substring(0, leftEndIdx);
            }

            for (; tail.length() > 1; ) {
                String value = this.wordsDict.get(tail);
                if (value != null && !value.equals("")) {
                    result = value + result;
                    tail = "";
                    break;
                } else {
                    left = left + tail.charAt(0);
                    tail = tail.substring(1, tail.length());
                }
            }
            if (tail.length() > 0){
                String value = this.wordsDict.get(tail);
                value = (value != null) ? value : SEP + tail;
                result = value + result;
                tail = "";
            }
        }

        if (result.charAt(0) == this.SEP){
            result = result.substring(1, result.length());
        }



        return result;
    }

    /**
     * 拼音，带分隔�?     * @param content
     * @return
     */
    public String translateWithSep(String content){
        String result = this.translatePinyin(content);
        result = result.replaceAll(this.REP + "", this.SEP + "");
        return result;
    }

    /**
     * 拼音数组，带分隔�?     * @param content
     * @return
     */
    public String[] translateInArray(String content){
        String result = this.translatePinyin(content);
        String[] resultArray = result.split(this.SEP + "");
        for (int idx = 0; idx < resultArray.length; idx++){
            String element = resultArray[idx];
            resultArray[idx] = element.replace(this.REP, this.SEP);
        }
        return resultArray;
    }

    /**
     * 拼音
     * @param content
     * @return
     */
    public String translate(String content){
        String result = this.translatePinyin(content);
        result = result.replaceAll(SEP + "","");
        result = result.replaceAll(this.REP + "", this.SEP + "");
        return result;
    }

    /**
     * 除去音调
     * @param content
     * @return
     */
    private String unMark(String content){
        content = content.replaceAll("ā","a");
        content = content.replaceAll("á","a");
        content = content.replaceAll("ǎ","a");
        content = content.replaceAll("à","a");

        content = content.replaceAll("ō","o");
        content = content.replaceAll("ó","o");
        content = content.replaceAll("ǒ","o");
        content = content.replaceAll("ò","o");

        content = content.replaceAll("ē","e");
        content = content.replaceAll("é","e");
        content = content.replaceAll("ě","e");
        content = content.replaceAll("è","e");

        content = content.replaceAll("ī","i");
        content = content.replaceAll("í","i");
        content = content.replaceAll("ǐ","i");
        content = content.replaceAll("ì","i");

        content = content.replaceAll("ū","u");
        content = content.replaceAll("ú","u");
        content = content.replaceAll("ǔ","u");
        content = content.replaceAll("ù","u");

        content = content.replaceAll("ǖ","ü");
        content = content.replaceAll("ǘ","ü");
        content = content.replaceAll("ǚ","ü");
        content = content.replaceAll("ǜ","ü");

        return content;
    }

    /**
     * 不带音调拼音，带分隔�?     * @param content
     * @return
     */
    public String translateWithSepNoMark(String content){
        String result = this.translateWithSep(content);
        return this.unMark(result);
    }

    /**
     * 不带音调拼音
     * @param content
     * @return
     */
    public String translateNoMark(String content){
        String result = this.translate(content);
        return this.unMark(result);
    }

    /**
     * 不带音调拼音数组，带分隔�?     * @param content
     * @return
     */
    public String[] translateInArrayNoMark(String content){
        String result = this.translatePinyin(content);
        String[] resultArray = result.split(this.SEP + "");
        for (int idx = 0; idx < resultArray.length; idx++){
            String element = resultArray[idx];
            element = this.unMark(element);
            resultArray[idx] = element.replace(this.REP, this.SEP);
        }
        return resultArray;
    }

    /**
     * 翻译为拼音首字母
     * @param content
     * @return
     */
    public String translateFirstChar(String content){
        String[] result = this.translateInArray(content);
        String chars = "";
        for(String element : result){
            chars = chars + element.charAt(0);
        }
        return chars;
    }

}
