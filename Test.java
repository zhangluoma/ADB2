import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONException;
public class Test {
	public static void main(String[] args){
		Infobox ib =null;
		for(Entity e : FreeBase.search("Michael Jordan")){
			try {
				ib = FreeBase.topic(e.mid);
			}catch (NoTypeException e1) {
				// TODO Auto-generated catch block
				continue;
			}
			break;
		}
		for(String str:ib.types){
			System.out.println(str);
		}
		System.out.println(ib.name);
	}
}
