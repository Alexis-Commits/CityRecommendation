package com.damnluck.cityrecommendation.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.damnluck.cityrecommendation.R;
import com.damnluck.cityrecommendation.db.filesmanagment.FileManager;
import com.damnluck.cityrecommendation.logic.Utils;
import com.damnluck.cityrecommendation.models.Traveller;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import kotlin.Unit;

public class FinishFragment extends Fragment {

	private MaterialButton backButton ;
	private TextView winner ;
	private TextView allTravellers;

	private String winnerCity;

	public FinishFragment(String city) {
		winnerCity = city;
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_finish, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setupViews(view);

		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				back();
			}
		});
	}


	private void setupViews(View view){

		backButton = view.findViewById(R.id.buttonBack);

		winner = view.findViewById(R.id.winnerCityTxt);

		winner.setText(winnerCity);

		allTravellers = view.findViewById(R.id.allTravellersTxt);
		allTravellers.setText("Travellers from file :\n");

		ArrayList<Traveller> travellers = FileManager.readFromFile(this.requireContext());

		for(Traveller traveller :travellers){
			allTravellers.append(traveller.getName() +" "+traveller.getAge()+"\n");
		}

	}

	private void back(){
		FragmentManager fragmentManager = getFragmentManager();

		assert fragmentManager != null;
		fragmentManager.beginTransaction().remove(this);
		fragmentManager.beginTransaction().add(R.id.fragmentContainer, new LoginFragment(), "login_fragment").commit();
	}
}
