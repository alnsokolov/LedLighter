package ru.hawk_inc.ledlighter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import ru.hawk_inc.ledlighter.Project.ProjectActivity;

public class NewProjectActivity extends AppCompatActivity {

    private TextView mCancel, mCreate;
    private EditText mTitle;
    private SwitchCompat mMode, mTheme;

    String title = "";
    boolean mode, theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTitle = (EditText)findViewById(R.id.new_name);
        mMode = (SwitchCompat)findViewById(R.id.new_mode_switch);
        mTheme = (SwitchCompat)findViewById(R.id.new_theme);
        mCancel = (TextView)findViewById(R.id.new_text_cancel);
        mCreate = (TextView)findViewById(R.id.new_text_create);

        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mode = b;
            }
        });
        mTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                theme = b;
            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Some code to create new project
                if(!title.equals("")) {
                    Intent intent = new Intent(NewProjectActivity.this, ProjectActivity.class);
                    intent.putExtra("name", title);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
