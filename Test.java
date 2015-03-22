import java.util.ArrayList;
public class Test {
	public static void main(String[] args){
<<<<<<< HEAD
		ArrayList<OrganizationRole> s = FreeBase.topic("/m/017nt").boardMembers;
		System.out.println(s.get(0).organization);
=======
		String b = FreeBase.topic("/m/0jm3v").arena;
		ArrayList<String> a = FreeBase.topic("/m/0jm3v").championships;
		ArrayList<Roster> c = FreeBase.topic("/m/0jm3v").roster;
		System.out.println(b);
		for(String l:a){
			System.out.println(l);
		}
		System.out.println(c.get(0).name);
>>>>>>> 7b0dfb04e8b8c1d6ee3e72f0e7c6eb56487b1d0b
	}
}
