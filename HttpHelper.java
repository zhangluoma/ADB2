import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author luomazhang
 * a class used to send http request
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
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
class NameQuery{
	public static void queryCreator(String terms){
		String fullQuery = "[{\"/organization/organization_founder/organizations_founded\": [{\"text~=\":"+terms+"}],\"mid\": [],}]";
	}
}

/**
 * @author luomazhang
 * the class provides two methods for search and topic
 */
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
	static public Infobox topic(String query) throws JSONException, NoTypeException{
		return new Infobox(new JSONObject(HttpHelper.query(topicPrefix+query+"?key="+key)));
	}
}
/**
 * @author luomazhang
 * Entity used to obtain the JSOBObject fetched from freeBase with search
 */
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

/**
 * @author luomazhang
 * infobox class to maintain all of the information we fetched using search()
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
/**
 * @author luomazhang
 *
 */
class Infobox{
	HashSet<String> types = new HashSet<String>();
	StringBuffer result = new StringBuffer();
	static int length=120;
	static int leftMargin = 20;
	private static int[] column4 = {25,25,25,-1}; 
	private static int[] column3 = {30,30,-1}; 
	private static int[] column2 = {40,-1}; 
	static{
		column4[3]=length-leftMargin-column4[0]-column4[1]-column4[2]-1;
		column3[2]=length-leftMargin-column3[0]-column3[1]-1;
		column2[1]=length-leftMargin-column2[0]-1;
	}
	static Hashtable<String,String> typeMap = new Hashtable<String,String>(){
		private static final long serialVersionUID = 1L;
		{
			put("/people/person","Person");
			put("/book/author","Author");
			put("/organization/organization_founder","Business Person");
			put("/business/board_member","Business Person");
			put("/film/actor","Actor");
			put("/tv/tv_actor","Actor");
			put("/sports/sports_league","League");
			put("/sports/sports_team","Sports Team");
			put("/sports/professional_sports_team","Sports Team");
		}
	};
	JSONObject obj;
	String name;
	String birthday;
	String placeOfBirth;
	Death death;
	ArrayList<Coach> coach = new ArrayList<Coach>();
	ArrayList<Roster> roster = new ArrayList<Roster>(); ;
	ArrayList<String> siblings = new ArrayList<String>();
	ArrayList<Spouse> spouses = new ArrayList<Spouse>();
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
	ArrayList<String> leagues = new ArrayList<String>();
	public Infobox(){
		/*Table t = new Table();
		t.add("rome", 10);
		t.add("shit", 10);
		t.addTuple(new ArrayList<String>(){{add("ohh");add("haha");}});*/
		//sb = new TypeBox(60, "what?");
	}
	/**
	 * constructor
	 * @param obj
	 * @throws NoTypeException
	 */
	public Infobox(JSONObject obj) throws NoTypeException{
		this.obj=obj;
		if(!checkType()){
			throw new NoTypeException();
		}
		getName();
		getBirth();
		getPlaceOfBirth();
		getDeath();
		getSport();
		getSlogan();
		getWeb();
		getChampionship();
		getTeams();
		getTeamSport();
		getArena();
		getChampionships();
		getFound();
		getLeagues();
		getLocations();
		getCoach();
		getRoster();
		getDecription();
		getSiblings();
		getSpouses();
		getBooks();
		getBooksAboutAuthor();
		getInfluenced();
		getFounded();
		getLeadership();
		getBoardMember();
		getDeath();
		getFilm();
		addFinalLine();
	}
	/**
	 * fetch name from the object
	 */
	private void getName(){
		name = get1Text(obj,"/type/object/name");
		String t=name+"(";
		ArrayList<String> typeList= new ArrayList<String>(types);
		for(int i=0;i<typeList.size();i++){
			if(i!=0){
				t=t+","+typeList.get(i);
			}else{
				t=t+typeList.get(i);
			}
		}
		t=t+")";
		result.append(new TypeBox(length,t).content());
		result.append(new ArrayBox(length,leftMargin, "Name", new ArrayList<String>(){{add(name);}}).content());
	}
	
