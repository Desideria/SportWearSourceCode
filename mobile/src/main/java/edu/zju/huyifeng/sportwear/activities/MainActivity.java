package edu.zju.huyifeng.sportwear.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.view.menu.MenuBuilder;
import android.support.v7.widget.ActionMenuPresenter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v4.app.FragmentManager;

import com.google.android.gms.wearable.DataMap;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.mariux.teleport.lib.TeleportClient;

import edu.zju.huyifeng.sportwear.R;
import edu.zju.huyifeng.sportwear.constants.Constant;
import edu.zju.huyifeng.sportwear.db.DatabaseHelper;
import edu.zju.huyifeng.sportwear.db.bean.BikeDB;
import edu.zju.huyifeng.sportwear.db.bean.BikeLocationDB;
import edu.zju.huyifeng.sportwear.db.bean.RunDB;
import edu.zju.huyifeng.sportwear.db.bean.RunLocationDB;
import edu.zju.huyifeng.sportwear.db.bean.WalkDB;
import edu.zju.huyifeng.sportwear.db.bean.WalkLocationDB;
import edu.zju.huyifeng.sportwear.service.LocationTraceService;


public class MainActivity extends ActionBarActivity implements NavigationDrawerCallbacks {

    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private Bundle bundle;
    private Menu menu;
    private int menu_position;

    private LocationTraceService mLocationTraceService;

    TeleportClient mTeleportClient;
    TeleportClient.OnSyncDataItemTask mOnSyncDataItemTask;

    private RuntimeExceptionDao<WalkDB, Integer> mWalkDao = null;
    private RuntimeExceptionDao<RunDB, Integer> mRunDao = null;
    private RuntimeExceptionDao<BikeDB, Integer> mBikeDao = null;
    private WalkDB mWalkDB = null;
    private RunDB mRunDB = null;
    private BikeDB mBikeDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.include_toolbar_default);
        mToolbar.setTitle(getString(R.string.menu_sport_note));
        menu_position = 0;
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        Intent intent = new Intent(this, LocationTraceService.class);
        startService(intent);

        //instantiate the TeleportClient with the application Context
        mTeleportClient = new TeleportClient(this);

        //Create and initialize task
        mOnSyncDataItemTask = new SaveDataFromWear();


        //let's set the two task to be executed when an item is synced or a message is received
        mTeleportClient.setOnSyncDataItemTask(mOnSyncDataItemTask);
    }

    /**
     * 改变toolbar的菜单项
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        switch (menu_position) {
            case 0:
                getMenuInflater().inflate(R.menu.menu_main, menu);
                mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_month:
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, MonthNoteActivity.class);
                                startActivity(intent);

                        }
                        return false;
                    }
                });
                break;
            case 1:
                getMenuInflater().inflate(R.menu.menu_friend, menu);
                mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_add_friend:
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, AddFriendActivity.class);
                                startActivity(intent);
                        }
                        return false;
                    }
                });
                break;
            case 2:
                getMenuInflater().inflate(R.menu.menu_train, menu);
                mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.action_add_train:
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, AddTrainActivity.class);
                                startActivity(intent);
                        }
                        return false;
                    }
                });
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        menu_position = position;
        FragmentManager fragmentManager = getSupportFragmentManager();

        bundle = new Bundle();
        bundle.putInt("section_number", position);
        switch (position) {
            case 0:
                SportNoteFragment sportNoteFragment = new SportNoteFragment();

                sportNoteFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, sportNoteFragment).commit();
                break;
            case 1:
                FriendFragment friendFragment = new FriendFragment();
                friendFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, friendFragment).commit();
                break;
            case 2:
                TrainFragment trainFragment = new TrainFragment();
                trainFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, trainFragment).commit();
                break;
            case R.id.iv_user_photo:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, PersonActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent();
                intent.setClass(MainActivity.this, Pedometer.class);
                startActivity(intent);
                break;
            default:
                break;
        }


    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mToolbar.setTitle(getString(R.string.menu_sport_note));
                break;
            case 1:
                mToolbar.setTitle(getString(R.string.menu_friends));
                break;
            case 2:
                mToolbar.setTitle(getString(R.string.menu_train));
                break;
        }
    }


    private class SaveDataFromWear extends TeleportClient.OnSyncDataItemTask{
        @Override
        protected void onPostExecute(DataMap result) {
//            result.get(Constant.STEP)
//            setData(result.get(Constant.STEP));
        }
    }

    private void setData(int steps, int time, int calorie, int maxSpeed, int distance){
        mWalkDao = DatabaseHelper.getHelper(this).getWalkDao();
        mWalkDB = new WalkDB(steps,time,calorie,maxSpeed,distance);
        mWalkDao.create(mWalkDB);
    }

}
