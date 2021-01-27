package com.sojan.androidrecyclerviewloadmore;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;

    MainActivityViewModel viewModel;
    int intStartNo = 1,intEndNo = 50;
    int page_count = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        recyclerView = findViewById(R.id.recyclerView);

        if(isNetworkAvailable(this)) {
            populateData();
        }else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

        initScrollListener();

    }

    private void populateData() {
        //sjn
        viewModel.getNums(intStartNo,intEndNo).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> heroList) {
                rowsArrayList.addAll(heroList);
                initAdapter();
            }
        });
    }

    private void initAdapter() {
        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        if(isNetworkAvailable(getApplicationContext())) {
                            loadMore();
                        }else {
                            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                        isLoading = true;
                        Toast.makeText(MainActivity.this, "Bottom of page "+page_count+" reached", Toast.LENGTH_SHORT).show();
                        page_count = page_count + 1;
                    }
                }
            }
        });
    }
    private  void loadData(int intStartNo,int intEndNo){
        viewModel.getNums(intStartNo,intEndNo).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> numList) {
                rowsArrayList.addAll(numList);
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
                //initAdapter();
            }
        });
    }

    private void loadMore() {
        intStartNo = intEndNo+1;
        intEndNo = intEndNo+50;
        loadData(intStartNo,intEndNo);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
