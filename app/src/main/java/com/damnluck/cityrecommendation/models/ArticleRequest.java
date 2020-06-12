package com.damnluck.cityrecommendation.models;

public class ArticleRequest {

//	action=parse&prop=extracts&titles="{city}"&format=json&formatversion=2
	String action ;
	String prop ;
	String titles;
	String format;
	String formatVersion;


	public ArticleRequest(String titles) {
		this.action = "parse";
		this.prop = "extract";
		this.format = "json";
		this.formatVersion = "2";

		this.titles = titles;
	}
}
