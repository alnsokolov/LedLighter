package ru.hawk_inc.ledlighter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

public class ScanActivity extends AppCompatActivity {

    TextView cancel;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width * .85), (int)(height * .9));

        //TODO: Some come to turn scanning on
        // (maybe passed to singletone with a handler on finding)

        mRecyclerView = (RecyclerView)findViewById(R.id.scan_list);
        cancel = (TextView)findViewById(R.id.scan_text_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
