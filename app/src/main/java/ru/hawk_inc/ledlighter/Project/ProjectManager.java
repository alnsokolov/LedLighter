package ru.hawk_inc.ledlighter.Project;

import java.util.ArrayList;

import ru.hawk_inc.compatwidgets.Widgets.Widget;

/**
 * Created by Admin on 5/1/2018.
 */

public class ProjectManager {
    private static ProjectManager curManager = null;
    private Project mProject;

    public static Project getCurInstance(String name){
        if(curManager == null)
            curManager = new ProjectManager(name);
        return curManager.getProject();
    }

    private ProjectManager(String name){
        //TODO: Get result drom db
        mProject = new Project(name);
    }

    public Project getProject() {
        return mProject;
    }

    private ArrayList<Widget> getWidgets(String name){
        return mProject.widgets;
    }
}
