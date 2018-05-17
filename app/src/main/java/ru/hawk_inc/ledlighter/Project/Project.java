package ru.hawk_inc.ledlighter.Project;

import java.util.ArrayList;

import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 5/1/2018.
 */

public class Project {
    public ArrayList<Widget> widgets;
    public String name = "";
    public String code = "";

    public Project(String name){
        this.name = name;
    }

    public void addWidget(Widget widget){
        widgets.add(widget);
    }

    public void removeWidget(Widget widget){
        widgets.remove(widget);
    }
}
