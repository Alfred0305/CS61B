public class LeapYear{
	public static void main(String[] args) {

		int year = 2000;

		if (isleap(year)){
			System.out.println(year + " is a leap year.");
		}
		else{
			System.out.println(year + " is not a leap year.");
		}

	}

	public static boolean isleap(int yearinput){
		if (yearinput % 400 == 0){
			return true;
		}
		else if (yearinput % 4 == 0 & yearinput % 100 != 0){
			return true;
		}
		else{
			return false;
		}

	}
}