	/**
	 * fetch team
	 */
	private void getTeams(){
		ArrayList<JSONObject> list = get1l(obj,"/sports/sports_league/teams");
		for(JSONObject jo:list){
			teams.add(get1Text(jo,"/sports/sports_league_participation/team"));
		}
		if(!teams.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Teams", teams));
		}
	}
	/**
	 * fetch leagues
	 */
	private void getLeagues(){
		ArrayList<JSONObject> list = get1l(obj,"/sports/sports_team/league");
		for(JSONObject jo:list){
			leagues.add(get1Text(jo,"/sports/sports_league_participation/league"));
		}
		if(!leagues.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Leagues", leagues));
		}
	}
	/**
	 * fetch locations
	 */
	private void getLocations(){
		locations = get1TextArray(obj,"/sports/sports_team/location");
		if(!locations.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Locations",locations));
		}
	}
	/**
	 * fetch description
	 */
	private void getDecription(){
		description = get2Text(obj,"/common/topic/description");
		if(description!=null){
			append(new DescriptionBox(length,leftMargin, description));
		}
	}
	/**
	 * fetch web
	 */
	private void getWeb(){
		officialWebsite = get1Text(obj,"/common/topic/official_website");
		if(officialWebsite!=null){
			append(new ArrayBox(length,leftMargin, "officialWebsite", new ArrayList<String>(){{add(officialWebsite);}}));
		}
	}
	/**
	 * fetch found
	 */
	private void getFound(){
		foundTime = get1Text(obj,"/sports/sports_team/founded");
		if(foundTime!=null){
			append(new ArrayBox(length,leftMargin, "Found Time", new ArrayList<String>(){{add(foundTime);}}));
		}
	}
	/**
	 * fetch Arena
	 */
	private void getArena(){
		arena = get1Text(obj,"/sports/sports_team/arena_stadium");
		if(arena!=null){
			append(new ArrayBox(length,leftMargin, "Arena", new ArrayList<String>(){{add(arena);}}));
		}
	}
	/**
	 * fetch Championship
	 */
	private void getChampionship(){
		championship = get1TextArray(obj,"/sports/sports_league/championship");
		if(!championship.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Championship", championship));
		}
	}
	/**
	 * fetch Championship
	 */
	private void getChampionships(){
		championships = get1TextArray(obj,"/sports/sports_team/championships");
		if(!championships.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Championships", championships));
		}
	}
	/**
	 * fetch getSport
	 */
	private void getSport(){
		sport = get1Text(obj,"/sports/sports_league/sport");
		if(sport!=null){
			append(new ArrayBox(length,leftMargin, "Sport", new ArrayList<String>(){{add(sport);}}));
		}
	}
	/**
	 * fetch teamSport
	 */
	private void getTeamSport(){
		sport = get1Text(obj,"/sports/sports_team/sport");
		if(sport!=null){
			append(new ArrayBox(length,leftMargin, "Sport", new ArrayList<String>(){{add(sport);}}));
		}
	}
	/**
	 * fetch getSlogan
	 */
	private void getSlogan(){
		slogan = get1TextArray(obj,"/organization/organization/slogan");
		if(!slogan.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Slogan", slogan));
		}
	}
	/**
	 * fetch getBirth
	 */
	private void getBirth(){
		birthday = get1Text(obj,"/people/person/date_of_birth");
		if(birthday!=null){
			append(new ArrayBox(length,leftMargin, "Birthday", new ArrayList<String>(){{add(birthday);}}));
		}
	}
	/**
	 * fetch getPlaceOfBirth
	 */
	private void getPlaceOfBirth(){
		placeOfBirth = get1Text(obj,"/people/person/place_of_birth");
		if(placeOfBirth!=null){
			append(new ArrayBox(length,leftMargin, "Place Of Birth", new ArrayList<String>(){{add(placeOfBirth);}}));
		}
	}
	/**
	 * fetch siblings
	 */
	public void getSiblings(){
		siblings=get2TextArray(obj,"/people/person/sibling_s","/people/sibling_relationship/sibling");
		if(!siblings.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Siblings", siblings));
		}
	}
	/**
	 * fetch spouses
	 */
	public void getSpouses(){
		ArrayList<JSONObject> list = get1l(obj,"/people/person/spouse_s");
		ArrayList<String> sp = new ArrayList<String>();
		for(JSONObject jo:list){
			spouses.add(new Spouse(get1Text(jo,"/people/marriage/spouse"),
					get1Text(jo,"/people/marriage/from"),
					get1Text(jo,"/people/marriage/to"),
					get1Text(jo,"/people/marriage/location_of_ceremony")));
		}
		if(!spouses.isEmpty()){
			for(Spouse s:spouses){
				sp.add(s.toString());
			}
			append(new ArrayBox(length,leftMargin, "spouses", sp));
		}
	}
	/**
	 * fetch books
	 */
	public void getBooks(){
		books = get1TextArray(obj,"/book/author/works_written");
		if(!books.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Books", books));
		}
	}
	/**
	 * fetch booksAboutAuthors
	 */
	public void getBooksAboutAuthor(){
		bookAboutAuthor = get1TextArray(obj,"/book/book_subject/works");
		if(!bookAboutAuthor.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Books about", bookAboutAuthor));
		}
	}
	/**
	 * fetch influenced
	 */
	public void getInfluenced(){
		influenced = get1TextArray(obj,"/influence/influence_node/influenced");
		if(!influenced.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Influenced", influenced));
		}
	}
	/**
	 * fetch getInfluenced
	 */
	public void getInfluencedBy(){
		influencedBy = get1TextArray(obj,"/influence/influence_node/influenced_by");
		if(!influencedBy.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Influenced", influencedBy));
		}
	}
	/**
	 * fetch film
	 */
	public void getFilm(){
		ArrayList<JSONObject> list = get1l(obj,"/film/actor/film");
		for(JSONObject jo:list){
			filmsParticipated.add(new Film(get1Text(jo,"/film/performance/film"),
					get1Text(jo,"/film/performance/character")));
		}
		if(!filmsParticipated.isEmpty()){
			Table t= new Table();
			t.add("Character", column2[0]);
			t.add("File Name", column2[1]);
			for(Film f:filmsParticipated){
				ArrayList<String> tmpList = new ArrayList<String>();
				tmpList.add(f.character);
				tmpList.add(f.name);
				t.addTuple(tmpList);
			}
			append(new TableBox(length,leftMargin, "Films", t));
		}
	}
	/**
	 * fetch leadership
	 */
	private void getLeadership(){
		ArrayList<JSONObject> list = get1l(obj,"/business/board_member/leader_of");
		for(JSONObject jo:list){
			leadership.add(new OrganizationRole(get1Text(jo,"/organization/leadership/from"),
					get1Text(jo,"/organization/leadership/to"),
					get1Text(jo,"/organization/leadership/organization"),
					get1Text(jo,"/organization/leadership/role"),
					get1Text(jo,"/organization/leadership/title")));
		}
		if(!leadership.isEmpty()){
			Table t= new Table();
			t.add("Organization", column4[0]);
			t.add("Role", column4[1]);
			t.add("Title", column4[2]);
			t.add("From-to", column4[3]);
			for(OrganizationRole or:leadership){
				ArrayList<String> tmpList = new ArrayList<String>();
				tmpList.add(or.organization);
				tmpList.add(or.role);
				tmpList.add(or.title);
				tmpList.add(or.from+" / "+or.to);
				t.addTuple(tmpList);
			}
			append(new TableBox(length,leftMargin, "Leadership", t));
		}
	}
	/**
	 * fetch boardMember
	 */
	private void getBoardMember(){
		ArrayList<JSONObject> list = get1l(obj,"/business/board_member/organization_board_memberships");
		for(JSONObject jo:list){
			boardMembers.add(new OrganizationRole(get1Text(jo,"/organization/organization_board_membership/from"),
					get1Text(jo,"/organization/organization_board_membership/to"),
					get1Text(jo,"/organization/organization_board_membership/organization"),
					get1Text(jo,"/organization/organization_board_membership/role"),
					get1Text(jo,"/organization/organization_board_membership/title")));
		}
		if(!boardMembers.isEmpty()){
			Table t= new Table();
			t.add("Organization", column4[0]);
			t.add("Role", column4[1]);
			t.add("Title", column4[2]);
			t.add("From-to", column4[3]);
			for(OrganizationRole or:boardMembers){
				ArrayList<String> tmpList = new ArrayList<String>();
				tmpList.add(or.organization);
				tmpList.add(or.role);
				tmpList.add(or.title);
				tmpList.add(or.from+" / "+or.to);
				t.addTuple(tmpList);
			}
			append(new TableBox(length,leftMargin, "Board Member", t));
		}
	}
	/**
	 * fetch coach
	 */
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
		if(!coach.isEmpty()){
			Table t= new Table();
			t.add("Name", column3[0]);
			t.add("Position", column3[1]);
			t.add("From-to", column3[2]);
			for(Coach c:coach){
				ArrayList<String> tmpList = new ArrayList<String>();
				tmpList.add(c.name);
				tmpList.add(c.position);
				tmpList.add(c.from+" / "+c.to);
				t.addTuple(tmpList);
			}
			append(new TableBox(length,leftMargin, "Coach", t));
		}
	}
	/**
	 * fetch roster
	 */
	private void getRoster(){
		ArrayList<JSONObject> list = get1l(obj,"/sports/sports_team/roster");
		for(JSONObject jo:list){
			if(get1Text(jo,"/sports/sports_team_roster/player")!=null){
			roster.add(new Roster(get1Text(jo,"/sports/sports_team_roster/player"),
					get1TextArray(jo,"/sports/sports_team_roster/position"),
					get1Text(jo,"/sports/sports_team_roster/number"),
					get1Text(jo,"/sports/sports_team_roster/from"),
					get1Text(jo,"/sports/sports_team_roster/to")));
			}
		}
		if(!roster.isEmpty()){
			Table t= new Table();
			t.add("Player", column4[0]);
			t.add("Postion", column4[1]);
			t.add("Number", column4[2]);
			t.add("From-to", column4[3]);
			for(Roster r:roster){
				ArrayList<String> tmpList = new ArrayList<String>();
				tmpList.add(r.name);
				tmpList.add(r.position());
				tmpList.add(r.number);
				tmpList.add(r.from+" / "+r.to);
				t.addTuple(tmpList);
			}
			append(new TableBox(length,leftMargin, "Roster", t));
		}
	}
	/**
	 * fetch death
	 */
	private void getDeath(){
		if(get1Text(obj,"/people/deceased_person/date_of_death")!=null){
			death = new Death(get1Text(obj,"/people/deceased_person/place_of_death"),
					get1Text(obj,"/people/deceased_person/date_of_death"),
					get1Text(obj,"/people/deceased_person/cause_of_death"));
		}
		if(death!=null){
			Table t= new Table();
			t.add("Date of Death", column3[0]);
			t.add("Cause of Death", column3[1]);
			t.add("Death Place", column3[2]);
			ArrayList<String> tmpList = new ArrayList<String>();
			tmpList.add(death.date);
			tmpList.add(death.cause);
			tmpList.add(death.place);
			t.addTuple(tmpList);
			append(new TableBox(length,leftMargin, "Death", t));
		}
	}
	/**
	 * fetch founded
	 */
	private void getFounded(){
		founded.addAll(get1TextArray(obj,"/organization/organization_founder/organizations_founded"));
		if(!founded.isEmpty()){
			append(new ArrayBox(length,leftMargin, "Founded", founded));
		}
	}
	/**
	 * get the JsonObjectArray in the first level with a name
	 * @param jo target JSONObject 
	 * @param name the key of the JSONObject
	 * @return
	 */
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
	/**
	 * get the only element with its value in the first level of a JSONObject with a key name
	 * @param jo
	 * @param name the key
	 * @return
	 */
	private String get2Text(JSONObject jo,String name){
		if(!get1l(jo,name).isEmpty()){
			return get1l(jo,name).get(0).getString("value");
		}
		return null;
	}
	/**
	 * get the only element  with its text in the first level of a JSONObject with a key name
	 * @param jo
	 * @param name the key
	 * @return
	 */
	private String get1Text(JSONObject jo,String name){
		if(!get1l(jo,name).isEmpty()){
			return get1l(jo,name).get(0).getString("text");
		}
		return null;
	}
	/**
	 * get element ArrayList in the first level of a JSONObject with a key name
	 * @param jo
	 * @param name
	 * @return
	 */
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
	/**
	 * get element ArrayList in the second level of a JSONObject with two key names
	 * @param jo
	 * @param name
	 * @param name2
	 * @return
	 */
	private ArrayList<String> get2TextArray(JSONObject jo,String name,String name2){
		ArrayList<JSONObject> jsonArray=get1l(jo,name);
		ArrayList<String> result = new ArrayList<String>();
		if(jsonArray!=null){
			for(JSONObject subJo:jsonArray){
				result.add(get1Text(subJo,name2));
			}
		}
		return result;
	}
	/**
	 * check the type of the object
	 * @return
	 */
	private boolean checkType(){
		ArrayList<JSONObject> jos = get1l(obj,"/type/object/type");
		for(JSONObject jo:jos){
			String id = jo.getString("id");
			if(typeMap.get(id)!=null){
				types.add(typeMap.get(id));
			}
		}
		if(!types.isEmpty()){
			return true;
		}
		return false;
	}
	public String toString(){
		return result.toString();
	}
	private void append(Box b){
		result.append(b.content());
	}
	private void addFinalLine(){
		result.append(" ");
		for(int i=1;i<length-1;i++){
			result.append("-");
		}
		result.append(" ");
	}
}
/**
 * this class is used for create the type box for the infobox
 * @author luomazhang
 *
 */
