package com.cold.pkview.old;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.ImageViewTarget;


public class ImageHelper {


    /**
     * 加载圆形图片
     *
     * @param urlString
     * @param imageView
     * @param defaultRes
     */
    public static void loadCircleImage(Context context, String urlString, final ImageView imageView, final int defaultRes) {


        final Resources resources = context.getResources();

        Glide.with(context.getApplicationContext())
                .load(urlString)
                .bitmapTransform(new CropCircleTransformation(context))
                .crossFade()
                .into(new CircleViewTarget<GlideDrawable>(imageView, defaultRes, resources));


    }

    private static class CircleViewTarget<T extends Drawable> extends ImageViewTarget<T> {

        int defaultRes;
        Resources resources;


        public CircleViewTarget(ImageView view, int defaultRes, Resources resources) {
            super(view);
            this.defaultRes = defaultRes;
            this.resources = resources;
        }

        @Override
        protected void setResource(T resource) {
            setDrawable(resource);
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            setDrawable(getCircleDrawable(resources, defaultRes));
        }


        //加载失败 加载圆形的默认图
        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {

            setDrawable(getCircleDrawable(resources, defaultRes));
        }

    }

    public static Drawable getCircleDrawable(Resources resources, int defaultRes) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) resources.getDrawable(defaultRes);
        Bitmap bitmap = null;
        if (bitmapDrawable != null) {
            bitmap = bitmapDrawable.getBitmap();
        }

        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
        drawable.setCircular(true);
        return drawable;
    }

    public static class CropCircleTransformation implements Transformation<Bitmap> {

        private BitmapPool mBitmapPool;

        public CropCircleTransformation(Context context) {
            this(Glide.get(context).getBitmapPool());
        }

        public CropCircleTransformation(BitmapPool pool) {
            this.mBitmapPool = pool;
        }

        @Override
        public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
            Bitmap source = resource.get();
            int size = Math.min(source.getWidth(), source.getHeight());

            int width = (source.getWidth() - size) / 2;
            int height = (source.getHeight() - size) / 2;

            Bitmap bitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader =
                    new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            if (width != 0 || height != 0) {
                // source isn't square, move viewport to center
                Matrix matrix = new Matrix();
                matrix.setTranslate(-width, -height);
                shader.setLocalMatrix(matrix);
            }
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            return BitmapResource.obtain(bitmap, mBitmapPool);
        }

        @Override
        public String getId() {
            return "CropCircleTransformation()";
        }
    }
}
