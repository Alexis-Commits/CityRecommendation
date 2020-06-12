package com.damnluck.cityrecommendation.logic;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface  Api {

	String API_KEY = "555f4c77e2d7585c2142b2feca34d9aa"; //For weather api

	@GET("api.php?action=query&prop=extracts")
	Call<ResponseBody> getArticle(@Query("titles") String city , @Query("format") String format );

	@GET("api.php?action=query&prop=coordinates")
	Call<ResponseBody> getCoords(@Query("titles") String city , @Query("format") String format );

	@GET("data/2.5/weather?")
	Call<ResponseBody> getWeather(@Query("q") String city , @Query("APPID") String appid);
}
