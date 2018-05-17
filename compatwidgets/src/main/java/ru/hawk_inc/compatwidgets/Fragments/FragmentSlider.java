package ru.hawk_inc.compatwidgets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hawk_inc.compatwidgets.R;
import ru.hawk_inc.compatwidgets.Widgets.Slider;
import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 2/22/2018.
 */

public class FragmentSlider extends WidgetFragment {
    TextView value;
    Slider slider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slider, container, false);

        name = (TextView)view.findViewById(R.id.name);
        value = (TextView)view.findViewById(R.id.value);
        slider = (Slider)view.findViewById(R.id.slider);

        slider.addListener(new Slider.OnSliderChangeListener() {
            @Override
            public void OnChange(Slider slide, float newValue, boolean fromUser) {
                value.setText(Math.round(newValue) + "");
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
                    slider.setEnabled(Boolean.parseBoolean(property.value));
                    break;
                case "color":
                    slider.setColor(Integer.parseInt(property.value));
                    break;
                case "max_value":
                    slider.setMaxValue(Integer.parseInt(property.value));
                    break;
                case "background":
                    slider.setBackground(Integer.parseInt(property.value));
                    break;
                case "orientation":
                    slider.setOrientation(Integer.parseInt(property.value));
                    break;
            }
        }
    }
}
