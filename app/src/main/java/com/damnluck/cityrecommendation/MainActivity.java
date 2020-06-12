package com.damnluck.cityrecommendation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.damnluck.cityrecommendation.ui.LoginFragment;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		addFragment();

	}


	private void addFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().add(R.id.fragmentContainer, new LoginFragment(), "loginFragment").commit();
	}
}
