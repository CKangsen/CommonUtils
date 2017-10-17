 

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
    private static int READ_TIME_OUT = 10000;
    private static int CONNECTE_TIME_OUT = 10000;
    private static final String ENCODE = "UTF-8";

    public HttpClientUtil() {
    }

    public static void doPost(final String url, final String data, final HttpClientUtil.HttpCallback<String> callback) {
        sExecutorService.submit(new Runnable() {
            public void run() {
                URL urlx = null;

                try {
                    urlx = new URL(url);
                } catch (MalformedURLException var19) {
                    var19.printStackTrace();
                    callback.onError(var19.getMessage());
                    return;
                }

                BufferedReader bufferedReader = null;
                StringBuffer response = new StringBuffer();
                HttpURLConnection urlConnection = null;

                try {
                    urlConnection = (HttpURLConnection)urlx.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setReadTimeout(HttpClientUtil.READ_TIME_OUT);
                    urlConnection.setConnectTimeout(HttpClientUtil.CONNECTE_TIME_OUT);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    urlConnection.setRequestProperty("Accept", "application/json");
                    DataOutputStream e = new DataOutputStream(urlConnection.getOutputStream());
                    e.writeBytes(data);
                    e.flush();
                    e.close();
                    int code = urlConnection.getResponseCode();
                    String line;
                    if(code >= 200 && code < 400) {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                        line = null;

                        while((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }

                        System.out.println("--------response=====" + response.toString());
                        callback.onSuccess(response.toString());
                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "UTF-8"));
                        line = null;

                        while((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }

                        callback.onError(response.toString());
                    }
                } catch (SocketTimeoutException var20) {
                    var20.printStackTrace();
                    callback.onError(var20.getMessage());
                } catch (IOException var21) {
                    var21.printStackTrace();
                    callback.onError(var21.getMessage());
                } finally {
                    if(bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException var18) {
                            var18.printStackTrace();
                        }
                    }

                }

            }
        });
    }

    public static void doGet(final String url, final HttpClientUtil.HttpCallback<String> callback) {
        sExecutorService.submit(new Runnable() {
            public void run() {
                URL urlx = null;

                try {
                    urlx = new URL(url);
                } catch (MalformedURLException var18) {
                    var18.printStackTrace();
                    callback.onError(var18.getMessage());
                    return;
                }

                BufferedReader bufferedReader = null;
                StringBuffer response = new StringBuffer();
                HttpURLConnection urlConnection = null;

                try {
                    urlConnection = (HttpURLConnection)urlx.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setReadTimeout(HttpClientUtil.READ_TIME_OUT);
                    urlConnection.setConnectTimeout(HttpClientUtil.CONNECTE_TIME_OUT);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("connection", "close");
                    int e = urlConnection.getResponseCode();
                    String line;
                    if(e >= 200 && e < 400) {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                        line = null;

                        while((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }

                        callback.onSuccess(response.toString());
                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream(), "UTF-8"));
                        line = null;

                        while((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }

                        callback.onError(response.toString());
                    }
                } catch (SocketTimeoutException var19) {
                    var19.printStackTrace();
                    callback.onError(var19.getMessage());
                } catch (IOException var20) {
                    var20.printStackTrace();
                    callback.onError(var20.getMessage());
                } finally {
                    if(bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException var17) {
                            var17.printStackTrace();
                        }
                    }

                }

            }
        });
    }

    static {
        sExecutorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    public interface HttpCallback<T> {
        void onSuccess(T var1);

        void onError(T var1);
    }
}
