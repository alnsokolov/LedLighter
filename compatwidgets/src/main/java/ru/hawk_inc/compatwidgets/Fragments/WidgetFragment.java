package ru.hawk_inc.compatwidgets.Fragments;


import android.support.v4.app.Fragment;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 4/22/2018.
 */

public abstract class WidgetFragment extends Fragment {
    protected Widget widget;
    protected TextView name;
    protected RelativeLayout.LayoutParams mLayoutParams;

    public void link(Widget widget, float scaleX, float scaleY){
        this.widget = widget;

        if(name != null)
            name.setText(widget.mName);

        mLayoutParams = new RelativeLayout.LayoutParams((int)(widget.mWidth*scaleX), (int)(widget.mHeight*scaleY));
        getView().setLayoutParams(mLayoutParams);
    }
}
