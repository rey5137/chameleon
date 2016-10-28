package com.rey.chameleon;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Rey on 10/28/16.
 * For Chameleon.
 */
public class ChameleonContextWrapper extends ContextWrapper {

    private ChameleonLayoutInflater mInflater;

    private final int mAttributeId;

    /**
     * Uses the default configuration from {@link ChameleonConfig}
     *
     * Remember if you are defining default in the
     * {@link ChameleonConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap.
     * @return ContextWrapper to pass back to the activity.
     */
    public static ContextWrapper wrap(Context base) {
        return new ChameleonContextWrapper(base);
    }

    /**
     * You only need to call this <b>IF</b> you call
     * {@link ChameleonConfig.Builder#disablePrivateFactoryInjection()}
     * This will need to be called from the
     * {@link Activity#onCreateView(View, String, Context, AttributeSet)}
     * method to enable view font injection if the view is created inside the activity onCreateView.
     *
     * You would implement this method like so in you base activity.
     * <pre>
     * {@code
     * public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
     *   return ChameleonContextWrapper.onActivityCreateView(this, parent, super.onCreateView(parent, name, context, attrs), name, context, attrs);
     * }
     * }
     * </pre>
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @param parent   Parent view from onCreateView
     * @param view     The View Created inside onCreateView or from super.onCreateView
     * @param name     The View name from onCreateView
     * @param context  The context from onCreateView
     * @param attr     The AttributeSet from onCreateView
     * @return The same view passed in, or null if null passed in.
     */
    public static View onActivityCreateView(Activity activity, View parent, View view, String name, Context context, AttributeSet attr) {
        return get(activity).onActivityCreateView(parent, view, name, context, attr);
    }

    /**
     * Get the Calligraphy Activity Fragment Instance to allow callbacks for when views are created.
     *
     * @param activity The activity the original that the ContextWrapper was attached too.
     * @return Interface allowing you to call onActivityViewCreated
     */
    static ChameleonActivityFactory get(Activity activity) {
        if (!(activity.getLayoutInflater() instanceof ChameleonLayoutInflater)) {
            throw new RuntimeException("This activity does not wrap the Base Context! See ChameleonContextWrapper.wrap(Context)");
        }
        return (ChameleonActivityFactory) activity.getLayoutInflater();
    }

    /**
     * Uses the default configuration from {@link ChameleonConfig}
     *
     * Remember if you are defining default in the
     * {@link ChameleonConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base ContextBase to Wrap
     */
    ChameleonContextWrapper(Context base) {
        super(base);
        mAttributeId = ChameleonConfig.get().getFontPathAttrId();
    }

    /**
     * Override the default AttributeId, this will always take the custom attribute defined here
     * and ignore the one set in {@link ChameleonConfig}.
     *
     * Remember if you are defining default in the
     * {@link ChameleonConfig} make sure this is initialised before
     * the activity is created.
     *
     * @param base        ContextBase to Wrap
     * @param attributeId Attribute to lookup.
     * @deprecated use {@link #wrap(Context)}
     */
    @Deprecated
    public ChameleonContextWrapper(Context base, int attributeId) {
        super(base);
        mAttributeId = attributeId;
    }

    @Override
    public Object getSystemService(String name) {
        if (LAYOUT_INFLATER_SERVICE.equals(name)) {
            if (mInflater == null) {
                mInflater = new ChameleonLayoutInflater(LayoutInflater.from(getBaseContext()), this, mAttributeId, false);
            }
            return mInflater;
        }
        return super.getSystemService(name);
    }

}
