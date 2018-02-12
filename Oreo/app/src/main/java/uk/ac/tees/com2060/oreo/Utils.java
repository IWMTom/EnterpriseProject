package uk.ac.tees.com2060.oreo;

import android.databinding.BindingAdapter;
import android.support.annotation.FontRes;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Switch;

public class Utils
{
    // Part of the fix for Google issue #63250768 relating to custom fonts for switches
    @BindingAdapter("labelTypeface")
    public static void setLabelTypeface(Switch view, @FontRes int id)
    {
        view.setTypeface(ResourcesCompat.getFont(view.getContext(), id));
    }
}
