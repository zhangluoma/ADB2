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
	ArrayList<String> siblings = new ArrayList<String>();
	ArrayList<String> spouses = new ArrayList<String>();
	String description;
	ArrayList<Leadership> leadership = new ArrayList<Leadership>();
	ArrayList<String> founded = new ArrayList<String>();
	public Infobox(JSONObject obj){
		this.obj=obj;
		getName();
		getLeadership();
		getFounded();
	}
	private void getName(){
		name = get1Text(obj,"/type/object/name");
	}
	private void getLeadership(){
		ArrayList<JSONObject> list = get1l(obj,"/business/board_member/leader_of");
		for(JSONObject jo:list){
			leadership.add(new Leadership(get1Text(jo,"/organization/leadership/from"),
					get1Text(jo,"/organization/leadership/to"),
					get1Text(jo,"/organization/leadership/organization"),
					get1Text(jo,"/organization/leadership/role"),
					get1Text(jo,"/organization/leadership/title")));
		}
	}
	private void getFounded(){
		founded.addAll(get1TextArray(obj,"/organization/organization_founder/organizations_founded"));
	}
	private ArrayList<JSONObject> get1l(JSONObject jo,String name){
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		JSONArray ja = jo.getJSONObject("property").getJSONObject(name).getJSONArray("values");
		for(int i=0;i<ja.length();i++){
			result.add(ja.getJSONObject(i));
		}
		return result;
	}
	private String get1Text(JSONObject jo,String name){
		if(!get1l(jo,name).isEmpty()){
			return get1l(jo,name).get(0).getString("text");
		}
		return null;
	}
	private ArrayList<String> get1TextArray(JSONObject jo,String name){
		ArrayList<JSONObject> jsonArray=get1l(jo,name);
		ArrayList<String> result = new ArrayList<String>();
		for(JSONObject subJo:jsonArray){
			result.add(subJo.getString("text"));
		}
		return result;
	}
}
class Leadership{
	String from;
	String to;
	String organization;
	String role;
	String title;
	public Leadership(String from, String to, String organization,String role,String title){
		this.from = from;
		this.to=to;
		this.organization =organization;
		this.role=role;
		this.title=title;
	}
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