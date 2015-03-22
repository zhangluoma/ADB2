import java.util.ArrayList;
public class Test {
	public static void main(String[] args){
		ArrayList<OrganizationRole> s = FreeBase.topic("/m/017nt").boardMembers;
		System.out.println(s.get(0).organization);
	}
}
