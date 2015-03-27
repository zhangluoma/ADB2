import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
* GetCreator contains getBusiness, getAuthor to answer the query "who created X?"
* @author  Luoma Zhang, Huaiyuan Cao
* @version 1.0
* @since   2015.3.25
*/

public class GetCreator {
	
	static String prefix = "https://www.googleapis.com/freebase/v1/mqlread?query=";
	static String key = "AIzaSyDIFHxpLA7AKRZZY8CP0S9vXgS0r-XEfv4";
	static String suffix = "&key="+key;

	public static String query(String tarUrl){
		/**
		* Given URL, fetch the content and return as String
		*/
		HttpURLConnection connection = null;
		try {
			URL url = new URL(tarUrl);
			connection = (HttpURLConnection) url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			StringBuffer result=new StringBuffer();
			String tmp= null;
			while((tmp = in.readLine())!=null){
				result.append(tmp);
			}
			return result.toString();
		} catch (IOException e) {
			return null;
		}
	}

	public static HashMap<String,ArrayList<String>> getBusiness(String query){

		/**
		* Get a Map of Businessperson name and his/her companys
		* @param res, the result HashMap
		* @param query, the processed query
		*/

		//Use HashMap to store name and his/her companys
		HashMap<String,ArrayList<String>> res =  new HashMap<String,ArrayList<String>>();
		query = "[{\"/organization/organization_founder/organizations_founded\": [{\"a:name\": null,\"name~=\": \""+query+"\"}],\"id\": null,\"name\": null,\"type\": \"/organization/organization_founder\"}]";
		//encode query
		try {
			query = URLEncoder.encode(query, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//fetch JSONObject
		JSONObject obj = new JSONObject(GetCreator.query(prefix+query+suffix));
		JSONArray ja = obj.getJSONArray("result");
		for(int i=0;i<ja.length();i++){
			String name = ja.getJSONObject(i).getString("name");
			ArrayList<String> aname = new ArrayList<String>();
			JSONArray arr = ja.getJSONObject(i).getJSONArray("/organization/organization_founder/organizations_founded");
			for(int j = 0; j<arr.length();j++){
				aname.add(arr.getJSONObject(j).getString("a:name"));
			}
			res.put(name, aname);
		}
		return res;
	}
	public static HashMap<String,ArrayList<String>> getAuthor(String query){

		/**
		* Get a Map of Author name and his/her books
		* @param res, the result HashMap
		* @param query, the processed query
		*/

		//Use HashMap to store name and his/her books
		HashMap<String,ArrayList<String>> res =  new HashMap<String,ArrayList<String>>();
		query = "[{\"/book/author/works_written\": [{\"a:name\": null,\"name~=\": \""+query+"\"}],\"id\": null,\"name\": null,\"type\": \"/book/author\"}]";
		try {
			query = URLEncoder.encode(query, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(query);
		JSONObject obj = new JSONObject(GetCreator.query(prefix+query+suffix));
		JSONArray ja = obj.getJSONArray("result");
		for(int i=0;i<ja.length();i++){
			String name = ja.getJSONObject(i).getString("name");
			ArrayList<String> aname = new ArrayList<String>();
			JSONArray arr = ja.getJSONObject(i).getJSONArray("/book/author/works_written");
			for(int j = 0; j<arr.length();j++){
				aname.add(arr.getJSONObject(j).getString("a:name"));
			}
			res.put(name, aname);
		}
		return res;
	}
}
