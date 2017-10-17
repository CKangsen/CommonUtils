

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;

import com.afmobi.palmgroup.PalmGroupApplication;
import com.afmobi.qs.R;

import java.io.File;
import java.io.IOException;


public class AudioPlayerUtil {
    private static final String TAG = AudioPlayerUtil.class.getSimpleName();
    private static AudioPlayerUtil instance;
    private MediaPlayer mMediaPlayer = null;
    private int mPositionCurrentPlay = 0;
    /**当前播放的文件名*/
    private String  mCurrentUrl="";
    /**业务可以给当前因为打个tag*/
    private String mCurrentAudioTag="";
    /**监听回调*/
    private OnMediaPlayListener mOnMediaPlayListener;
    private Handler mHandler;
    private final int CALLBACK_PLAYPOSITION =16;
    private Runnable mGetCurPositionRunnable = new Runnable() {
        @Override
        public void run() {
            if(mOnMediaPlayListener!=null){
                mOnMediaPlayListener.onCurrentPosition(getAudioPlayingPosition());
            }
            mHandler.postDelayed(mGetCurPositionRunnable,CALLBACK_PLAYPOSITION);
        }
    };

    public AudioPlayerUtil() {
        init();
    }
    /**
     * 单例
     * @return
     */
    public static AudioPlayerUtil getInstance() {
        if(null==instance){
            synchronized (AudioPlayerUtil.class){
                if(null==instance){
                    instance = new AudioPlayerUtil();
                }
            }
        }
        return instance;
    }

    private void init(){
        mMediaPlayer = new MediaPlayer();
        mHandler = new Handler();
    }

    public void playAudio(String filePath,OnMediaPlayListener onMediaPlayListener,String tag){
        mCurrentAudioTag = tag;
        playAudio(filePath,onMediaPlayListener);
    }