class TypeBox extends Box{
	SingleBox basic;
	public TypeBox(int length,String title){
		int len = title.length();
		String[] str = new String[1];
		str[0]=title;
		basic = new SingleBox(length,2,(length-len)/2,"",str);
	}
	@Override
	public String content() {
		// TODO Auto-generated method stub
		return basic.toString();
	}
}
/**
 * description box
 * @author luomazhang
 *
 */
class DescriptionBox extends Box{
	SingleBox basicBox;
	int rightMargin = 2;
	public DescriptionBox(int length,int leftMargin,String description){
		description = description.replaceAll("\n","");
		int lineLen = length-leftMargin-1-rightMargin;
		int descriptionHeight = (description.length()-1)/lineLen+1;
		String[] context = new String[descriptionHeight];
		for(int i=0;i<descriptionHeight;i++){
			int start=  i*lineLen;
			int end = (i+1)*lineLen>description.length()?description.length():(i+1)*lineLen;
			context[i]=description.substring(start,end);
		}
		basicBox = new SingleBox(length,descriptionHeight+1,leftMargin,"Description:",context);
	}
	@Override
	public String content() {
		// TODO Auto-generated method stub
		return basicBox.toString();
	}
}
/**
 * arrayBox
 * @author luomazhang
 *
 */
