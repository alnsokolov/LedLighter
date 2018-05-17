package ru.hawk_inc.compatwidgets.Widgets;

import java.util.ArrayList;

/**
 * Created by Admin on 4/22/2018.
 */

public abstract class Widget {
    public enum Type{
        CIRCLE_SLIDER,
        BUTTON,
        GRID,
        JOYSTICK,
        SLIDER,
        TERMINAL
    }
    
    public String mName;
    public String mType;
    public ArrayList<Property> properties;
    public int mHeight, mWidth;
    public int x, y;
    
    public Widget(String name, Type type, int x, int y){
        mName = name;               this.x = x;
        mType = type.toString();    this.y = y;
        
        properties = new ArrayList<>();

        switch (type){
            case GRID:
                mHeight = 3;
                mWidth =  3;
                break;
            case BUTTON:
                mHeight = 2;
                mWidth =  2;
                break;
            case SLIDER:
                mHeight = 1;
                mWidth  = 2;
                break;
            case JOYSTICK:
                mHeight = 3;
                mWidth  = 3;
                break;
            case CIRCLE_SLIDER:
                mHeight = 3;
                mWidth  = 3;
                break;
            case TERMINAL:
                mHeight = 3;
                mWidth  = 4;
        }
    }

    public Widget(String name, Type type, int x, int y, int height, int width){
        this(name, type, x, y);

        mHeight = height;
        mWidth = width;
    }
    
    public void setProperty(String key, String value){
        for(Property property : properties)
            if(property.key.equals(key)) {
                property.value = value;
                return;
            }
        
        properties.add(new Property(key, value));
    }

    public Type getType() {
        return Type.valueOf(mType);
    }

    public void setType(Type type) {
        mType = type.toString();
    }

    public class Property{
        public String key, value;

        public Property(String key, String value){
            this.key = key;
            this.value = value;
        }
    }

    public interface WidgetListener{
        void onWidgetChanged(String parameter, String value);
    }
}


