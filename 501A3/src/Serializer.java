import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.IdentityHashMap;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serializer {
	private static IdentityHashMap objMap = new IdentityHashMap<>();
	private static Element value;
	private static Element ref;
	private static Element field;
	private static ArrayList<Element> valArr;
	private static ArrayList<Element> valElement;
	private static ArrayList<Element> refArr;
	
	public static org.jdom2.Document serialize(Object obj) {
		Element rootNode = new Element("serialized");
		Document rootDoc = new Document(rootNode);
		
		return serializeObj(obj, rootDoc);
	}

	public static Document serializeObj(Object obj, Document doc) {
		Element objElement = null;
		Element type = null;
		try {
			Class objClass = obj.getClass();
			String id = Integer.toString(objMap.size());
			
			//Adding object ID to hash map
			objMap.put(id, obj);
			
			//Nested Class Attributes
			objElement = nestClass(objClass, id);
			doc.getRootElement().addContent(objElement);
			
			//If of type Array
			if (objClass.isArray()) {
				objElement = nestArray(objElement, obj, doc);
			}
			
			//Serializing feilds
			System.out.println("serializing fields");
			objElement = nestField(objElement, obj, doc);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public static Element nestClass(Class objClass, String id) {
		Element element = new Element("object");
		element.setAttribute("class", objClass.getName());
		element.setAttribute("id", id);
		return element;
	}
	
	public static Element nestArray(Element element, Object object, Document doc) {
		valArr = new ArrayList<>();
		valElement = new ArrayList<>();
		refArr = new ArrayList<>();
		
		element.setAttribute("Length", String.valueOf(Array.getLength(object)));
		Class type = object.getClass().getComponentType();
		
		for(int i = 0; i < Array.getLength(object); i++) {
			if(type.isPrimitive()) {
				value = new Element("value");
				String val = String.valueOf(Array.get(object, i));
				value.addContent(val);
				valArr.add(value);
				valElement = valArr;
			}
			else {
				ref = new Element("reference");
				ref.addContent(Integer.toString(objMap.size()));
				refArr.add(ref);
				valElement = refArr;
				
				//Checking recursively
				if (!objMap.containsKey(Integer.toString(objMap.size()))); {
					Object arrObj = Array.get(object, i);
					serializeObj(arrObj, doc);
				}
			}
		}
		return element.setContent(valElement);
	}
	
	public static Element nestField(Element element, Object object, Document doc) {
		Field[] fieldObj = object.getClass().getDeclaredFields();
		for (Field f : fieldObj) {
			try {
				if(!Modifier.isPublic(f.getModifiers()))
					f.setAccessible(true);
				Object objField = f.get(object);
				field = serializeField(f, doc, object, objField);
				element.addContent(field);
			} catch (Exception e) { e.printStackTrace(); }
		}
		return element;
	}

	private static Element serializeField(Field f, Document doc, Object object, Object objField) {
		if (objField != null) {
			try {
				field = new Element("field");
				value = new Element("value");
				ref = new Element("reference");
				Class type = f.getType();
				
				field.setAttribute("name", f.getName());
				field.setAttribute("declaringClass", f.getDeclaringClass().getName());
				
				if (type.isPrimitive()) {
					String val = f.get(object).toString();
					value.addContent(val);
					field.setContent(value);
				}
				else {
					String id = Integer.toString(objMap.size());
					ref.addContent(id);
					field.setContent(ref);
					serializeObj(f.get(object), doc);
				}	
			} catch (Exception e) { e.printStackTrace(); }
		}
		else {
			field = new Element("null");
		}
		return field;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
