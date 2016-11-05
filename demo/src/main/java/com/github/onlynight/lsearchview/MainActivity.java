package com.github.onlynight.lsearchview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.onlynight.library.lsearchview.LSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LSearchView searchView;
    private ListView listView;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (LSearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.listView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("LSearchViewDemo");

        searchView.getSearchButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setSearching(true);
                searchView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchView.setSearching(false);
                        fillListViewData();
                    }
                }, 1000);
            }
        });
        searchView.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackSearch();
            }
        });
        initListView();
    }

    private void initListView() {
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuSearch) {
            searchView.showWithAnim();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isShown()) {
            onBackSearch();
        } else {
            super.onBackPressed();
        }
    }

    private void onBackSearch(){
        searchView.hideWithAnim();
        adapter.clear();
        adapter.notifyDataSetChanged();
    }

    private void fillListViewData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add("data" + (i + 1));
        }
        adapter.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
