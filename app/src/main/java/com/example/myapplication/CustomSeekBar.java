package com.example.myapplication;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatSeekBar;
public class CustomSeekBar extends AppCompatSeekBar {

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);

        // 使用a.getDrawable()来获取自定义drawable
        Drawable progressDrawable = a.getDrawable(R.styleable.CustomSeekBar_android_progressDrawable);
        if (progressDrawable != null) {
            setProgressDrawable(progressDrawable);
        }

        // 记得回收TypedArray
        a.recycle();
    }
}
//
//public class CustomSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
//
//    public CustomSeekBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        // 获取自定义属性
//        TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.CustomSeekBar, 0, 0);
//        Drawable progressDrawable = a.getDrawable(R.styleable.CustomSeekBar_android_progressDrawable);
//        Drawable thumb = a.getDrawable(R.styleable.CustomSeekBar_android_thumb);
//        a.recycle();
//
//        // 设置SeekBar的进度drawable和thumb
//        if (progressDrawable != null) {
//            setProgressDrawable(progressDrawable);
//        }
//        if (thumb != null) {
//            setThumb(thumb);
//        }
//    }
//}
//public class CustomSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {
//    public CustomSeekBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//    }
//    private void init(AttributeSet attrs) {
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);
//
//        Drawable progressDrawable = a.getDrawable(R.styleable.CustomSeekBar_android_progressDrawable);
//        if (progressDrawable != null) {
//            setProgressDrawable(progressDrawable);
//        }
//
//        a.recycle();
//    }
//    private void init(AttributeSet attrs) {
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);
//        // 检查新属性是否已经设置，并获取它的值
//        int progressTint = a.getColor(R.styleable.CustomSeekBar_progressTint, 0xFFD700); // 默认黄色
//
//        // 根据 progressTint 创建一个新的 Drawable 或修改现有的 progressDrawable
//        // ...
//
//        a.recycle();
//    }

//    public CustomSeekBar(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(attrs);
//    }
//
//    private void init(AttributeSet attrs) {
//        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomSeekBar);
//
//        // 使用a.getDrawable()来获取自定义drawable
//        Drawable progressDrawable = a.getDrawable(R.styleable.CustomSeekBar_android_progressDrawable);
//        // 设置progressDrawable
//        setProgressDrawable(progressDrawable);
//
//        // 记得回收TypedArray
//        a.recycle();
//    }
//}