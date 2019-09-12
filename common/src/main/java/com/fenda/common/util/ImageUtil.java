package com.fenda.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.fenda.common.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author mirrer.wangzhonglin
 * @Time 2019/8/26  16:06
 * @Description 图片工具类
 */
public class ImageUtil {


    /**
     * 普通加载
     * @param url
     * @param imageView
     */
    public static void loadImage( String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆形图片
     * @param url
     * @param imageView
     */
    public static void loadCircleImage( String url, ImageView imageView) {
        RequestOptions options = new RequestOptions()
                .circleCrop()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆角图片
     * @param url
     * @param imageView
     */
    public static  void loadRoundImage( String url, ImageView imageView) {
        RequestOptions options = RequestOptions
                .bitmapTransform(new RoundedCorners(20))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);

        Glide.with(imageView.getContext()).load(url).apply(options).into(imageView);
    }

    /**
     * 加载图片并监听
     * @param url
     * @param imageView
     * @param listener
     */
    public static void loadImageWithResqust(String url,ImageView imageView,RequestListener listener){
        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
                .load(url)
                .apply(options)
                .listener(listener)
                .into(imageView);

    }
    /**
     * 加载GIF图片
     * @param resource
     * @param imageView
     */
    public static void loadGIFImage(int resource,ImageView imageView,int defaultImg){
        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(defaultImg)
                .error(defaultImg);
        Glide.with(imageView.getContext())
                .asGif()
                .load(resource)
                .apply(options)
                .into(imageView);



    }


    /**
     * 加载文件图片
     * @param file
     * @param imageView
     */
    public static void loadFileImage(File file, ImageView imageView){
        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher);
        Glide.with(imageView.getContext())
                .asFile()
                .load(file)
                .apply(options)
                .into(imageView);



    }

    /**
     * 加载可设置默认图片
     * @param v
     * @param url
     * @param defaultImg
     */
    public static void loadDefaultcImg(ImageView v, String url,int defaultImg) {

        RequestOptions options = RequestOptions
                .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                .placeholder(defaultImg)
                .error(defaultImg);

        Glide.with(v.getContext())
                .load(url)
                .apply(options)
                .into(v);
    }


    public static final String DirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + "imgs" + File.separator;

    public static void loadImg(Context context, ImageView v, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH);

        Glide.with(context)
                .load(url)
                .apply(options)
                .into(v);
    }


    public static File compressImage(File imageFile, float reqWidth, float reqHeight, Bitmap.CompressFormat compressFormat, int quality, String destinationPath) throws IOException {
        FileOutputStream fileOutputStream = null;
        String desPath = DirectoryPath + destinationPath;
        File file = new File(desPath).getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            fileOutputStream = new FileOutputStream(desPath);
            // write the compressed bitmap at the destination specified by destinationPath.
            Bitmap bmp = decodeSampledBitmapFromFile(imageFile, reqWidth, reqHeight);
            if (bmp != null) {
                bmp.compress(compressFormat, quality, fileOutputStream);
            } else {
                return null;
            }
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        return new File(desPath);
    }

    public static Bitmap decodeSampledBitmapFromFile(File imageFile, float reqWidth, float reqHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions

        Bitmap scaledBitmap = null, bmp = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        LogUtils.d("actualWidth ==>" + actualWidth + " ; " + "actualHeight ==>" + actualHeight);
        if (actualWidth <= 0 || actualHeight <= 0) {
            return null;
        }

        float imgRatio = (float) actualWidth / (float) actualHeight;
        float maxRatio = reqWidth / reqHeight;

        if (actualHeight > reqHeight || actualWidth > reqWidth) {
            //If Height is greater
            if (imgRatio < maxRatio) {
                imgRatio = reqHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) reqHeight;

            }  //If Width is greater
            else if (imgRatio > maxRatio) {
                imgRatio = reqWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) reqWidth;
            } else {
                actualHeight = (int) reqHeight;
                actualWidth = (int) reqWidth;
            }
        }

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        bmp.recycle();
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(),
                    scaledBitmap.getHeight(), matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.d("scaledBitmap-Width ==>" + scaledBitmap.getWidth() + " ; " + "scaledBitmap-Height ==>" + scaledBitmap.getHeight());

        return scaledBitmap;


    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            inSampleSize *= 2;
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }




}
