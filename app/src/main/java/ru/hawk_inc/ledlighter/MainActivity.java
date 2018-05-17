package ru.hawk_inc.ledlighter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import ru.hawk_inc.blurry_v2.Blurry;

public class MainActivity extends AppCompatActivity {

    private boolean blurred = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_help:
                //TODO: Some code for helping user get around the app
                break;
            case R.id.menu_add:
                Intent add = new Intent(this, NewProjectActivity.class);
                startActivity(add);
                break;
            case R.id.menu_connect:
                Blurry.with(this)
                        .color(Color.argb(66, 0, 0, 0))
                        .radius(10)
                        .sampling(2)
                        .animate(200)
                        .async()
                        .onto((ViewGroup)findViewById(android.R.id.content));
                blurred = true;
                Intent connect = new Intent(this, ConnectActivity.class);
                startActivity(connect);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        if(blurred){
            Blurry.delete((ViewGroup)findViewById(android.R.id.content), 400);
            blurred = false;
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
