package com.example.syl.mydaotest;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Button mInsertButton;
//    private Button mDeleteButton;
    private ListView mRecordList;

    private SQLiteDatabase mDB;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private WeatherDao mWeatherDao;

    private Cursor mCursor;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weather-db", null);
        mDB= helper.getWritableDatabase();

        mDaoMaster = new DaoMaster(mDB);
        mDaoSession = mDaoMaster.newSession();
        mWeatherDao = mDaoSession.getWeatherDao();

        // insert button
        mInsertButton = (Button) findViewById(R.id.insert_button);
        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                String date_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

                Weather weather = new Weather(null, 20, "晴天", date, date_str);
                mWeatherDao.insert(weather);

                mCursor.requery();
                mAdapter.notifyDataSetChanged();

                //Log.d(TAG, "inserted Weather, ID=" + weather.getId());
                Toast.makeText(getApplicationContext(), "inserted Weather, ID="+weather.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        // listview
        String orderBy = WeatherDao.Properties.Weather.columnName;
        mCursor = mDB.query(mWeatherDao.getTablename(), mWeatherDao.getAllColumns(), null, null, null, null, orderBy);

        //SimpleDateFormat sdf = new SimpleDateFormat();

        String[] from = {WeatherDao.Properties.Weather.columnName, WeatherDao.Properties.Date_str.columnName};
        int[] to = {android.R.id.text1, android.R.id.text2};

        mAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_list_item_2,
                mCursor, from, to, 0);

        mRecordList = (ListView) findViewById(R.id.record_list);
        mRecordList.setAdapter(mAdapter);
        mRecordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mWeatherDao.deleteByKey(id);

                mCursor.requery();
                mAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "delete Weather, ID="+id, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
