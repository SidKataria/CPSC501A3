import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Class to create XML of objects
 * @author sid
 *
 */
public class SerializeObj extends Serializer {
	public File serializedFile = null;
	
	public SerializeObj (ArrayList<Object> objList) {
		try {
			for (Object o : objList) {
				Class objClass = o.getClass();
				System.out.println("Serializing Object"); 
				Document doc = serialize(o);
				System.out.println("File Created"); 
				serializedFile = makeXML(doc);
			}
		} catch (Exception e) { e.printStackTrace();}
	}

	private File makeXML(Document doc) {
		File file = new File("output.xml");
		XMLOutputter xmlOut = new XMLOutputter();
		xmlOut.setFormat(Format.getPrettyFormat());
		
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			xmlOut.output(doc, bw);
			bw.close();
		} catch (Exception e) { e.printStackTrace();}
		return file;
	}
}
