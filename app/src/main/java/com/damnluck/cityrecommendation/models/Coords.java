package com.damnluck.cityrecommendation.models;

public class Coords {
	private final double lat;
	private final double lon;

	public Coords(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	@Override
	public String toString() {
		return "Coords{" +
			"lat=" + lat +
			", lon=" + lon +
			'}';
	}
}
