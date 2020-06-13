package com.damnluck.cityrecommendation.logic;

import android.util.Pair;

import com.damnluck.cityrecommendation.models.Coords;

import java.util.ArrayList;
import java.util.List;

public class Utils {


	/**
	 * Count words in article
	 * Input ArrayList of criterion
	 * Output Pair of <String criterion , Int words >
	 */
	public static List<Pair<String, Integer>> countMatchWords(String cityArticle, List<String> criterions) {
		cityArticle = cityArticle.toLowerCase();

		ArrayList<Pair<String, Integer>> result = new ArrayList<>();

		for (String criterion : criterions) {
			int index = cityArticle.indexOf(criterion);
			int count = 0;
			while (index != -1) {
				count++;
				cityArticle = cityArticle.substring(index + 1);
				index = cityArticle.indexOf(criterion);
			}

			result.add(new Pair<String, Integer>(criterion, count));
		}

		return result;
	}


	public static int scoreBasedOnWordsCount(List<Pair<String, Integer>> words) {
		int score = 0;

		for (Pair<String, Integer> word : words) {
			score = score + word.second;
		}

		return score;
	}

	public static int scoreBasedOnCriterion(List<Pair<String, Integer>> words) {
		return words.size();
	}


	public static double calcDistance(Coords coords1, Coords coords2) {
		if ((coords1.getLat() == coords2.getLat()) && (coords1.getLon() == coords2.getLon())) {
			return 0;
		} else {
			double theta = coords1.getLon() - coords2.getLon();
			double dist =
				Math.sin(Math.toRadians(coords1.getLat())) * Math.sin(Math.toRadians(coords2.getLat())) +
				Math.cos(Math.toRadians(coords1.getLat())) * Math.cos(Math.toRadians(coords2.getLat())) * Math.cos(Math.toRadians(theta));
			dist = Math.acos(dist);
			dist = Math.toDegrees(dist);
			dist = dist * 60 * 1.1515;

			return dist;
		}
	}


	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
	{

		ArrayList<T> newList = new ArrayList<>();

		for (T element : list) {


			if (!newList.contains(element)) {

				newList.add(element);
			}
		}

		return newList;
	}

}
