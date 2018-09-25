package net.sourceforge.simcpux.asynctask;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import net.sourceforge.simcpux.log.L;
import net.sourceforge.simcpux.net.OkHttpMananger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadTask extends AsyncTask<String, Integer, Integer> {
    private final DownloadLisenter lisenter;
    private static final String TAG = "DownloadTask";
    public static final int STATE_FAILE = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_PAUSE = 2;
    public static final int STATE_CANCEL = 3;
    private boolean isPaused;
    private boolean isCancel;

    public DownloadTask(DownloadLisenter lisenter) {
        this.lisenter = lisenter;
    }

    public interface DownloadLisenter {
        void onProgress(int progress);

        void onSuccess();

        void onFail();

        void onPause();

        void onCancel();
    }

    public void pause() {
        isPaused = true;
    }

    public void cancel() {
        isCancel = true;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String downloadURL = strings[0];
        String fileName = Uri.parse(downloadURL).getLastPathSegment();
        File dir = new File(Environment.getExternalStorageDirectory(), "FirstCoding/download");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File downloadFile = new File(dir, fileName);
        long downloadLength = 0;
        if (downloadFile != null) {
            downloadLength = downloadFile.length();
        }
        long contentLength = getContentLength(downloadURL);
        //排除特殊情况
        if (contentLength == 0) {
            return STATE_FAILE;
        }

        if (downloadLength == contentLength) {
            return STATE_SUCCESS;
        }

        //开始下载
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadURL).header("RANGE", "bytes=" + downloadLength + "-").build();
        InputStream inputStream = null;
        RandomAccessFile raf = null;
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                inputStream = response.body().byteStream();
                raf = new RandomAccessFile(downloadFile, "rw");
                raf.seek(downloadLength);
                byte[] buffer = new byte[1024];
                int len = -1;
                long total = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    if (isPaused) {
                        return STATE_PAUSE;
                    }

                    if (isCancel) {
                        return STATE_CANCEL;
                    }

                    raf.write(buffer, 0, len);
                    total += len;
                    publishProgress((int) ((downloadLength + total) * 100 / contentLength));
                }
                return STATE_SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
            L.e(TAG, e.toString());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (raf != null) {
                    try {
                        raf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //任务取消后删除文件
                if (isCancel && downloadFile != null) {
                    downloadFile.delete();
                }
            }
        }
        return STATE_FAILE;
    }

    private long getContentLength(String downloadURL) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadURL).build();
        try {
            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                return response.body().contentLength();
            }
        } catch (IOException e) {
            e.printStackTrace();
            L.e(TAG, e.toString());
        }
        return 0;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        lisenter.onProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer state) {
        super.onPostExecute(state);
        switch (state){
            case STATE_CANCEL:
                lisenter.onCancel();
                break;
            case STATE_PAUSE:
                lisenter.onPause();
                break;
            case STATE_FAILE:
                lisenter.onFail();
                break;
            case STATE_SUCCESS:
                lisenter.onSuccess();
                break;
        }
    }
}
