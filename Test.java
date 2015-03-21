import java.util.ArrayList;
public class Test {
	public static void main(String[] args){
		ArrayList<String> a = FreeBase.topic("/m/017nt").founded;
		for(String l:a){
			System.out.println(l);
		}
	}
}
