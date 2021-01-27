package com.sojan.androidrecyclerviewloadmore;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

   // String BASE_URL = "https://simplifiedcoding.net/demos/";
   String BASE_URL = "http://waypointsystems.dyndns.org:25410/Api/DemoApi/";

    /*@GET("GetAllNumbers?intStartNo=1&intEndNo=50")
    Call<List<String>> getHeroes();*/

    @GET("GetAllNumbers")
    Call<List<String>> getNumbers(@Query("intStartNo") int intStartNo, @Query("intEndNo") int intEndNo);
}
