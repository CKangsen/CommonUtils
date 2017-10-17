
import android.content.ClipboardManager;
import android.content.Context;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


/**
 * 禁止粘贴空格，禁止输入空格和 Emoji表情
 * Created by cks on 2017/5/22.
 */

public class PasswordCustomPasteEditText extends EditText {
    public PasswordCustomPasteEditText(Context context) {
        super(context);
        setEditTextInhibitInputSpace(this);
    }

    public PasswordCustomPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEditTextInhibitInputSpace(this);
        //setLongClickable(false);
        setTextIsSelectable(false);
        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

    public PasswordCustomPasteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setEditTextInhibitInputSpace(this);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        if (id == android.R.id.paste) {
            //只设置粘贴文本
            int lastCursorPosion=getSelectionStart();
            ClipboardManager clip = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String cliptext=clip.getPrimaryClip().getItemAt(0).getText().toString().replaceAll("\\s*", "");//去掉空格
            super.onTextContextMenuItem(android.R.id.paste);

            String text = getText().toString().replaceAll("\\s*", "");//去掉空格
            //这里之所以分两种情况是因为android系统的粘贴，为了用户体验，会在粘贴的文本前后加上空格，表示是粘贴的内容
            //如果在文本中间粘贴，会在粘贴文本前后都加上空格；如果在文末粘贴，会在粘贴文本前加上空格；如果空的内容中粘贴，则不加空格
            /*if ("".equals(text)){
                text = cliptext;
            }*/
            if(lastCursorPosion!=0){
                setText(text);
                setSelection(text.length());
            }else{
                setText(text);
                setSelection(text.length());
            }
            return true;
        }
        return super.onTextContextMenuItem(id);
    }

    //禁止输入空格和 Emoji表情
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.length()<=0){
                    return null;
                }
                if(source.equals(" ")){
                    return "";
                } else if(isEmojiCharacter(source.charAt(0))){
                    return "";
                } else {
                    return null;
                }
            }
        };
        editText.setFilters(new InputFilter[]{new LoginFilter.PasswordFilterGMail(),filter,new InputFilter.LengthFilter(TudcConstant.PASSWORD_MAX_LENGTH)});
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint >= 0x2600 && codePoint <= 0x27BF) // 杂项符号与符号字体
                || codePoint == 0x303D
                || codePoint == 0x2049
                || codePoint == 0x203C
                || (codePoint >= 0x2000 && codePoint <= 0x200F)//
                || (codePoint >= 0x2028 && codePoint <= 0x202F)//
                || codePoint == 0x205F //
                || (codePoint >= 0x2065 && codePoint <= 0x206F)//
                /* 标点符号占用区域 */
                || (codePoint >= 0x2100 && codePoint <= 0x214F)// 字母符号
                || (codePoint >= 0x2300 && codePoint <= 0x23FF)// 各种技术符号
                || (codePoint >= 0x2B00 && codePoint <= 0x2BFF)// 箭头A
                || (codePoint >= 0x2900 && codePoint <= 0x297F)// 箭头B
                || (codePoint >= 0x3200 && codePoint <= 0x32FF)// 中文符号
                || (codePoint >= 0xD800 && codePoint <= 0xDFFF)// 高低位替代符保留区域
                || (codePoint >= 0xE000 && codePoint <= 0xF8FF)// 私有保留区域
                || (codePoint >= 0xFE00 && codePoint <= 0xFE0F)// 变异选择器
                || codePoint >= 0x10000;
    }

    boolean canPaste() {
        return false;
    }

    boolean canCut() {
        return false;
    }

    boolean canCopy() {
        return false;
    }

    boolean canSelectAllText() {
        return false;
    }

    boolean canSelectText() {
        return false;
    }

    boolean textCanBeSelected() {
        return false;
    }
}
