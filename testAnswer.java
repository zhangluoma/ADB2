import java.util.ArrayList;

public class testAnswer {
	public static void main(String[] args){
		
		String people = "Bill Gates";
		String target = "Microsoft";
		target = target.toLowerCase();
		
		Infobox ib =null;
		for(Entity e : FreeBase.search(people)){
			try {
				ib = FreeBase.topic(e.mid);
			}catch (NoTypeException e1) {
				// TODO Auto-generated catch block
				continue;
			}
			break;
		}
		ArrayList<String> result = new ArrayList<String>();
		for(String str:ib.types){
			if(str.equals("Author")){
				for(String book:ib.books){
					String booktmp = book.toLowerCase();
					if(booktmp.indexOf(target)>=0){
						result.add(book);
					}
				}
			}
			if(str.equals("BusinessPerson")){
				for(String company:ib.founded){
					String companytmp = company.toLowerCase();
					if(companytmp.indexOf(target)>=0){
						result.add(company);
					}
				}
			}
		}
		for(int i = 0; i<result.size();i++){
			System.out.println(result.get(i));
		}
		
	}
}
