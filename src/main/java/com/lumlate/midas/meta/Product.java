package com.lumlate.midas.meta;

import java.util.Set;

public class Product {
	private Vertical vertical;   //electronics
	private Set<String> wordcloud;
	
	public Vertical getVertical() {
		return vertical;
	}
	public void setVertical(Vertical vertical) {
		this.vertical = vertical;
	}
	public Set<String> getWordcloud() {
		return wordcloud;
	}
	public void setWordcloud(Set<String> wordcloud) {
		this.wordcloud = wordcloud;
	}
	
	public void addWordinCloud(String word){
		this.wordcloud.add(word);
	}
}
