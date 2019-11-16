import java.util.Scanner;
import java.util.Vector;

/**
 * CPSC 501 - A3
 * Class to create arbitrary objects as controlled by the user.
 * The following kinds of objects can be created:
 * 		An obj with only primitive for instance variables for which values can be set by the user,
 * 		An obj that contains reference to other objects,
 * 		An obj that contains an array of primitives,
 * 		An obj that contains an array of obj references,
 * 		An obj that uses an instance of one of Java;s collection classes to refer to several other objects.
 * At the end, a XML form of the obj that user made is printed to the GUI
 * @author siddharth kataria
 * UCID: 30000880
 *
 */
public class ObjCreater {

	private static boolean checker = true;
	PrimitiveObj priObj = null;
	ReferenceObj refObj = null;
	PrimitiveArrayObj priArrObj = null;
	ReferenceArrayObj refArrObj = null;
	CollectionObj collectionObj = null;
	String charstring;
	int intvalue;
	float floatvalue;
	boolean boolvalue;
	int arrlength;
	
	private static Scanner input = new Scanner(System.in);
	
	//Method to create objects with only primitive for instance variables with custom values
	public PrimitiveObj createPrimitiveObj() {
		System.out.println("Wish to not create any instance variables? Please enter 'n' if you dont, anything else to continue...");
		try {
			inputHandler(0);
			if (checker = true) {
				System.out.println("Create primitive instance variables!");
				System.out.println("Please enter an interger value to create a primitive integer object...");
				inputHandler(1);
				intvalue = input.nextInt();
				System.out.println("Please enter a floating interger value to create a primitive float object...");
				inputHandler(2);
				floatvalue = input.nextFloat();
				System.out.println("Please enter a boolean value to create a primitive boolean object");
				inputHandler(3);
				boolvalue = input.nextBoolean();
				
				priObj = new PrimitiveObj(intvalue, floatvalue, boolvalue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return priObj;
	}
	
	//Method to create instances of reference primitive objects
	public ReferenceObj createPriRefObj() {
		System.out.println("Wish to not create any instance reference objects? Please enter 'n' if you dont, anything else to continue...");
		try {
			inputHandler(0);
			if (checker == true) {
				System.out.println("Creating a reference object, you will now be asked to input primitive values for the object...");
				refObj = new ReferenceObj(createPrimitiveObj());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return refObj;
	}
	
	//Method to create an array object containing char primitives
	public PrimitiveArrayObj createPriArrObj() {
		System.out.println("Wish to not create any character arrays? Please enter 'n' if you dont, anything else to continue...");
		try {
			inputHandler(0);
			if (checker == true) {
				System.out.println("Creating an array containing primitve characters...");
				System.out.println("Please enter the desired array's length");
				inputHandler(1);
				arrlength = input.nextInt();
				char[] newArr = new char[arrlength];
				System.out.println("Please enter a character for each array entry");
				createPrimitiveArray(newArr);
				priArrObj = new PrimitiveArrayObj(newArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return priArrObj;
	}
	
	//Method to create an array of objects
	public ReferenceArrayObj createRefArrObj() {
		System.out.println("Wish to not create any object reference array? Please enter 'n' if you dont, anything else to continue...");
		try {
			inputHandler(0);
			if (checker == true) {
				System.out.println("Creating a reference object array...");
				System.out.println("Please enter the desired array's length");
				inputHandler(1);
				arrlength = input.nextInt();
				Object[] newArr = new Object[arrlength];
				System.out.println("Please enter object values for each array entry");
				createObjArray(newArr);
				refArrObj = new ReferenceArrayObj(newArr);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return refArrObj;
	}
	
	//Method to create instance of one of Java's collection classes
	public CollectionObj createCollectionObj() {
		System.out.println("Wish to not create any collection objects? Please enter 'n' if you dont, anything else to continue...");
		try {
			inputHandler(0);
			if (checker == true) {
				System.out.println("Creating a Java collection object...");
				Vector<Object> obj = new Vector<Object>();
				while (checker) {
					System.out.println("Wish to not add object to collection class? Please enter 'n' if you dont, anything else to continue...");
					inputHandler(0);
					if (!checker)
						break;
					obj.add(createPrimitiveObj());
				}
				collectionObj = new CollectionObj(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return collectionObj;
	}
	
	//Method to create custom char array
	private void createPrimitiveArray(char[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.println("Enter a character for index: " + i);
			inputHandler(4);
			charstring = charstring + input.toString();
		}
		array = charstring.toCharArray();
	}
	
	//Method to create custom object array
		private void createObjArray(Object[] array) {
			for (int i = 0; i < array.length; i++) {
				System.out.println("Enter object values for index: " + i);
				array[i] = createPrimitiveObj();
			}
		}
	
	//Method to check user inputs
	public static void inputHandler(int type) {
		if (type == 0) {
			String ans = input.next();
			if (ans.equals("n")) {
				checker = false;
			}
			else { checker = true; }
		}
		else if (type == 1) {
			while (!input.hasNextInt()) {
				System.out.println("Please input a valid integer value");
				input.next();
			}
			checker = true;
		}
		else if (type == 2) {
			while (!input.hasNextFloat()) {
				System.out.println("Please input a valid floating integer value");
				input.next();
			}
			checker = true;
		}
		else if (type == 3) {
			String boolval = input.next();
			while ((!boolval.contentEquals("true")) || !boolval.contentEquals("false")) {
				System.out.println("Please enter either true or false");
				boolval = input.next();
			}
			checker = true;
		}
		else if (type == 4) {
			String character = input.next();
			while (character.length() != 1) {
				System.out.println("Please only enter a single character");
				character = input.next();
			}
		}
	}
}
