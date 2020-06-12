package com.damnluck.cityrecommendation.models;

import androidx.room.Entity;

import com.damnluck.cityrecommendation.logic.Utils;

@Entity
public class Business extends Traveller{


	public Business(String name, int age, Coords coords) {
		super(name, age, coords);
	}

	@Override
	public double similarity(Coords coords) {
		System.out.println("BUSINESS SIMILARITY RUNS");
		return Utils.calcDistance(coords , this.getCoords());
	}


}
