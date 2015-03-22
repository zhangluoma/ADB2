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
	ArrayList<Coach> coach = new ArrayList<Coach>();
	ArrayList<Roster> roster = new ArrayList<Roster>(); ;
	ArrayList<String> siblings = new ArrayList<String>();
	ArrayList<String> spouses = new ArrayList<String>();
	ArrayList<String> books = new ArrayList<String>();
	ArrayList<String> bookAboutAuthor = new ArrayList<String>();
	ArrayList<String> influenced = new ArrayList<String>();
	ArrayList<String> influencedBy = new ArrayList<String>();
	ArrayList<Film> filmsParticipated = new ArrayList<Film>();
	String description;
	ArrayList<OrganizationRole> leadership = new ArrayList<OrganizationRole>();
	ArrayList<OrganizationRole> boardMembers = new ArrayList<OrganizationRole>();
	ArrayList<String> founded = new ArrayList<String>();
	ArrayList<String> championship = new ArrayList<String>(); 
	ArrayList<String> championships =  new ArrayList<String>(); 
	ArrayList<String> teams =  new ArrayList<String>(); 
	String sport;
	String arena;
	String foundTime;
	String officialWebsite;
	ArrayList<String> slogan = new ArrayList<String>();
	ArrayList<String> locations = new ArrayList<String>();
	String leagues;
	public Infobox(JSONObject obj){
		this.obj=obj;
		getBirth();
		getName();
		getLeadership();
		getPlaceOfBirth();
		getDeath();
		getSiblings();
		getSpouses();
		getBoardMember();
		getFounded();
		getChampionship();
		getSport();
		getSlogan();
		getArena();
		getWeb();
		getChampionships();
		getDecription();
		getDeath();
		getLocations();
		getCoach();
		getRoster();
		getFound();
		getTeams();
		getLeagues();
	}
	
	private void getTeams(){
		
	}
	private void getLeagues(){
		
	}
	private void getLocations(){
		locations = get1TextArray(obj,"/sports/sports_team/location");
	}
	private void getDecription(){
		description = get2Text(obj,"/common/topic/description");
	}
	private void getWeb(){
		officialWebsite = get1Text(obj,"/common/topic/official_website");
	}
	private void getFound(){
		foundTime = get1Text(obj,"/sports/sports_team/founded");
	}
	private void getArena(){
		arena = get1Text(obj,"/sports/sports_team/arena_stadium");
	}
	private void getChampionship(){
		championship = get1TextArray(obj,"/sports/sports_league/championship");
	}
	private void getChampionships(){
		championships = get1TextArray(obj,"/sports/sports_team/championships");
	}
	private void getSport(){
		sport = get1Text(obj,"/sports/sports_league/sport");
	}
	private void getSlogan(){
		slogan = get1TextArray(obj,"/organization/organization/slogan");
	}
	
	private void getName(){
		name = get1Text(obj,"/type/object/name");
	}
	private void getBirth(){
		name = get1Text(obj,"/people/person/date_of_birth");
	}
	private void getPlaceOfBirth(){
		name = get1Text(obj,"/people/person/place_of_birth");
	}
	public void getSiblings(){
		siblings=get1TextArray(obj,"/people/person/sibling_s");
	}
	public void getSpouses(){
		spouses = get1TextArray(obj,"/people/person/spouse_s");
	}
	public void getBooks(){
		books = get1TextArray(obj,"/book/author/works_written");
	}
	public void getBooksAboutAuthor(){
		bookAboutAuthor = get1TextArray(obj,"/book/book_subject/works");
	}
	public void getInfluenced(){
		influenced = get1TextArray(obj,"/influence/influence_node/influenced");
	}
	public void getInfluencedBy(){
		influencedBy = get1TextArray(obj,"/influence/influence_node/influenced_by");
	}
	public void getFilm(){
		ArrayList<JSONObject> list = get1l(obj,"/film/actor/film");
		for(JSONObject jo:list){
			filmsParticipated.add(new Film(get1Text(jo,"/film/performance/film"),
					get1Text(jo,"/film/performance/character")));
		}
	}
	private void getLeadership(){
		ArrayList<JSONObject> list = get1l(obj,"/business/board_member/leader_of");
		for(JSONObject jo:list){
			leadership.add(new OrganizationRole(get1Text(jo,"/organization/leadership/from"),
					get1Text(jo,"/organization/leadership/to"),
					get1Text(jo,"/organization/leadership/organization"),
					get1Text(jo,"/organization/leadership/role"),
					get1Text(jo,"/organization/leadership/title")));
		}
	}
	private void getBoardMember(){
		ArrayList<JSONObject> list = get1l(obj,"/business/board_member/organization_board_memberships");
		for(JSONObject jo:list){
			boardMembers.add(new OrganizationRole(get1Text(jo,"/organization/organization_board_membership/from"),
					get1Text(jo,"/organization/organization_board_membership/to"),
					get1Text(jo,"/organization/organization_board_membership/organization"),
					get1Text(jo,"/organization/organization_board_membership/role"),
					get1Text(jo,"/organization/organization_board_membership/title")));
		}
	}
	private void getCoach(){
		ArrayList<JSONObject> list = get1l(obj,"/sports/sports_team/coaches");
		for(JSONObject jo:list){
			if(get1Text(jo,"/sports/sports_team_coach_tenure/coach")!=null){
			coach.add(new Coach(get1Text(jo,"/sports/sports_team_coach_tenure/coach"),
					get1Text(jo,"/sports/sports_team_coach_tenure/position"),
					get1Text(jo,"/sports/sports_team_coach_tenure/from"),
					get1Text(jo,"/sports/sports_team_coach_tenure/to")));
			}
		}
	}
	private void getRoster(){
		ArrayList<JSONObject> list = get1l(obj,"/sports/sports_team/roster");
		for(JSONObject jo:list){
			if(get1Text(jo,"/sports/sports_team_roster/player")!=null){
			roster.add(new Roster(get1Text(jo,"/sports/sports_team_roster/player"),
					get1Text(jo,"/sports/sports_team_roster/position"),
					get1Text(jo,"/sports/sports_team_roster/number"),
					get1Text(jo,"/sports/sports_team_roster/from"),
					get1Text(jo,"/sports/sports_team_roster/to")));
			}
		}
	}
	private void getDeath(){
		if(get1Text(obj,"/people/deceased_person/date_of_death")!=null){
			death = new Death(get1Text(obj,"/people/deceased_person/place_of_death"),
					get1Text(obj,"/people/deceased_person/date_of_death"),
					get1Text(obj,"/people/deceased_person/cause_of_death"));
		}
	}
	private void getFounded(){
		founded.addAll(get1TextArray(obj,"/organization/organization_founder/organizations_founded"));
	}
	private ArrayList<JSONObject> get1l(JSONObject jo,String name){
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();	
		try{
			JSONArray ja = jo.getJSONObject("property").getJSONObject(name).getJSONArray("values");
			for(int i=0;i<ja.length();i++){
				result.add(ja.getJSONObject(i));
			}
		}catch(Exception e){
			return result;
		}
		return result;
	}
	private String get2Text(JSONObject jo,String name){
		if(!get1l(jo,name).isEmpty()){
			return get1l(jo,name).get(0).getString("value");
		}
		return null;
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
		if(jsonArray!=null){
			for(JSONObject subJo:jsonArray){
				result.add(subJo.getString("text"));
			}
		}
		return result;
	}
}
class OrganizationRole{
	String from;
	String to;
	String organization;
	String role;
	String title;
	public OrganizationRole(String from, String to, String organization,String role,String title){
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
class Film{
	String name;
	String character;
	public Film(String name,String character){
		this.name=name;
		this.character=character;
	}
}
class Coach{
	String name;
	String position;
	String from;
	String to;
	public Coach(String name,String position,String from, String to){
		this.name=name;
		this.position=position;
		this.from=from;
		this.to=to;
	}
}
class Roster{
	String name;
	String position;
	String number;
	String from;
	String to;
	public Roster(String name,String position,String number,String from, String to){
		this.name=name;
		this.position=position;
		this.number=number;
		this.from=from;
		this.to=to;
	}
}