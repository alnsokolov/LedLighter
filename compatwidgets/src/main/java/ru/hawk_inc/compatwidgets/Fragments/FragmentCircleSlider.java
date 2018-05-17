package ru.hawk_inc.compatwidgets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hawk_inc.compatwidgets.R;
import ru.hawk_inc.compatwidgets.Widgets.CircleSlider;
import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 2/22/2018.
 */

public class FragmentCircleSlider extends WidgetFragment {
    TextView value;
    CircleSlider slider;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);

        name = (TextView)view.findViewById(R.id.name);
        value = (TextView)view.findViewById(R.id.value);
        slider = (CircleSlider)view.findViewById(R.id.circleSlider);

        slider.addListener(new CircleSlider.OnSliderChangeListener() {
            @Override
            public void OnValueChanged(CircleSlider slider, float newValue, boolean fromUser) {
                value.setText(Math.round(newValue) + "");
            }

            @Override
            public void OnStartTrackingTouch(CircleSlider circleSlider) {

            }

            @Override
            public void OnStopTrackingTouch(CircleSlider circleSlider) {

            }
        });

        return view;
    }

    @Override
    public void link(Widget widget) {
        super.link(widget);
        for(Widget.Property property : widget.properties){
            switch (property.key){
                case "max_value":
                    slider.setMaxValue(Integer.parseInt(property.value));
                    break;
                case "min_value":
                    slider.setMinValue(Integer.parseInt(property.value));
                    break;
                case "color":
                    slider.setColor(Integer.parseInt(property.value));
                    break;
                case "enabled":
                    slider.setEnabled(Boolean.parseBoolean(property.value));
                    break;
            }
        }
    }
}
