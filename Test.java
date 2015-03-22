import java.util.ArrayList;
public class Test {
	public static void main(String[] args){
		String b = FreeBase.topic("/m/0jm3v").arena;
		ArrayList<String> a = FreeBase.topic("/m/0jm3v").championships;
		ArrayList<Roster> c = FreeBase.topic("/m/0jm3v").roster;
		System.out.println(b);
		for(String l:a){
			System.out.println(l);
		}
		System.out.println(c.get(0).name);
	}
}
