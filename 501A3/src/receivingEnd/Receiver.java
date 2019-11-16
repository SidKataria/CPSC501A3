package receivingEnd;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;

/**
 * Class for receiving file over local connection
 * @author sid
 *
 */
public class Receiver extends Thread{
	private Socket sock;
	private static ServerSocket sockServer;
	
	public Receiver(int port) {
		try {
			sockServer = new ServerSocket(port);
			sockServer.setSoTimeout(600000);
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public static void main (String[] args) {
		try {
			int port = 8000;
			Thread thread = new Receiver(port);
			thread.start();
		} catch (Exception e) { e.printStackTrace();}
	}
	
    //Overriding method
    public void run(){
        while(true){
            try{
                System.out.println("Server running on " + sockServer.getLocalPort());
                sock = sockServer.accept();
                System.out.println("Server connected to " + sock.getRemoteSocketAddress());
                File file =  new File("received.xml");
          
                InputStream inputStream = sock.getInputStream();
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                byte[] fileB = new byte[2048 * 2048];
                int bytes = 0;
                while((bytes =  inputStream.read(fileB)) > 0){
                    fileOutputStream.write(fileB, 0, bytes);
                    break;
                }
                
                System.out.println("File received.");
                System.out.println("Creating Objects...");
                
                SAXBuilder saxBuilder = new SAXBuilder();
                Document document = (Document) saxBuilder.build(file);
                Object obj = Deserializer.deserialize(document);

                //visualizing objects
                System.out.println("----------------------------------------\n");
                Inspector inspectorGadget = new Inspector();
                inspectorGadget.inspect(obj, false);
                System.out.println("----------------------------------------------");
                System.out.println();

                //close socket
                sock.close();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
