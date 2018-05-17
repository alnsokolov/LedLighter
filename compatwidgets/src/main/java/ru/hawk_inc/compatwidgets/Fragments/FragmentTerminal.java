package ru.hawk_inc.compatwidgets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.hawk_inc.compatwidgets.R;
import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 2/24/2018.
 */

public class FragmentTerminal extends WidgetFragment {
    private TextView text;
    private EditText input;
    private ImageButton button;
    private boolean mEnabled;
    private int mColor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);

        name = (TextView)view.findViewById(R.id.name);
        text = (TextView)view.findViewById(R.id.text);
        input = (EditText)view.findViewById(R.id.textInput);
        button = (ImageButton)view.findViewById(R.id.buttonSend);

        text.setMovementMethod(new ScrollingMovementMethod());
        input.setImeActionLabel("Go!", KeyEvent.KEYCODE_ENTER);
        input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER
                        && !input.getText().toString().equals("")){
                    text.setText(text.getText() + " > " + input.getText().toString() + "\n");
                    text.setScrollY(Math.max(text.getLineCount()*text.getLineHeight() - text.getHeight(),0));
                    input.setText("");
                }
                return true;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!input.getText().toString().equals("")) {
                    text.setText(text.getText() + " > " + input.getText().toString() + "\n");
                    text.setScrollY(Math.max(text.getLineCount()*text.getLineHeight() - text.getHeight(),0));
                    input.setText("");
                }
            }
        });

        return view;
    }

    @Override
    public void link(Widget widget) {
        super.link(widget);
        for(Widget.Property property : widget.properties) {
            switch (property.key) {
                case "enabled":
                    mEnabled = Boolean.parseBoolean(property.value);
                    break;
                case "color":
                    mColor = (Integer.parseInt(property.value));
                    break;
            }
        }
    }
}
