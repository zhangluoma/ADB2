import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONException;
public class Test {
	public static void main(String[] args){
		//NameQuery.queryCreator("Microsoft");
		Infobox ib =new Infobox();
		for(Entity e : FreeBase.search("Bill Gates")){
			try {
				ib = FreeBase.topic(e.mid);
			}catch (NoTypeException e1) {
				// TODO Auto-generated catch block
				continue;
			}
			break;
		}
		System.out.println(ib);
	}
}
