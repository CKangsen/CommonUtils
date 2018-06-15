package com.threetree.baseproject.util;

import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;

import com.blankj.utilcode.util.RegexUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditTextUtil {

  /**
   * EditText只能输入字母数字
   *
   * @param editText
   */
  public static void setOnlyInputLetterAndNum(EditText editText) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= 16 || dest.length() >= 16) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.LETTER_AND_NUM, source.toString())) {
          return "";
        }
        return null;
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * EditText只能输入字母数字
   *
   * @param editText
   */
  public static void setOnlyInputLetterAndNumLength(EditText editText, final int length) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isSourceMoreThanLength = false;
        if (source.length()>length){
          isSourceMoreThanLength = true;
          source = source.subSequence(0,length);
        }
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= length || dest.length() >= length) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.LETTER_AND_NUM, source.toString())) {
          return "";
        }
        if (isSourceMoreThanLength){
          return source.toString();
        }else {
          return null;
        }

      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * EditText只能输入字母数字
   *
   * @param editText
   */
  public static void setOnlyInputLetterLenght(EditText editText, final int length) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isSourceMoreThanLength = false;
        if (source.length()>length){
          isSourceMoreThanLength = true;
          source = source.subSequence(0,length);
        }
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= length || dest.length() >= length) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.LETTER, source.toString())) {
          return "";
        }
        if (isSourceMoreThanLength){
          return source.toString();
        }else {
          return null;
        }
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

 /**
   * EditText只能输入字母数字
   *
   * @param editText
   */
  public static void setOnlyInputLetterAndNum_IDCard(EditText editText) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= 18 || dest.length() >= 18) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.LETTER_AND_NUM, source.toString())) {
          return "";
        }
        return null;
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }


  /**
   * 身份证姓名
   *
   * @param editText
   */
  public static void setOnlyInputName(EditText editText) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //        CharSequence source,  //输入的文字
        //        int start,  //开始位置
        //        int end,  //结束位置
        //        Spanned dest, //当前显示的内容
        //        int dstart,  //当前开始位置
        //        int dend //当前结束位置

        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if ("·".equals(source.toString())) {
          return "·";
        }
        if (dstart >= 16 || dest.length() >= 16) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.REGEX_ZH, source.toString())) {
          return "";
        }
        return null;
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * 身份证姓名
   *
   * @param editText
   */
  public static void setOnlyInputIdCardNum(EditText editText) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= 19 || dest.length() >= 19) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.NUMBER, source.toString())) {
          return "";
        }
        return null;
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }


  /**
   * 身份证姓名
   *
   * @param editText
   */
  public static void setOnlyInputNumLength(EditText editText, final int length) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isSourceMoreThanLength = false;
        if (source.length()>length){
          isSourceMoreThanLength = true;
          source = source.subSequence(0,length);
        }
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= length || dest.length() >= length) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.NUMBER, source.toString())) {
          return "";
        }
        if (isSourceMoreThanLength){
          return source.toString();
        }else {
          return null;
        }
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /**
   * 身份证姓名
   *
   * @param editText
   */
  public static void setOnlyInputLength(EditText editText, final int length) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isSourceMoreThanLength = false;
        if (source.length()>length){
          isSourceMoreThanLength = true;
          source = source.subSequence(0,length);
        }
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= length || dest.length() >= length) {// 字符数限制
          return "";
        }
        if (isSourceMoreThanLength){
          return source.toString();
        }else {
          return null;
        }
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  public static void setOnlyInputLength(EditText editText, final int length, final MatchLengthListener matchLengthListener) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isSourceMoreThanLength = false;
        if (source.length()>length){
          isSourceMoreThanLength = true;
          source = source.subSequence(0,length);
        }
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= length || dest.length() >= length) {// 字符数限制
          matchLengthListener.matchLength();
          return "";
        }
        if (isSourceMoreThanLength){
          return source.toString();
        }else {
          return null;
        }
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }


  public static void setIDCardDateInput(EditText editText, final int length) {
    InputFilter filter = new InputFilter() {
      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        boolean isSourceMoreThanLength = false;
        if (source.length()>length){
          isSourceMoreThanLength = true;
          source = source.subSequence(0,length);
        }
        // 删除等特殊字符，直接返回
        if ("".equals(source.toString())) {
          return null;
        }
        if (dstart >= length || dest.length() >= length) {// 字符数限制
          return "";
        }
        if (!RegexUtils.isMatch(RegesConstants.HENGAN_NUM,source)){
          return "";
        }
        if (isSourceMoreThanLength){
          return source.toString();
        }else {
          return null;
        }
      }
    };
    editText.setFilters(new InputFilter[]{filter});
  }

  /** * 检测是否有emoji表情 * @param source * @return */
  public static boolean containsEmoji(String source) {                          //两种方法限制emoji
    int len = source.length();
    for (int i = 0; i < len; i++) {
      char codePoint = source.charAt(i);
      if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
        return true;
      }
    }
    return false;
  }


  /** * 判断是否是Emoji * @param codePoint 比较的单个字符 * @return */
  private static boolean isEmojiCharacter(char codePoint) {
    return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
        || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
        || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
        || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
  }

  /**
   * 给edittext设置过滤器 过滤emoji
   *
   * @param et
   */
  public static void setEmojiFilter(EditText et, final int length) {
    InputFilter emojiFilter = new InputFilter() {
      Pattern pattern = Pattern
          .compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
              Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

      @Override
      public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        if (length < dest.length() + source.length()) {
          return "";
        }

        Matcher matcher = pattern.matcher(source);
        if (matcher.find()) {
          return "";
        }
        /**
         * 判断是否超出长度
         */
        if (dstart + source.length() > length) {
          return source.toString().substring(0, length - dstart);
        }
        return null;
      }
    };
    et.setFilters(new InputFilter[]{emojiFilter});
  }

  public interface MatchLengthListener{
    void matchLength();
  }
}
