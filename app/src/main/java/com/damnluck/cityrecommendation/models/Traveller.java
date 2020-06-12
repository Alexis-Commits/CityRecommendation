package com.damnluck.cityrecommendation.models;

import android.util.Pair;
import java.util.List;

public class Traveller {

	private final String name;
	private final int age;
	private final Coords coords;

	public Traveller(String name, int age, Coords coords) {
		this.name = name;
		this.age = age;
		this.coords = coords;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public Coords getCoords() {
		return coords;
	}


	@Override
	public String toString() {
		return "Traveller{" +
			"name='" + name + '\'' +
			", age=" + age +
			", coords=" + coords +
			'}';
	}



	public double similarity(Coords coords){
		return 0f;
	}

	public  int similarity(List<Pair<String, Integer>> criteria){
		return 0;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Traveller myObject = (Traveller) o;

		if(!myObject.name.equals(name)) return false;
		if(myObject.age != age) return false;


		return true;
	}

}
