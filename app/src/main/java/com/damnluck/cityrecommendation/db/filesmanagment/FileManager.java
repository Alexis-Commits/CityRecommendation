package com.damnluck.cityrecommendation.db.filesmanagment;

import android.content.Context;

import com.damnluck.cityrecommendation.logic.Utils;
import com.damnluck.cityrecommendation.models.Tourist;
import com.damnluck.cityrecommendation.models.Traveller;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FileManager {

	private static String TRAVELLERS ="travellers.json";

	static final Type type = new TypeToken<ArrayList<Traveller>>() {}.getType();



	static public void writeToFile(Traveller traveller, Context context) {
		try {

			ArrayList<Traveller> travellers = readFromFile(context);
			travellers.add(traveller);
			Gson gson =  new Gson();
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(TRAVELLERS, Context.MODE_PRIVATE));
			outputStreamWriter.write(gson.toJson(travellers , type));
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	static public ArrayList<Traveller> readFromFile(Context context) {

		ArrayList<Traveller> travellers = new ArrayList<>();

		try {
			InputStream inputStream = context.openFileInput(TRAVELLERS);

			if ( inputStream != null ) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ( (receiveString = bufferedReader.readLine()) != null ) {
					stringBuilder.append("\n").append(receiveString);
				}

				Gson gson = new Gson();
				inputStream.close();

				travellers = (ArrayList<Traveller>) gson.fromJson(stringBuilder.toString() ,type);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}

		return Utils.removeDuplicates(travellers);
	}

}
