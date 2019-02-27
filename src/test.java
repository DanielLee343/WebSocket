
public class test {
	public static void main(String[] args) {
		String message = "101我是神Guest9";
		String a = message.replace("101", "");
		System.out.println(a);
		String toUser = a.substring(a.length()-6);
		System.out.println(toUser);
		String bg = a.replace(toUser, "");
		String filter = String.format("%s: %s", "af", bg);
		System.out.println(filter);
		
		String whatever = "uhsadfoaudaGuest";
	}
}
