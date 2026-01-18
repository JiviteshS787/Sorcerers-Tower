import java.util.*;

public class SortByTime implements Comparator <String>{

	// Description: Compares the times of the users and helps sort.
	// Parameter(s): String o1, o2
	// Return(s): int
	public int compare(String o1, String o2) {
		String input1 = o1;
		String input2 = o2;
		String time1 = input1.substring(input1.lastIndexOf(" "));
		String time2 = input2.substring(input2.lastIndexOf(" "));
		// Compare the times.
		return time1.compareTo(time2);
	}

}
