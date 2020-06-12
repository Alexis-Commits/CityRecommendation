package com.damnluck.cityrecommendation.logic;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class ApiCLIENT {

	private static Retrofit retrofit = null;

	public static Retrofit getClientWiki() {

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		retrofit = new Retrofit.Builder()
			.baseUrl("https://en.wikipedia.org/w/")
			.client(client)
			.build();

		return retrofit;
	}

	public static Retrofit getClientWeather() {

		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

		retrofit = new Retrofit.Builder()
			.baseUrl("https://api.openweathermap.org/")
			.client(client)
			.build();

		return retrofit;
	}

}

