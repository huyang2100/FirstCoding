package net.sourceforge.simcpux.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import net.sourceforge.simcpux.constant.ConstantFile;
import net.sourceforge.simcpux.log.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileUtil {
    private static final String TAG = "FileUtil";

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    /**
     * 压缩图片到指定大小
     * @param imgFile 文件file
     * @param reqWidth 期望的宽
     * @param reqHeight 期望的高
     * @return 压缩后的file
     * @throws FileNotFoundException
     */
    public static File compress(File imgFile, int reqWidth, int reqHeight) throws FileNotFoundException {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(), opts);
        L.i(TAG, "w: " + opts.outWidth + "/h:" + opts.outHeight);

        int inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        L.i(TAG, "inSampleSize: " + inSampleSize);
        opts.inSampleSize = inSampleSize;
        opts.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), opts);
        File dir = new File(Environment.getExternalStorageDirectory(), "temp");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File tempFile = new File(dir, "temp.jpg");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, new FileOutputStream(tempFile));
        return tempFile;
    }

    public static File compress(File imgFile) throws FileNotFoundException {
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        File dir = new File(Environment.getExternalStorageDirectory(), "temp");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File tempFile = new File(dir, "temp.jpg");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, new FileOutputStream(tempFile));
        return tempFile;
    }
}
