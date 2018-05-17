package ru.hawk_inc.compatwidgets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hawk_inc.compatwidgets.R;
import ru.hawk_inc.compatwidgets.Widgets.Joystick;
import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 2/22/2018.
 */

public class FragmentJoystick extends WidgetFragment {
    TextView X, Y;
    Joystick joystick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joystick, container, false);

        name = (TextView)view.findViewById(R.id.name);
        X = (TextView)view.findViewById(R.id.x);
        Y = (TextView)view.findViewById(R.id.y);
        joystick = (Joystick)view.findViewById(R.id.joystick);

        joystick.addListener(new Joystick.OnJoystickTouchListener() {
            @Override
            public void OnJoystickTouch(float x, float y, boolean in) {
                X.setText(Math.round(x)+""); Y.setText(Math.round(y)+"");
                //TODO:Code for communication with Bluetooth
            }
        });

        return view;
    }

    @Override
    public void link(Widget widget) {
        super.link(widget);
        for(Widget.Property property : widget.properties){
            switch (property.key){
                case "enabled":
                    joystick.setEnabled(Boolean.parseBoolean(property.value));
                    break;
                case "color":
                    joystick.setColor(Integer.parseInt(property.value));
                    break;
                case "max_value":
                    joystick.setMaxValue(Integer.parseInt(property.value));
                    break;
                case "min_value":
                    joystick.setMinValue(Integer.parseInt(property.value));
                    break;
                case "sticky":
                    joystick.setSticky(Boolean.parseBoolean(property.value));
                    break;
            }
        }
    }
}