    public void playAudio(String filePath,OnMediaPlayListener onMediaPlayListener){
        mOnMediaPlayListener = onMediaPlayListener;
        if(null==mMediaPlayer||(TextUtils.isEmpty(filePath))){
            LogUtils.e(TAG,"--------------null==mMediaPlayer--or------filePath==null-----");
            return;
        }
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            if(mOnMediaPlayListener!=null){
                mOnMediaPlayListener.onCompletion(mCurrentUrl);
                mHandler.removeCallbacks(mGetCurPositionRunnable);
            }
        }
        mCurrentUrl = filePath;
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                releaseAudioFocus();
                if(null!=mOnMediaPlayListener){
                    if(mOnMediaPlayListener!=null){
                        mOnMediaPlayListener.onCurrentPosition(getAudioPlayingPosition());
                    }
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mOnMediaPlayListener.onCompletion(mCurrentUrl);
                        }
                    }, 10);

                    mHandler.removeCallbacks(mGetCurPositionRunnable);
                    mCurrentAudioTag = "";
                    mCurrentUrl = "";
                }
            }
        });

        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtils.e(TAG,"------------onError----------what------"+what+"--------extra--------"+extra);
                doWithPlayErrorInfo(what+"");
                return false;
            }
        });

        try{
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mCurrentUrl);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            applyForAudioFocus();
            if(null!=mOnMediaPlayListener){
                mOnMediaPlayListener.onStart(mCurrentUrl);
            }
            mHandler.postDelayed(mGetCurPositionRunnable,CALLBACK_PLAYPOSITION);
        } catch (IllegalArgumentException e){
            LogUtils.e(TAG,"------------IllegalArgumentException----------------"+e.toString());
            doWithPlayErrorInfo(IllegalArgumentException.class.getSimpleName());
            e.printStackTrace();
        } catch (SecurityException  e){
            LogUtils.e(TAG,"------------SecurityException----------------"+e.toString());
            doWithPlayErrorInfo(SecurityException.class.getSimpleName());
            e.printStackTrace();
        } catch (IllegalStateException  e){
            LogUtils.e(TAG,"------------IllegalStateException----------------"+e.toString());
            doWithPlayErrorInfo(IllegalStateException.class.getSimpleName());
            e.printStackTrace();
        }catch (IOException e){
            LogUtils.e(TAG,"------------IllegalStateException----------------"+e.toString());
            doWithPlayErrorInfo(IOException.class.getSimpleName());
            e.printStackTrace();
        }

    }

    /**
     * 判断播放器是否处于播放阶段
     * @return
     */
    public boolean isPlaying(){
        if(mMediaPlayer==null){
            return false;
        }
        return mMediaPlayer.isPlaying();
    }

    /**
     * 判断当前播放是否是某个文件
     * @param filePath
     * @return
     */
    public boolean isPlaying(String filePath){
        boolean isCurrentFilePlaying = false;
        if(isPlaying()){
            if(!TextUtils.isEmpty(filePath)){
                if(mCurrentUrl.equals(filePath)){
                    isCurrentFilePlaying = true;
                }else{
                    isCurrentFilePlaying = false;
                }
            } else {
                isCurrentFilePlaying = false;
            }
        } else {
            isCurrentFilePlaying = false;
        }
        return  isCurrentFilePlaying;
    }

    /**
     * 利用filepath 和tag判断是否在播放
     * @param filePath
     * @param tag
     * @return
     */
    public boolean isPlaying(String filePath ,String tag){
        boolean isPlaying =false;
        if(isPlaying(filePath)){
            if(mCurrentAudioTag.equals(tag)){
                isPlaying = true;
            } else {
                isPlaying = false;
            }
        } else {
            isPlaying =false;
        }
        return isPlaying;
    }

    /**
     * 停止播放
     */
    public void stopAudio(){
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mCurrentUrl = "";
        }
        if(mOnMediaPlayListener!=null){
            mOnMediaPlayListener.onCompletion(mCurrentUrl);
            mHandler.removeCallbacks(mGetCurPositionRunnable);
        }
        releaseAudioFocus();
    }

    /**
     * 暂停播放
     */
    public void pauseAudio(){
        if(mMediaPlayer!=null){
            mMediaPlayer.pause();
            mPositionCurrentPlay =mMediaPlayer.getCurrentPosition();
            releaseAudioFocus();
        }
    }

    /**
     * 暂停后恢复播放
     */
    public void resumePlay(){
        if(mMediaPlayer!=null){
            try{
                mMediaPlayer.prepare();
                mMediaPlayer.seekTo(mPositionCurrentPlay);
                mMediaPlayer.start();
                applyForAudioFocus();
            } catch (IllegalArgumentException e){
                LogUtils.e(TAG,"------------IllegalArgumentException----------------"+e.toString());
                doWithPlayErrorInfo(IllegalArgumentException.class.getSimpleName());
                e.printStackTrace();
            } catch (SecurityException  e){
                LogUtils.e(TAG,"------------SecurityException----------------"+e.toString());
                doWithPlayErrorInfo(SecurityException.class.getSimpleName());
                e.printStackTrace();
            } catch (IllegalStateException  e){
                LogUtils.e(TAG,"------------IllegalStateException----------------"+e.toString());
                doWithPlayErrorInfo(IllegalStateException.class.getSimpleName());
                e.printStackTrace();
            }catch (IOException e) {
                LogUtils.e(TAG, "------------IllegalStateException----------------" + e.toString());
                doWithPlayErrorInfo(IOException.class.getSimpleName());
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取当前播放位置
     * @return
     */
    public int getAudioPlayingPosition(){
        int position;
        if(null==mMediaPlayer){
            position = 0;
        } else {
            position = mMediaPlayer.getCurrentPosition();
        }
        return  position;
    }

    /**
     *获取音频文件时长
     * @param filePath
     * @return
     */
    public int getAudioDuration(String filePath){
        int audioDuration = 0;
        if(TextUtils.isEmpty(filePath)){
            LogUtils.e(TAG,"---------getAudioDuration-----filePath-----NULL-----");
            return 0;
        }
        File file = new File(filePath);
        if(!file.exists()){
            LogUtils.e(TAG,"---------getAudioDuration-----filePath-----NULL-----");
            ToastManagerUtil.getInstance().show(PalmGroupApplication.getmPalmGroupApplication().getApplicationContext(),PalmGroupApplication.getmPalmGroupApplication().getString(R.string.File_doesnot_exist));
            return 0;
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.reset();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            audioDuration = mediaPlayer.getDuration();
        } catch (IllegalArgumentException e){
            LogUtils.e(TAG,"------------IllegalArgumentException----------------"+e.toString());
            doWithPlayErrorInfo(IllegalArgumentException.class.getSimpleName());
            e.printStackTrace();
        } catch (SecurityException  e){
            LogUtils.e(TAG,"------------SecurityException----------------"+e.toString());
            doWithPlayErrorInfo(SecurityException.class.getSimpleName());
            e.printStackTrace();
        } catch (IllegalStateException  e){
            LogUtils.e(TAG,"------------IllegalStateException----------------"+e.toString());
            doWithPlayErrorInfo(IllegalStateException.class.getSimpleName());
            e.printStackTrace();
        }catch (IOException e){
            LogUtils.e(TAG,"------------IllegalStateException----------------"+e.toString());
            doWithPlayErrorInfo(IOException.class.getSimpleName());
            e.printStackTrace();
        } finally {
            mediaPlayer.release();
            mediaPlayer=null;
        }
        return audioDuration;
    }

    /**
     *申请音频焦点
     */
    private void applyForAudioFocus(){
        //获取音频焦点
        AudioManager am = (AudioManager) PalmGroupApplication.getmPalmGroupApplication().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * 释放音频焦点
     */
    private void releaseAudioFocus(){
        //释放音频焦点
        AudioManager am = (AudioManager) PalmGroupApplication.getmPalmGroupApplication().getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
    }
    /**
     * 处理回调播放错误
     * @param error
     */
    private void doWithPlayErrorInfo(String error){
        mCurrentAudioTag = "";
        mCurrentUrl = "";
        if(mOnMediaPlayListener!=null){
            mOnMediaPlayListener.onError(mCurrentUrl,error);
        }
        releaseAudioFocus();
        mHandler.removeCallbacks(mGetCurPositionRunnable);
    }

    public interface OnMediaPlayListener {

        void onStart(String url);

        void onCompletion(String url);

        void onError(String url,String error);

        void onCurrentPosition(int curPosition);
    }
}
