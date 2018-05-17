package ru.hawk_inc.ledlighter.Project;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.KillerBLS.modpeide.processor.language.ModPELanguage;

import ru.hawk_inc.ledlighter.R;

public class ProjectActivity extends AppCompatActivity {

    private NavigationView mElements, mMode;
    private DrawerLayout mLayout;
    private Toolbar mToolbar;

    private FragmentManager fm;
    private Fragment container;
    private CodeFragment fCode;
    private SchemeFragment fScheme;

    private Project project;
    private String name;

    boolean playing = false, currentIsScheme = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        name = getIntent().getStringExtra("name");
        project = ProjectManager.getCurInstance(name);

        initUI();
        initFragment();
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        container = fm.findFragmentById(R.id.project_content_frame);

        fCode = new CodeFragment();
        fCode.setLanguage(new ModPELanguage());
        fScheme = new SchemeFragment();

        if(container == null) {
            container = fScheme;
            fm.beginTransaction()
                    .add(R.id.project_content_frame, container)
                    .commit();
        }
    }

    private void initUI(){
        mElements = (NavigationView)findViewById(R.id.nav_elements);
        mMode = (NavigationView)findViewById(R.id.nav_mode);
        mLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mToolbar = (Toolbar)findViewById(R.id.project_toolbar);

        mToolbar.setTitle(project.name);
        setSupportActionBar(mToolbar);
        mElements.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_elements_button:
                        break;
                    case R.id.nav_elements_slider:
                        break;
                    case R.id.nav_elements_circle:
                        break;
                    case R.id.nav_elements_joystick:
                        break;
                    case R.id.nav_elements_terminal:
                        break;
                    case R.id.nav_elements_grid:
                        break;
                }
                item.setChecked(false);
                mLayout.closeDrawers();
                return true;
            }
        });
        mMode.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_mode_code:
                        mLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mElements);

                        if(currentIsScheme) {
                            fm.beginTransaction()
                                    .remove(container)
                                    .commit();
                            fm.beginTransaction()
                                    .replace(R.id.project_content_frame, fCode)
                                    .commit();
                            mToolbar.getMenu().clear();
                            getMenuInflater().inflate(R.menu.project_code_menu, mToolbar.getMenu());
                        }

                        currentIsScheme = false;
                        break;
                    case R.id.nav_mode_scheme:
                        mLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mElements);

                        if(!currentIsScheme) {
                            fm.beginTransaction()
                                    .remove(container)
                                    .commit();
                            fm.beginTransaction()
                                    .replace(R.id.project_content_frame, fScheme)
                                    .commit();
                            mToolbar.getMenu().clear();
                            getMenuInflater().inflate(R.menu.project_scheme_menu, mToolbar.getMenu());
                        }

                        currentIsScheme = true;
                        break;
                }
                item.setChecked(true);
                mLayout.closeDrawers();
                return true;
            }
        });
        mMode.setCheckedItem(R.id.nav_mode_scheme);
    }

    private void stop(){
        mLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        mToolbar.getMenu().findItem(R.id.scheme_play)
                .setIcon(R.drawable.menu_play).setTitle(R.string.scheme_run);
    }

    private void run(){
        mLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mToolbar.getMenu().findItem(R.id.scheme_play)
                .setIcon(R.drawable.menu_stop).setTitle(R.string.scheme_stop);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.project_scheme_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.scheme_add:
                if(currentIsScheme && !playing)
                    mLayout.openDrawer(mElements);
                break;
            case R.id.scheme_settings:
                break;
            case R.id.scheme_play:
                if(playing)
                    stop();
                else
                    run();
                playing = !playing;
                break;
            case R.id.code_save:
                fCode.saveFile();
                break;
            case R.id.code_compile:
                break;
            case R.id.code_upload:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(playing){
            stop();
            playing = !playing;
        }
        else
            super.onBackPressed();
    }
}
