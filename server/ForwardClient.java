package server;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

import client.MessageFormat;

public class ForwardClient extends Thread {
	public static int myPeerId;
	public static MessageFormat MF;
	public static int next_peer_port;
	
	public ForwardClient(int myPeerId, MessageFormat MF, int next_peer_port) {
		super();
		this.myPeerId = myPeerId;
		this.MF = MF;
		this.next_peer_port = next_peer_port;
	}
	
	@Override
	public void run () {
        try{
        	InetAddress ip = InetAddress.getByName("localhost");  

        	Socket s = new Socket(ip, next_peer_port);
        	OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			oos.writeObject(MF);
			
			oos.close();
			os.close();
			s.close();
			
		} catch(IOException io) {
			io.printStackTrace();
		}
	}
}