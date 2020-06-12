package com.damnluck.cityrecommendation.ui;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import com.damnluck.cityrecommendation.R;
import com.damnluck.cityrecommendation.db.filesmanagment.FileManager;
import com.damnluck.cityrecommendation.logic.Api;
import com.damnluck.cityrecommendation.logic.ApiCLIENT;
import com.damnluck.cityrecommendation.logic.Utils;
import com.damnluck.cityrecommendation.models.Business;
import com.damnluck.cityrecommendation.models.City;
import com.damnluck.cityrecommendation.models.Coords;
import com.damnluck.cityrecommendation.models.Traveller;
import com.damnluck.cityrecommendation.viewmodels.CityViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectFragment extends Fragment {

	private Traveller traveller;

	private MaterialButton searchButton;

	private CheckBox seaCheckbox;
	private CheckBox cafeCheckbox;
	private CheckBox museumCheckbox;
	private CheckBox withOutRaining;

	private TextInputEditText city1Txt;
	private TextInputEditText city2Txt;

	private List<Pair<String, Integer>> resultWords = new ArrayList<>();

	private List<Pair<String , Double>> resultDistance = new ArrayList<>();

	private CityViewModel cityViewModel;


	SelectFragment(Traveller traveller) {
		this.traveller = traveller;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_select, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setupViews(view);

		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				recommend();
			}
		});
		setupViewModel();
	}

	private void setupViews(View view) {
		searchButton = view.findViewById(R.id.searchButton);
		seaCheckbox = view.findViewById(R.id.seaCheckbox);
		cafeCheckbox = view.findViewById(R.id.cafeCheckbox);
		museumCheckbox = view.findViewById(R.id.museumCheckbox);
		city1Txt = view.findViewById(R.id.city1Txt);
		city2Txt = view.findViewById(R.id.city2Txt);
		withOutRaining = view.findViewById(R.id.withOutRainingCheckbox);

		if(traveller.getClass() == Business.class){
			hideCriterion();
		}
	}

	private void setupViewModel(){
		cityViewModel = new CityViewModel(this.requireContext());

	}

	private void recommend() {
		if (checkForm()) {
			if(withOutRaining.isChecked()){
				getWeather(city1Txt.getText().toString());
				getWeather(city2Txt.getText().toString());
			}else{
				if (traveller.getClass() == Business.class) {
					requestCoords(city1Txt.getText().toString());
					requestCoords(city2Txt.getText().toString());

				} else {
					checkIfCityIsInDb(city1Txt.getText().toString());
					checkIfCityIsInDb(city2Txt.getText().toString());
				}
			}
		}
	}

	private boolean checkForm() {
		boolean check = true;

		if (Objects.requireNonNull(city1Txt.getText()).toString().isEmpty()) {
			check = false;
		}
		if (Objects.requireNonNull(city2Txt.getText()).toString().isEmpty()) {
			check = false;
		}

		return check;
	}


	private void countWords(String article, String city) {

		ArrayList<String> criterions = new ArrayList<>();
		if (seaCheckbox.isChecked()) {
			criterions.add("sea");
		}
		if (cafeCheckbox.isChecked()) {
			criterions.add("cafe");
		}
		if (museumCheckbox.isChecked()) {
			criterions.add("museum");
		}


		List<Pair<String , Integer>> results = Utils.countMatchWords(article , criterions);

		int score = traveller.similarity(results);

		resultWords.add(new Pair<>(city , score));
		if(resultWords.size() > 1){
			if(resultWords.get(0).second > resultWords.get(1).second){
				String winner = resultWords.get(0).first;
				System.out.println("score winner : " + resultWords.get(0).second);
				System.out.println("score loser : " + resultWords.get(1).second);
				finishFragment(winner);

			}else {
				String winner = resultWords.get(1).first;

				System.out.println("score winner : " + resultWords.get(1).second);
				System.out.println("score loser : " + resultWords.get(0).second);
				FileManager.writeToFile(traveller , this.requireContext());
				finishFragment(winner);
			}
		}
	}

	private void hideCriterion(){
		seaCheckbox.setVisibility(View.INVISIBLE);
		museumCheckbox.setVisibility(View.INVISIBLE);
		cafeCheckbox.setVisibility(View.INVISIBLE);
	}

	private void countDistance(Coords coords , String city ){

		resultDistance.add(new Pair<>(city , traveller.similarity(coords)));

		if(resultDistance.size() > 1){
			if(resultDistance.get(0).second > resultDistance.get(1).second){
				String winner = resultDistance.get(1).first;
				finishFragment(winner);
			}else {
				String winner = resultDistance.get(0).first;
				FileManager.writeToFile(traveller , this.requireContext());
				finishFragment(winner);
			}
		}
	}


	private void getWeather(final String city ) {
		Api apiInterface = ApiCLIENT.getClientWeather().create(Api.class);
		Call<ResponseBody> call = apiInterface.getWeather(city , Api.API_KEY);

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					String body = response.body().string();
					boolean isRaining = raining(body);
					if(!isRaining){
						if (traveller.getClass() == Business.class) {
							requestCoords(city);
						} else {
							requestArticle(city);
						}
					}else {
						Toast.makeText(getContext() , "Βρεχει στην πόλη "+city , Toast.LENGTH_SHORT).show();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {

			}
		});
	}

	private void requestArticle(final String city) {

		Api apiInterface = ApiCLIENT.getClientWiki().create(Api.class);
		Call<ResponseBody> call = apiInterface.getArticle(city, "json");

		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					countWords(response.body().string() , city);
					insertCity(new City(city, response.body().string()));

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {

			}
		});
	}


	private void checkIfCityIsInDb(final String city){

		cityViewModel.getCity().observe(this.getViewLifecycleOwner(), new Observer<List<City>>() {
			@Override
			public void onChanged(List<City> cities) {
				for(City city2 : cities){
					if(city.equals(city2.getName())){
						countWords(city2.getArticle() , city);
						return;
					}
				}
				requestArticle(city);

			}
		});

		getAllCities();
	}

	private void getAllCities(){
		cityViewModel.getAll(new Continuation<Unit>() {
			@NotNull
			@Override
			public CoroutineContext getContext() {
				return null;
			}

			@Override
			public void resumeWith(@NotNull Object o) {

			}
		});
	}

	private void insertCity(City city){
		cityViewModel.insert(city, new Continuation<Unit>() {
			@NotNull
			@Override
			public CoroutineContext getContext() {
				return null;
			}

			@Override
			public void resumeWith(@NotNull Object o) {

			}
		});
	}

	private void requestCoords(final String city) {
		Api apiInterface = ApiCLIENT.getClientWiki().create(Api.class);
		Call<ResponseBody> call = apiInterface.getCoords(city, "json");
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
				try {
					if(response.code() == 200){
						String body = response.body().string();
						Coords coords = cleanCoords(body);

						countDistance(coords , city);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(Call<ResponseBody> call, Throwable t) {

			}
		});
	}


	private boolean raining(String body){
		int index = body.indexOf("rain");
		int count = 0;
		while (index != -1) {
			count++;
			body = body.substring(index + 1);
			index = body.indexOf("rain");
		}
		return count > 0;
	}

	private Coords cleanCoords(String body) {
		int startIndex = body.indexOf("\"lat\":");
		int endIndex = body.indexOf(",", startIndex);

		if (endIndex == -1) {
			endIndex = body.length();
		}

		String latString = body.substring(startIndex, endIndex);
		String lat = latString.replace("\"lat\":", "");

		startIndex = body.indexOf("\"lon\":");
		endIndex = body.indexOf(",", startIndex);

		if (endIndex == -1) {
			endIndex = body.length();
		}

		String lonString = body.substring(startIndex, endIndex);
		String lon = lonString.replace("\"lon\":", "");


		return new Coords(Double.parseDouble(lat), Double.parseDouble(lon));
	}

	private void finishFragment(String city){
		FragmentManager fragmentManager = getFragmentManager();

		assert fragmentManager != null;
		fragmentManager.beginTransaction().remove(this);
		fragmentManager.beginTransaction().add(R.id.fragmentContainer, new FinishFragment(city), "finish_fragment").commit();
	}

}