class ArrayBox extends Box{
	SingleBox basicBox;
	public ArrayBox(int length, int leftMargin,String title,ArrayList<String> textArray){
		String[] sa = new String[textArray.size()];
		for(int i=0;i<textArray.size();i++){
			sa[i]=textArray.get(i);
		}
		basicBox = new SingleBox(length,textArray.size()+1,leftMargin,title+":",sa);
	}
	@Override
	public String content() {
		// TODO Auto-generated method stub
		return basicBox.toString();
	}
}
/**
 * box with table
 * @author luomazhang
 *
 */
class TableBox extends Box{
	ArrayBox basicBox;
	public TableBox(int length,int leftMargin,String title, Table table){
		basicBox = new ArrayBox(length,leftMargin,title,table.result);
	}
	@Override
	public String content() {
		return basicBox.content();
	}
}
/**
 * table object used to create table inside a box
 * @author luomazhang
 *
 */
class Table{
	ArrayList<TableInfo> attributes= new ArrayList<TableInfo>();
	ArrayList<ArrayList<String>> tuples = new ArrayList<ArrayList<String>>();
	ArrayList<String> result = new ArrayList<String>(){{add("");}};
	private int length = 0;
	public void add(String name, int length){
		StringBuffer buffer = new StringBuffer("|"+name);
		for(int i=name.length()+1;i<length;i++){
			buffer.append(" ");
		}
		result.set(0,result.get(0)+buffer.toString());
		attributes.add(new TableInfo(name,length));
		this.length+=length;
	}
	public void addTuple(ArrayList<String> tuple){
		if(result.size()==1){
			StringBuffer split = new StringBuffer();
			for(int i=0;i<length;i++){
				split.append("-");
			}
			result.add(split.toString());
		}
		tuples.add(tuple);
		char[] tmp = new char[length];
		for(int i=0;i<tmp.length;i++){
			tmp[i]=' ';
		}
		int currIndex=0;
		for(int i=0;i<attributes.size();i++){
			tmp[currIndex]='|';
			insertInto(tmp,currIndex+1,currIndex+attributes.get(i).length,tuple.get(i)==null?"":tuple.get(i));
			currIndex+=attributes.get(i).length;
		}
		result.add(new String(tmp));
	}
	public void insertInto(char[] target, int startColumn,int endColumn, String str){
		StringBuffer sb=null;
		if(str.length()>endColumn-startColumn){
			sb=new StringBuffer();
			for(int i=0;i<endColumn-startColumn-3;i++){
				sb.append(str.charAt(i));
			}
			sb.append("...");
		}else{
			sb = new StringBuffer(str);
			endColumn= startColumn+str.length();
		}
		for(int i=startColumn;i<endColumn;i++){
			target[i]=sb.charAt(i-startColumn);
		}
	}
}
/**
 * store table information
 * @author luomazhang
 *
 */
