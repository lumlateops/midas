package com.lumlate.midas.utils;

import java.util.LinkedList;

public class TextSegementer {
	
	private String text;
	private LinkedList<String> links;
	private final static String LEAD_NONALPHA="^\\W+";
	private final static String LEAD_DIGITS="^\\d+";
	private final static String LEAD_SPACE="^\\s+";
	private final static String LEAD_UNDSCORE="^_+";
	private final static String TRAIL_NONALPHA="\\W+$";
	private final static String TRAIL_DIGITS="\\d+$";
	private final static String TRAIL_SPACE="\\s+$";
	private final static String TRAIL_UNDSCORE="_+$";
	private final static String WHITESPACE="\\s+";
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public static LinkedList<String> SpaceSegmenter(String text) throws Exception{
		LinkedList<String> returnvec=new LinkedList<String>();
		for(String term:text.split("\\s+")){
		    term=term.replaceAll(LEAD_NONALPHA, "").replaceAll(LEAD_DIGITS, "").replaceAll(LEAD_SPACE, "").replaceAll(LEAD_UNDSCORE, "").replaceAll(TRAIL_NONALPHA, "").replaceAll(TRAIL_DIGITS, "").replaceAll(TRAIL_SPACE, "").replaceAll(TRAIL_UNDSCORE, "").replaceAll(WHITESPACE, " ");
		    if(term.length()>3 && term.length()<20){
			    returnvec.add(term);
		    }
		}
		//LinkedList<String> wordvec=Sanitize(text.split(" "));
		return returnvec;
	}
}
