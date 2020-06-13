package com.damnluck.cityrecommendation.ui;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.damnluck.cityrecommendation.R;
import com.damnluck.cityrecommendation.logic.Permissions;
import com.damnluck.cityrecommendation.models.Business;
import com.damnluck.cityrecommendation.models.Coords;
import com.damnluck.cityrecommendation.models.Tourist;
import com.damnluck.cityrecommendation.models.Traveller;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class LoginFragment extends Fragment {


	private TextInputEditText ageTxt;
	private TextInputEditText nameTxt;
	private MaterialButton next;

	private RadioButton business;
	private RadioButton tourist;

	private Permissions permissions;

	private Coords coords;
	private String name;
	private int age;

	public LoginFragment() {
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		permissions = new Permissions(this.getActivity());
		permissions.requestNecessaryPermissions();

		getLocation();
		setupView(view);
		setupListener();
	}

	@SuppressLint("MissingPermission")
	private void getLocation() {

		if(isGoogleServicesAvailable()){
			LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(this.getActivity())).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
				@Override
				public void onSuccess(Location location) {
					coords = new Coords(location.getLatitude(), location.getLongitude());
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					permissions.requestNecessaryPermissions();
				}
			});
		}else {
			//Emulator
			coords = new Coords(37.983096,23.7275388); //Athens coordinates
		}

	}

	private boolean isGoogleServicesAvailable(){
		return GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.requireContext()) == ConnectionResult.SUCCESS;
	}

	private void setupView(View view) {
		ageTxt = view.findViewById(R.id.ageTxt);
		nameTxt = view.findViewById(R.id.nameTxt);
		next = view.findViewById(R.id.nextButton);

		business = view.findViewById(R.id.businessRadio);
		tourist = view.findViewById(R.id.touristRadio);
	}

	private void setupListener() {
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				nextClicked();
			}
		});
	}

	private void nextClicked() {
		if (checkForm()) {
			Traveller traveller;
			if(business.isChecked()){
				traveller = new Business(name , age , coords);
			}else {
				traveller = new Tourist(name , age , coords);
			}
			System.out.println("Traveller type : "+traveller.getClass());
			addSelectFragment(traveller);
		}
	}

	private boolean checkForm() {

		if (nameTxt.getText().toString().isEmpty()) {
			nameTxt.requestFocus();
			return false;
		}

		if (ageTxt.getText().toString().isEmpty()) {
			ageTxt.requestFocus();
			return false;
		}

		if(!(business.isChecked() || tourist.isChecked())){
			return false;
		}

		age = Integer.parseInt(ageTxt.getText().toString());
		name = nameTxt.getText().toString();

		return true;
	}

	private void addSelectFragment(Traveller traveller) {
		FragmentManager fragmentManager = getFragmentManager();

		assert fragmentManager != null;
		fragmentManager.beginTransaction().remove(this);
		fragmentManager.beginTransaction().add(R.id.fragmentContainer, new SelectFragment(traveller), "select_fragment").commit();
	}

}
