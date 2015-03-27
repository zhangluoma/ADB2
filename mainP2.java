import java.util.*;

/**
* Main entry of part 2
*
* @author  Luoma Zhang, Huaiyuan Cao
* @version 1.0
* @since   2015.3.25
*/

public class mainP2 {
	
	public static void main(String[] args){
		Scanner stdin = new Scanner(System.in);
		System.out.println("Question? Please in the format of <who created X?> ");
		String inp = stdin.nextLine().trim();
		String[] str = inp.split(" ");
		// handle bad querys
		if(str.length<3){
			System.out.println("wrong query question");
			System.exit(1);
		}
		String query = "";

		//delete ? in the last word
		String last = str[str.length-1];
		last = last.substring(0,last.length()-1);
		str[str.length-1] = last;
		
		for (int i = 2 ; i<str.length ; i++){
			query = query + str[i] +" ";
		}
		query = query.trim();
		
		//get BusinessPerson
		HashMap<String,ArrayList<String>> res1 = GetCreator.getBusiness(query);
		int count = 1;
		for(String name : res1.keySet()){
			System.out.print(count+". ");
			System.out.print(name + " (as Businessperson) created");
			ArrayList<String> arr = res1.get(name);
			for (int i = 0; i<arr.size(); i++){
				if(i == arr.size()-1){
					System.out.print("and ");
					System.out.print("<"+arr.get(i)+">");
				}else{
					System.out.print("<"+arr.get(i)+">");
					System.out.print(", ");
				}
			}
			System.out.print("\n");
			count++;
		}
		//get Author
		HashMap<String,ArrayList<String>> res2 = GetCreator.getAuthor(query);
		for(String name : res2.keySet()){
			System.out.print(count+". ");
			System.out.print(name + " (as Author) created ");
			ArrayList<String> arr = res2.get(name);
			for (int i = 0; i<arr.size(); i++){
				if(i == arr.size()-1){
					System.out.print("and ");
					System.out.print("<"+arr.get(i)+">");
				}else{
					System.out.print("<"+arr.get(i)+">");
					System.out.print(", ");
				}
			}
			System.out.print("\n");
			count++;
		}

	}
}
