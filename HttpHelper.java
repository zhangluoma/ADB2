import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
public class HttpHelper{
	public static String query(String tarUrl){
		//escape the query first
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
			// TODO Auto-generated catch block
			return null;
		}
	}
}
class FreeBase{
	static String searchPrefix = "https://www.googleapis.com/freebase/v1/search?query=";
	static String topicPrefix = "https://www.googleapis.com/freebase/v1/topic";
	static String key = "AIzaSyDIFHxpLA7AKRZZY8CP0S9vXgS0r-XEfv4";
	static public ArrayList<Entity> search(String query){
		try {
			return Entity.getEntityList(new JSONObject(HttpHelper.query(searchPrefix+URLEncoder.encode(query, "ISO-8859-1")+"&key="+key)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	static public Infobox topic(String query){
		return new Infobox(new JSONObject(HttpHelper.query(topicPrefix+query+"?key="+key)));
	}
}
class Entity{
	String mid;
	public Entity(String mid){
		this.mid=mid;
	}
	static public ArrayList<Entity> getEntityList(JSONObject obj){
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		JSONArray ja = obj.getJSONArray("result");
		for(int i=0;i<ja.length();i++){
			entityList.add(new Entity(ja.getJSONObject(i).getString("mid")));
		}
		return entityList;
	}
}
class Infobox{
	JSONObject obj;
	String name;
	String Birthday;
	String placeOfBirth;
	Death death;
	ArrayList<String> siblings;
	ArrayList<String> spouses;
	String description;
	public Infobox(JSONObject obj){
		this.obj=obj;
		getName();
	}
	class Death{
		String place;
		String date;
		String cause;
		public Death(String place,String date,String cause){
			this.place=place;
			this.date=date;
			this.cause=cause;
		}
	}
	private void getName(){
		name = get1Text("/type/object/name");
	}
	private ArrayList<JSONObject> get1l(String name){
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		JSONArray ja = obj.getJSONObject("property").getJSONObject(name).getJSONArray("values");
		for(int i=0;i<ja.length();i++){
			result.add(ja.getJSONObject(i));
		}
		return result;
	}
	private String get1Text(String name){
		if(!get1l(name).isEmpty()){
			return get1l(name).get(0).getString("text");
		}
		return null;
	}
}