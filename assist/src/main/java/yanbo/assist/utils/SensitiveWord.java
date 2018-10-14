package yanbo.assist.utils;

import android.support.annotation.IntDef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 描述：敏感词过滤
 *
 * @author Yanbo
 * @date 2018/9/22
 */
public class SensitiveWord {

    /**
     * 最小匹配规则
     */
    public static final int MATCH_TYPE_MIN = 1;
    /**
     * 最大匹配规则
     */
    public static final int MATCH_TYPE_MAX = 2;

    private Map sensitiveWordMap = null;

    private static class Holder {
        private static final SensitiveWord INSTANCE = new SensitiveWord();
    }

    public static SensitiveWord getInstance() {
        return SensitiveWord.Holder.INSTANCE;
    }

    private void initKeyWord() {
        if (sensitiveWordMap == null || sensitiveWordMap.size() == 0) {
            Set<String> keyWordSet = readSensitiveWordFile();
            addSensitiveWordToHashMap(keyWordSet);
        }
    }

    /**
     * 构造函数，初始化敏感词库
     */
    private SensitiveWord() {
        initKeyWord();
    }

    /**
     * 替换敏感字字符
     * 替换字符，默认*
     *
     * @param txt 文字
     */
    public String filter(String txt) {
        return replaceSensitiveWord(txt, MATCH_TYPE_MIN, "*");
    }

    /**
     * 替换敏感字字符
     *
     * @param txt         文字
     * @param matchType   匹配规则 1：最小匹配规则，2：最大匹配规则
     * @param replaceChar 替换字符，默认*
     */
    public String replaceSensitiveWord(String txt, @MatchType int matchType, String replaceChar) {
        String resultTxt = txt;
        //获取所有的敏感词
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }
        return resultTxt;
    }

    /**
     * 判断文字是否包含敏感字符
     *
     * @param txt       文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     */
    public boolean isContaintSensitiveWord(String txt, @MatchType int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int matchFlag = this.checkSensitiveWord(txt, i, matchType);
            if (matchFlag > 0) {
                //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则 1：最小匹配规则，2：最大匹配规则
     */
    public Set<String> getSensitiveWord(String txt, @MatchType int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();
        for (int i = 0; i < txt.length(); i++) {
            //判断是否包含敏感字符
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {
                //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                //减1的原因，是因为for会自增
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }

    /**
     * 检查文字中是否包含敏感字符，检查规则如下：
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @return 如果存在，则返回敏感词字符的长度，不存在返回0
     */
    private int checkSensitiveWord(String txt, int beginIndex, @MatchType int matchType) {
        //敏感词结束标识位：用于敏感词只有1位的情况
        boolean flag = false;
        //匹配标识数默认为0
        int matchFlag = 0;
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            //获取指定key
            nowMap = (Map) nowMap.get(word);
            //存在，则判断是否为最后一个
            if (nowMap != null) {
                //找到相应key，匹配标识+1
                matchFlag++;
                //如果为最后一个匹配规则,结束循环，返回匹配标识数
                if ("1".equals(nowMap.get("isEnd"))) {
                    //结束标志位为true
                    flag = true;
                    //最小规则，直接返回,最大规则还需继续查找
                    if (MATCH_TYPE_MIN == matchType) {
                        break;
                    }
                }
            } else {
                //不存在，直接返回
                break;
            }
        }
        //长度必须大于等于1，为词
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }

    /**
     * 读取敏感词库中的内容，将内容添加到set集合中
     */
    private Set<String> readSensitiveWordFile() {
        Set<String> set = new HashSet<>();
        InputStreamReader read = null;
        try {
            InputStream is = Utils.getAppContext().getAssets().open("sensitive_word.txt");
            read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt;
            while ((txt = bufferedReader.readLine()) != null) {
                set.add(txt);
            }
        } catch (Exception ignore) {
        } finally {
            if (read != null) {
                try {
                    read.close();
                } catch (IOException ignore) {
                }
            }
        }
        return set;
    }

    /**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：
     */
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());
        //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        for (String aKeyWordSet : keyWordSet) {
            key = aKeyWordSet;
            //关键字
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                //转换成char型
                char keyChar = key.charAt(i);
                //获取
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {
                    //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<>(1);
                    //不是最后一个
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    //最后一个
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 获取替换字符串
     *
     * @param replaceChar
     * @param length
     * @return
     */
    private String getReplaceChars(String replaceChar, int length) {
        StringBuilder resultReplace = new StringBuilder(replaceChar);
        for (int i = 1; i < length; i++) {
            resultReplace.append(replaceChar);
        }
        return resultReplace.toString();
    }

    @IntDef({MATCH_TYPE_MIN, MATCH_TYPE_MAX})
    @Retention(RetentionPolicy.SOURCE)
    @interface MatchType {
    }
}
