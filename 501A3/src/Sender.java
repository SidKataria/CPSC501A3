import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class to send JDOM doc
 * 
 * Sample Code from stackOverFlow
 * @author sid
 *
 */
public class Sender {
	private static ObjCreater objCreator;
	private static SerializeObj serializer;
	private static Object obj;
	private static String objString;
	private static ArrayList<Object> objList = new ArrayList<>();

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		objCreator = new ObjCreater();
		serializer = null;
		obj = null;
		objString = null;
		
		System.out.println("Pick one or multiple options, seperated by a space for type of objects to create:\n" +
		        "1. Primitives\n" +
		        "2. References\n" +
		        "3. Array of Primitives\n" +
		        "4. Array of Object References\n" +
		        "5. Collections\n");
		//Receive user input
		objString = input.nextLine();
		String[] objectArray = objString.split(" ");
		for (String object : objectArray) {
			switch (object) {
			case "1" : {
				objList.add(objCreator.createPrimitiveObj());
				break;
				}
			case "2" : {
				objList.add(objCreator.createPriRefObj());
				break;
				}
			case "3" : {
				objList.add(objCreator.createPriArrObj());
				break;
				}
			case "4" : {
				objList.add(objCreator.createRefArrObj());
				break;
				}
			case "5" : {
				objList.add(objCreator.createCollectionObj());
				break;
				}
			default :
				System.out.println("Invalid Coice...");
				break;
				}
			}
		serializer = new SerializeObj(objList);
		sendFile("localhost", 8000, serializer.serializedFile);
	}

	//Method to send file
	private static void sendFile(String host, int port, File file) {
		try {
			System.out.println("Connecting to host: " + host + "\n" + "port: " + port);
			Socket sock = new Socket(host, port);
			System.out.println("Connected to: " + sock.getRemoteSocketAddress());
			
			OutputStream outputStr = sock.getOutputStream();
			FileInputStream inputStr = new FileInputStream(file);
			
			byte[] fileB = new byte[2048*2048];
			int bytes = 0;
			
			while ( (bytes = inputStr.read(fileB)) > 0 )
				outputStr.write(fileB, 0, bytes); 
			
			inputStr.close();
			outputStr.close();
			sock.close();
			
			System.out.println("File Transmitted...");
		} catch (Exception e) { e.printStackTrace();}
	}
}
