package ru.hawk_inc.compatwidgets.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.hawk_inc.compatwidgets.R;
import ru.hawk_inc.compatwidgets.Widgets.CompatGrid;
import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 2/22/2018.
 */

public class FragmentCompatGrid extends WidgetFragment {
    TextView columns, rows;
    CompatGrid grid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);

        name = (TextView)view.findViewById(R.id.name);
        columns = (TextView)view.findViewById(R.id.column);
        rows = (TextView)view.findViewById(R.id.row);
        grid = (CompatGrid) view.findViewById(R.id.compatGrid);

        grid.addListener(new CompatGrid.OnGridChangeListener() {
            @Override
            public void OnClick(int row, int column, CompatGrid.Tile tile, boolean pressed) {
                columns.setText(column+""); rows.setText(row+"");
                //TODO: Code for communication
            }
        });

        return view;
    }

    @Override
    public void link(Widget widget) {
        super.link(widget);
        for(Widget.Property property : widget.properties){
            switch (property.key){
                case "color":
                    grid.setColor(Integer.parseInt(property.value));
                    break;
                case "toggle":
                    grid.setToggle(Boolean.parseBoolean(property.value));
                    break;
                case "cols":
                    grid.setColor(Integer.parseInt(property.value));
                    break;
                case "rows":
                    grid.setRows(Integer.parseInt(property.value));
                    break;
                case "enabled":
                    grid.setEnabled(Boolean.parseBoolean(property.value));
                    break;
            }
        }
    }
}
