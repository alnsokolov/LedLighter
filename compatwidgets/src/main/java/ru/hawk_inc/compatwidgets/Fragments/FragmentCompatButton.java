package ru.hawk_inc.compatwidgets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import ru.hawk_inc.compatwidgets.Widgets.Widget.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hawk_inc.compatwidgets.R;
import ru.hawk_inc.compatwidgets.Widgets.CompatButton;
import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 2/22/2018.
 */

public class FragmentCompatButton extends WidgetFragment {
    CompatButton button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);

        //TODO:Code for color changing and communication with Bluetooth
        name = (TextView)view.findViewById(R.id.name);
        button = (CompatButton)view.findViewById(R.id.compatButton);

        button.addListener(new CompatButton.OnCompatButtonClickListener() {
            @Override
            public void OnClick(CompatButton button, boolean isOn) {
                //TODO:Code for communication with bluetooth
            }
        });

        return view;
    }

    @Override
    public void link(Widget widget) {
        super.link(widget);

        for(Property property : widget.properties){
            switch (property.key){
                case "enabled":
                    button.setEnabled(Boolean.parseBoolean(property.value));
                    break;
                case "color":
                    button.setColor(Integer.parseInt(property.value));
                    break;
                case "toggle":
                    button.setToggled(Boolean.parseBoolean(property.value));
                    break;
            }
        }
    }
}
