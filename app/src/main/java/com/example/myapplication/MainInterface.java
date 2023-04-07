package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MainInterface {

    @GET("search/photos?client_id=aB24Od_HuNq2ah7wwb90UA0z67B2rjssF_28ie9wgXk&query=photos/random&")
    Call<String> STRING_CALL(
            @Query("page")int page,
            @Query("limit")int limit
    );
    @GET("search/photos?client_id=aB24Od_HuNq2ah7wwb90UA0z67B2rjssF_28ie9wgXk&")
    Call<String> STRING_CALL2(
            @Query("query")String cat,
            @Query("page")int page,
            @Query("limit")int limit
    );

}