class TableInfo{
	String name;
	int length;
	public TableInfo(String name,int length){
		this.name=name;
		this.length=length;
	}
}
/**
 * abstract box containing an abstract method
 * @author luomazhang
 *
 */
abstract class Box{
	abstract public String content();
}
/**
 * basic unit of a box object
 * @author luomazhang
 *
 */
class SingleBox{
	private String title;
	private String[] context;
	private int length;
	private int height;
	private int leftMargin;
	private char[][] matrix;
	public SingleBox(int length,int height, int leftMargin,String title,String[] context){
		this.length=length;
		this.height= height;
		this.leftMargin=leftMargin;
		this.context= context;
		this.title=title;
		matrix=  new char[height][length+1];
		appendFrame();
		appendTitle();
		appendLine();
	}
	private void appendFrame(){
		for(int i=0;i<height;i++){
			for(int j=0;j<length;j++){
				matrix[i][j]=' ';
			}
		}
		for(int i=1;i<height;i++){
			matrix[i][0]='|';
			matrix[i][length-1]='|';
		}
		for(int i=0;i<height;i++){
			matrix[i][length]='\n';
		}
		for(int i=1;i<length-1;i++){
			matrix[0][i]='-';
		}
	}
	private void appendLine(){
		for(int i=0;i<context.length;i++){
			insertInto(1+i,leftMargin,length,context[i]);
		}
	}
	private void appendTitle(){
		insertInto(1,2,leftMargin,title);
	}
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<height;i++){
			sb.append(new String(matrix[i]));
		}
		return sb.toString();
	}
	public void insertInto(int startRow, int startColumn,int endColumn, String str){
		StringBuffer sb=null;
		if(str.length()>endColumn-startColumn){
			sb=new StringBuffer();
			for(int i=0;i<endColumn-startColumn-3;i++){
				sb.append(str.charAt(i));
			}
			sb.append("...");
		}else{
			sb = new StringBuffer(str);
			endColumn= startColumn+str.length();
		}
		for(int i=startColumn;i<endColumn;i++){
			matrix[startRow][i]=sb.charAt(i-startColumn);
		}
	}
}
/**
 * object to hold organization roles
 * @author luomazhang
 *
 */
