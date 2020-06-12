package com.damnluck.cityrecommendation.models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class City {

	@PrimaryKey
	@NotNull
	private String name;

	private String article;


	public City(String name, String article) {
		this.name = name;
		this.article = article;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

}
