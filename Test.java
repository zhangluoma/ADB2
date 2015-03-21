import java.util.ArrayList;
public class Test {
	public static void main(String[] args){
		ArrayList<Entity> a = FreeBase.search("Bill Gates");
		for(Entity e:a){
			System.out.println(e.mid);
		}
	}
}