class OrganizationRole{
	String from;
	String to;
	String organization;
	String role;
	String title;
	public OrganizationRole(String from, String to, String organization,String role,String title){
		this.from = from;
		if(to==null){
			to="now";
		}
		this.to=to;
		this.organization =organization;
		this.role=role;
		this.title=title;
	}
}
/**
 * class to store death information
 * @author luomazhang
 *
 */
class Death{
	String place="";
	String date="";
	String cause="";
	public Death(String place,String date,String cause){
		if(place!=null) this.place=place;
		if(date!=null) this.date=date;
		if(cause!=null) this.cause=cause;
	}
}
/**
 * class to store film information
 * @author luomazhang
 *
 */
class Film{
	String name;
	String character;
	public Film(String name,String character){
		this.name=name;
		this.character=character;
	}
}
/**
 * class to store coach information
 * @author luomazhang
 *
 */
class Coach{
	String name;
	String position;
	String from;
	String to;
	public Coach(String name,String position,String from, String to){
		this.name=name;
		this.position=position;
		this.from=from;
		if(to==null){
			to="now";
		}
		this.to=to;
	}
}
/**
 * class to store roster
 * @author luomazhang
 *
 */
class Roster{
	String name;
	ArrayList<String> position;
	String number;
	String from;
	String to;
	public Roster(String name,ArrayList<String> position,String number,String from, String to){
		this.name=name;
		this.position=position;
		this.number=number;
		this.from=from;
		if(to==null){
			to="now";
		}
		this.to=to;
	}
	public String position(){
		if(position.size()>0){
			String result = position.get(0);
			for(int i=1;i<position.size();i++){
				result+=" , "+position.get(i);
			}
			return result;
		}else{
			return "";
		}
	}
}
/**
 *  class to store spouse
 * @author luomazhang
 *
 */
class Spouse{
	String name;
	String startDate;
	String endDate;
	String place;
	public Spouse(String name,String startDate,String endDate,String place){
		this.name= name;
		this.startDate=startDate;
		if(endDate==null){
			endDate="now";
		}
		this.endDate=endDate;
		this.place=place;
	}
	public String toString(){
		String p = place==null?"":" @ "+place;
		String result = name+" ("+startDate+" - "+endDate+")"+p;
		return result;
	}
}
class NoTypeException extends Exception{};