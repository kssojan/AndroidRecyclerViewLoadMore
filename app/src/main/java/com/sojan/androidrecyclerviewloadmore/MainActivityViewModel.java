package com.sojan.androidrecyclerviewloadmore;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivityViewModel extends ViewModel {
    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<String>> heroList;

    //we will call this method to get the data
    public LiveData<List<String>> getNums(int intStartNo,int intEndNo) {
        //if the list is null
        if (heroList == null) {
            heroList = new MutableLiveData<List<String>>();
            //we will load it asynchronously from server in this method
            loadNumbers(intStartNo,intEndNo);
        }else{
            heroList = new MutableLiveData<List<String>>();
            //we will load it asynchronously from server in this method
            loadNumbers(intStartNo,intEndNo);
        }

        //finally we will return the list
        return heroList;
    }

    //This method is using Retrofit to get the JSON data from URL
    private void loadNumbers(int intStartNo,int intEndNo) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);
        Call<List<String>> call = api.getNumbers(intStartNo,intEndNo);


        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                //finally we are setting the list to our MutableLiveData
                heroList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });
    }
}
