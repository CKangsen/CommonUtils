mport android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class HttpClientUtil {

    private static final String TAG = HttpClientUtil.class.getSimpleName();

    public static int POOL_SIZE = 8;

    private static ExecutorService sExecutorService;

    private static int READ_TIME_OUT = 60 * 1000;

    private static int CONNECTE_TIME_OUT = 60 * 1000;

    private static final String ENCODE = "UTF-8";

    private static final int REPONSESUCCESS = 10001;
    private static final int REPONSESERROR = 10002;


    static {
        sExecutorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public interface HttpCallback <T> {
        public  void onSuccess(T response);
        public  void onError(T error);
    }


    public static void doPost(String url,final String data,final HttpCallback<String> callback){
        final String _url = url;
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case REPONSESUCCESS:
                        if(msg.obj!=null){
                            String deviceid = "";
                            try {
                                deviceid = (String)(msg.obj);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if(callback!=null){
                                callback.onSuccess(deviceid);
                            }
                        }
                        break;
                    case REPONSESERROR:
                        if(msg.obj!=null){
                            String errMsg="";
                            try {
                                errMsg = (String)(msg.obj);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            if(callback!=null){
                                callback.onError(errMsg);
                            }
                        }
                        break;
                }
            }
        };


        sExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                    return;
                }
                BufferedReader bufferedReader  = null;
                StringBuffer response = new StringBuffer();
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setReadTimeout(READ_TIME_OUT);
                    urlConnection.setConnectTimeout(CONNECTE_TIME_OUT);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                    wr.writeBytes(data);
                    wr.flush();
                    wr.close();

                    int code = urlConnection.getResponseCode();
                    if (code >= 200 && code < 400) {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),ENCODE));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        LogUtils.i(TAG,"--------response====="+response.toString());
                        Message msg = Message.obtain();
                        msg.what=REPONSESUCCESS;
                        msg.obj=response.toString();
                        handler.sendMessage(msg);

                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(),ENCODE));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        LogUtils.i(TAG,"--------response====="+response.toString());
                        Message msg = Message.obtain();
                        msg.what=REPONSESERROR;
                        msg.obj=response.toString();
                        handler.sendMessage(msg);
                    }
                }catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    LogUtils.i(TAG,"--------SocketTimeoutException====="+e.getMessage());
                    Message msg = Message.obtain();
                    msg.what=REPONSESERROR;
                    msg.obj=e.getMessage();
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what=REPONSESERROR;
                    msg.obj=e.getMessage();
                    handler.sendMessage(msg);
                    LogUtils.i(TAG,"--------IOException====="+e.getMessage());

                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static void doGet(String url,final HttpCallback<String> callback) {
        final String _url = url;
        sExecutorService.submit(new Runnable() {

            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL(_url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                    return;
                }
                BufferedReader bufferedReader  = null;
                StringBuffer response = new StringBuffer();
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setReadTimeout(READ_TIME_OUT);
                    urlConnection.setConnectTimeout(CONNECTE_TIME_OUT);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("connection", "close");
                    int code = urlConnection.getResponseCode();
                    if (code >= 200 && code < 400) {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),ENCODE));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        callback.onSuccess(response.toString());
                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(),ENCODE));
                        String line = null;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }
                        callback.onError(response.toString());
                    }
                }catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(e.getMessage());
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getReponseData(){

    }

}
