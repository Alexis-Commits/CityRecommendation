package com.damnluck.cityrecommendation.models;

import android.util.Pair;

import androidx.room.Entity;

import com.damnluck.cityrecommendation.logic.Utils;

import java.util.List;

@Entity
public class Tourist extends Traveller{

	public Tourist(String name, int age, Coords coords) {
		super(name, age, coords);
	}

	@Override
	public int similarity(List<Pair<String, Integer>> criteria) {
		return Utils.scoreBasedOnWordsCount(criteria);
	}
}
