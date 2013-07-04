package bacci.giovanni.biogiova.sequencing;

public class FormatUtils {
	/**
	 * Transpose a double array as example:
	 * <pre>
	 * {@code
	 * Object[][] array = {{Obj1,Obj2,Obj3,Obj4},
	 *                     {Obj5,Obj6,Obj7,Obj8}};
	 * FormatUtils.transpose(array) = {{Obj1,Obj5},
	 *                                 {Obj2,Obj6},
	 *                                 {Obj3,Obj7},
	 *                                 {Obj4,Obj8}}; 
 	 * }
	 * </pre>
	 * @param array the array to be transposed
	 * @return
	 */
	public static char[][] transpose(char[][] array) {
		char[][] output = new char[array[0].length][array.length];
		int index = 0;
		for (char[] s : array) {
			for (int m = 0; m < s.length; m++) {
				output[m][index] = s[m];
			}
			index++;
		}
		return output;
	}
}
