package ru.hawk_inc.ledlighter.Project;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import ru.hawk_inc.compatwidgets.Fragments.FragmentCircleSlider;
import ru.hawk_inc.compatwidgets.Fragments.FragmentCompatButton;
import ru.hawk_inc.compatwidgets.Fragments.FragmentCompatGrid;
import ru.hawk_inc.compatwidgets.Fragments.FragmentJoystick;
import ru.hawk_inc.compatwidgets.Fragments.FragmentSlider;
import ru.hawk_inc.compatwidgets.Fragments.FragmentTerminal;
import ru.hawk_inc.compatwidgets.Fragments.WidgetFragment;
import ru.hawk_inc.compatwidgets.Widgets.Widget;
import ru.hawk_inc.ledlighter.R;

/**
 * Created by Admin on 4/11/2018.
 */

public class SchemeFragment extends Fragment{
    private final int V_PARTS = 6, H_PARTS = 6;
    private Project mProject;
    private FrameLayout mLayout;

    private float scaleX, scaleY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scheme, container, false);
        mLayout = (FrameLayout) view.findViewById(R.id.scheme_layout);

        scaleX = mLayout.getWidth() / H_PARTS;
        scaleY = mLayout.getHeight() / V_PARTS;

        return view;
    }

    public void setProject(Project project){
        mProject = project;

        for(Widget widget : project.widgets){
            Widget.Type type = widget.getType();
            WidgetFragment widgetFragment = null;

            switch (type){
                case CIRCLE_SLIDER:
                    widgetFragment = new FragmentCircleSlider();
                    break;
                case JOYSTICK:
                    widgetFragment = new FragmentJoystick();
                    break;
                case SLIDER:
                    widgetFragment = new FragmentSlider();
                    break;
                case BUTTON:
                    widgetFragment = new FragmentCompatButton();
                    break;
                case GRID:
                    widgetFragment = new FragmentCompatGrid();
                    break;
                case TERMINAL:
                    widgetFragment = new FragmentTerminal();
                    break;
            }

            widgetFragment.link(widget, scaleX, scaleY);
            FragmentManager fm = getChildFragmentManager();
            fm.beginTransaction()
                    .add(R.id.scheme_layout, widgetFragment)
                    .commit();
        }
    }
}
