package com.rey.chameleon.theme;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.rey.chameleon.ChameleonConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rey on 10/25/2016.
 */
public class ThemeManager {

    private volatile static ThemeManager mInstance;

    public static final int STYLE_UNDEFINED = 0;

    private SparseArray<int[]> mStyles =  new SparseArray<>();
    private int mCurrentTheme = 0;
    private List<OnThemeChangedListener> mListeners = new ArrayList<>();

    private ViewStyler mViewStyler = new ViewStyler();

    public interface OnThemeChangedListener{

        void onThemeChanged(@Nullable OnThemeChangedEvent event);

    }

    /**
     * Get the styleId from attributes.
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     * @return The styleId.
     */
    public static int getStyleId(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray a = context.obtainStyledAttributes(attrs, new int[]{ChameleonConfig.get().getStyleAttrId()}, defStyleAttr, defStyleRes);
        int styleId = a.getResourceId(0, 0);
        a.recycle();
        return styleId;
    }

    /**
     * Get the singleton instance of ThemeManager.
     * @return The singleton instance of ThemeManager.
     */
    public static ThemeManager getInstance(){
        if(mInstance == null){
            synchronized (ThemeManager.class){
                if(mInstance == null)
                    mInstance = new ThemeManager();
            }
        }

        return mInstance;
    }

    private int[] loadStyleList(Context context, int resId){
        if(context == null)
            return null;

        TypedArray array = context.getResources().obtainTypedArray(resId);
        int[] result = new int[array.length()];
        for(int i = 0; i < result.length; i++)
            result[i] = array.getResourceId(i, 0);
        array.recycle();

        return result;
    }

    private int[] getStyleList(Context context, int styleId){
        if(mStyles == null)
            return null;

        int[] list = mStyles.get(styleId);
        if(list == null){
            list = loadStyleList(context, styleId);
            mStyles.put(styleId, list);
        }

        return list;
    }

    private void dispatchThemeChanged(int theme){
        OnThemeChangedEvent event = new OnThemeChangedEvent(theme);
        for(int i = mListeners.size() - 1; i >= 0; i--)
            mListeners.get(i).onThemeChanged(event);
    }

    /**
     * Get the current theme.
     * @return The current theme.
     */
    public int getCurrentTheme(){
        return mCurrentTheme;
    }

    /**
     * Set the current theme. Should be called in main thread (UI thread).
     * @param theme The current theme.
     * @return True if set theme successfully, False if method's called on main thread or theme already set.
     */
    public boolean setCurrentTheme(int theme){
        if (Looper.getMainLooper().getThread() != Thread.currentThread())
            return false;

        if(mCurrentTheme != theme){
            mCurrentTheme = theme;
            dispatchThemeChanged(mCurrentTheme);
            return true;
        }

        return false;
    }

    /**
     * Get current style of a styleId.
     * @param context The context.
     * @param styleId The styleId.
     * @return The current style.
     */
    public int getCurrentStyle(Context context, int styleId){
        return getStyle(context, styleId, mCurrentTheme);
    }

    /**
     * Get a specific style of a styleId.
     * @param context The context.
     * @param styleId The styleId.
     * @param theme The theme.
     * @return The specific style.
     */
    public int getStyle(Context context, int styleId, int theme){
        int[] styles = getStyleList(context, styleId);
        return styles == null ? 0 : styles[theme];
    }

    /**
     * Register a listener will be called when current theme changed.
     * @param listener A {@link OnThemeChangedListener} will be registered.
     */
    public void registerOnThemeChangedListener(@NonNull OnThemeChangedListener listener){
        boolean exist = false;
        for(int i = mListeners.size() - 1; i >= 0; i--){
            if(mListeners.get(i) == listener)
                exist = true;
        }
        if(!exist)
            mListeners.add(listener);
    }

    /**
     * Unregister a listener from be called when current theme changed.
     * @param listener A {@link OnThemeChangedListener} will be unregistered.
     */
    public void unregisterOnThemeChangedListener(@NonNull OnThemeChangedListener listener){
        for(int i = mListeners.size() - 1; i >= 0; i--){
            if(mListeners.get(i) == listener)
                mListeners.remove(i);
        }
    }

    public void applyStyle(View view, int styleRes){
        mViewStyler.applyStyle(view, styleRes);
    }

}
