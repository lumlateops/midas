package com.lumlate.midas.utils;

import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class HtmlParser {

	private String url;
	private String rawtext;
	private String title;
	private LinkedList<String> links=new LinkedList<String>();
	private LinkedList<String> images=new LinkedList<String>();
	private LinkedList<String> media=new LinkedList<String>();
	private LinkedList<String> imports=new LinkedList<String>();
	private LinkedList<String> scripts=new LinkedList<String>();
	private LinkedList<String> unsubscribelinks = new LinkedList<String>();
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRawtext() {
		return rawtext;
	}

	public void setRawtext(String rawtext) {
		this.rawtext = rawtext;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LinkedList<String> getLinks() {
		return links;
	}

	public void setLinks(LinkedList<String> links) {
		this.links = links;
	}

	public LinkedList<String> getImages() {
		return images;
	}

	public void setImages(LinkedList<String> images) {
		this.images = images;
	}

	public LinkedList<String> getMedia() {
		return media;
	}

	public void setMedia(LinkedList<String> media) {
		this.media = media;
	}

	public LinkedList<String> getImports() {
		return imports;
	}

	public void setImports(LinkedList<String> imports) {
		this.imports = imports;
	}

	public LinkedList<String> getScripts() {
		return scripts;
	}

	public void setScripts(LinkedList<String> scripts) {
		this.scripts = scripts;
	}

	public void parsehtml(String html) throws Exception{
		if(html==null){
			String err="Please provide a non empty html string";
			throw new Exception(err);
		}
		Document doc=null;
		
		try{
			doc = Jsoup.parseBodyFragment(html);
			Elements elinks=doc.select("a[href]");
			Elements emedia=doc.select("[src]");
			Elements eimports=doc.select("link[href]");
			Elements eall = doc.getAllElements();
			
			Iterator it = eall.iterator();
			this.setRawtext(doc.body().text());
			this.setTitle(doc.title());
			
	        for (Element src : emedia) {
	            if (src.tagName().equals("img")){
	            	if(src.attr("src") != null){
	                	this.images.add(src.attr("src"));	
	            	}
	            }
	            else if(src.tagName().equals("script")){
	            	if(src.attr("src") != null){
	            		this.scripts.add(src.attr("src"));}
	            }
	            else{
	            	this.media.add(src.attr("abs:src"));
	            }
	        }
	 
	        for (Element link : elinks) {
	        	if(link.attr("href")!=null){
	        		if(link.tagName().equalsIgnoreCase("a") && link.text().matches("(?i).*(unsubscribe|preference).*")){
	        			unsubscribelinks.add(link.attr("href"));
	        		}
	                this.links.add(link.attr("href"));
	        	}
	        }
	        
	        for (Element link : eimports) {
	            this.imports.add(link.attr("rel"));
	        }
		}
		catch(Exception err){
			err.printStackTrace();
		}
	}
}
