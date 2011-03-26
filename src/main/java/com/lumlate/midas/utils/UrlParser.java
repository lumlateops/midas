package com.lumlate.midas.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class UrlParser {

	public static LinkedList<String> parseDomains(LinkedList<String> links) throws Exception {
		LinkedList<String> returnlink=new LinkedList<String>();
		for(String u:links){
			try{
				URL aURL = new URL(u);
				String protocol = aURL.getProtocol();
				String authority = aURL.getAuthority();
				String host = aURL.getHost();
				int port = aURL.getPort();
				String path = aURL.getPath();
				String query = aURL.getQuery();
				String filename = aURL.getFile();
				String ref = aURL.getRef();
				if(protocol.equals("http") || protocol.equals("https")){
					returnlink.add(host);
				}
			}catch (Exception e){
				//e.printStackTrace();
				continue;
			}
		}
		return returnlink;
	}

	public static String parseDomain(String url) throws Exception {
		// TODO Auto-generated method stub
		URL aURL = new URL(url);
		String protocol = aURL.getProtocol();
		String authority = aURL.getAuthority();
		String host = aURL.getHost();
		int port = aURL.getPort();
		String path = aURL.getPath();
		String query = aURL.getQuery();
		String filename = aURL.getFile();
		String ref = aURL.getRef();
		if(protocol.equals("http") || protocol.equals("https")){
			return host;
		}
		return null;
	}

}
