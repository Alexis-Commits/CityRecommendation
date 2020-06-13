package com.damnluck.cityrecommendation.logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class Permissions {

	private Activity activity;

	public Permissions(Activity activity) {
		this.activity = activity;
	}

	public void requestNecessaryPermissions() {
		Dexter.withActivity(activity)
			.withPermissions(
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.ACCESS_COARSE_LOCATION,
				Manifest.permission.ACCESS_FINE_LOCATION
			).withListener(buildListener(activity))
			.check();
	}

	private MultiplePermissionsListener buildListener(Context context){

		return new MultiplePermissionsListener() {
			@Override
			public void onPermissionsChecked(MultiplePermissionsReport report) {

			}

			@Override
			public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
				token.continuePermissionRequest();
			}
		};

	}
}
