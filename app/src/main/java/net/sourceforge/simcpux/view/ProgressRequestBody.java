package net.sourceforge.simcpux.view;

import android.os.Handler;
import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class ProgressRequestBody extends RequestBody {
    private RequestBody requestBody;
    private ProgressRequestListener listener;
    private BufferedSink bufferedSink;
    private Handler handler = new Handler();

    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener listener) {
        this.requestBody = requestBody;
        this.listener = listener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }

        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                if (contentLength == 0) {
                    contentLength = contentLength();
                }

                bytesWritten += byteCount;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onProgress(bytesWritten, contentLength);
                    }
                });
            }
        };
    }


    public interface ProgressRequestListener {
        void onProgress(long bytesWritten, long contentLength);
    }
}
