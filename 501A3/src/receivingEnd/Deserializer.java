package receivingEnd;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
/**
 * Class to deserialize a file
 * @author sid
 *
 */
public class Deserializer {

    
   public static Object deserialize(Document document){
       Element rootElement = document.getRootElement();
       List objList = rootElement.getChildren();
       HashMap objMap =  new HashMap();
       Object obj = null;

       try{
           createObjInstances(objList, objMap);
           setFieldValues(objList, objMap);
           obj = objMap.get("0");
       } catch (Exception e){ e.printStackTrace(); }

       return obj;
   }
   
   private static void createObjInstances(List objList, HashMap objMap){
       for(int i = 0; i < objList.size(); i++){
           try{
               Element element = (Element) objList.get(i);
               Class objClass =  Class.forName(element.getAttributeValue("class"));

               Object objInstance;
               if(objClass.isArray()){
                   int arrlength = Integer.parseInt(element.getAttributeValue("length"));
                   Class type = objClass.getComponentType();
                   objInstance = Array.newInstance(type, arrlength);
               }
               else {
                   Constructor constructor =  objClass.getConstructor(null);
                   
                   if(!Modifier.isPublic(constructor.getModifiers()))
                       constructor.setAccessible(true);

                   objInstance = constructor.newInstance(null);
               }
               String objId = element.getAttributeValue("id");
               objMap.put(objId, objInstance);
           } catch(Exception e) { e.printStackTrace(); }
       }
   }

   private static Object deserializeField(Class type, Element element){
       Object valueObject = null;

       //Code taken from stackOverFlow
       if(type.equals(int.class))
           valueObject = Integer.valueOf(element.getText());
       else if(type.equals(byte.class))
           valueObject = Byte.valueOf(element.getText());
       else if(type.equals(short.class))
           valueObject = Short.valueOf(element.getText());
       else if(type.equals(long.class))
           valueObject = Long.valueOf(element.getText());
       else if(type.equals(float.class))
           valueObject = Float.valueOf(element.getText());
       else if(type.equals(double.class))
           valueObject = Double.valueOf(element.getText());
       else if(type.equals(boolean.class)){
           String boolString = element.getText();
           if(boolString.equals("true"))
               valueObject = Boolean.TRUE;
           else
               valueObject = Boolean.FALSE;
       }

       return valueObject;
   }
   
   private static Object deserializeElement(Class type, Element element, HashMap objMap){
       Object contentObject;
       String contentType = element.getName();

       if(contentType.equals("reference"))
           contentObject = objMap.get(element.getText());
       else if(contentType.equals("value"))
           contentObject = deserializeField(type, element);
       else
           contentObject = element.getText();

       return contentObject;
   }
   
   private static void setFieldValues(List objList, HashMap objMap){
       for(int i =0; i < objList.size(); i++){
           try{
               Element element = (Element) objList.get(i);
               Object obj = objMap.get(element.getAttributeValue("id"));

               List objChildList = element.getChildren();

               Class objClass = obj.getClass();
               
               if(objClass.isArray()){
                   Class type =  objClass.getComponentType();
                   for(int j= 0; j < objChildList.size(); j++){
                       Element arrElement = (Element) objChildList.get(j);
                       Object arrVal = deserializeElement(type, arrElement, objMap);
                       Array.set(obj, j, arrVal);
                   }
               }
               else{
                   for(int j = 0; j < objChildList.size(); j++){
                       Element f = (Element) objChildList.get(j);

                       Class declaringClass =  Class.forName(f.getAttributeValue("declaringclass"));
                       String fieldName = f.getAttributeValue("name");
                       Field field = declaringClass.getDeclaredField(fieldName);

                       if(!Modifier.isPublic(field.getModifiers())){
                           field.setAccessible(true);
                           Field mods = Field.class.getDeclaredField("modifiers");
                           mods.setAccessible(true);
                           mods.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                       }

                       Class fieldType = field.getType();
                       Element fieldElement = (Element) f.getChildren().get(0);
                       Object fieldContent = deserializeElement(fieldType, fieldElement, objMap);
                       field.set(obj, fieldContent);
                   }
               }
           }
           catch(Exception e){
               e.printStackTrace();
           }
       }
   }
}
