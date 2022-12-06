package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;
import client.MessageFormat;

public class ResponseHandlerClient extends Thread{	
	public static DataOutputStream dout;
	public static OutputStream out;
	public static int fromPeerId;
	public static int fromPeerDownloadPort;
	public static boolean fileFound;
	public static MessageFormat MF;
	public static String sharedDir;
	public static int myPeerId;
    public static ForwardClient fc;
    public static String dir = "C:/Users/USER/eclipse-workspace 2/p2p App/src/";
    public static boolean searchCompleted = false;
    public static int next_peer_id;
    public static int next_peer_port;
    
	public ResponseHandlerClient(MessageFormat MF, boolean fileFound, String sharedDir, int myPeerId) {
		super();
		this.sharedDir = sharedDir;
		this.myPeerId = myPeerId;
		this.MF = MF;
		this.fileFound = fileFound;
	}
	
	@Override
	public void run () {
		try {
			searchCompleted = false;
			InetAddress ip = InetAddress.getByName("localhost");  
        	Socket respSocket = new Socket(ip, MF.fromPeerDownloadPort);
        	out = respSocket.getOutputStream();
			dout=new DataOutputStream(out);
			dout.writeBoolean(fileFound);
			dout.writeInt(myPeerId);
			
			InputStream is = new FileInputStream(dir+"topology.txt");
       		Properties prop = new Properties();
           	prop.load(is);
       		next_peer_id = Integer.parseInt(prop.getProperty("peer"+myPeerId+".next"));

			if (fileFound) {
				sendFile();
				searchCompleted = true;
			} else {
	       		if (next_peer_id == MF.fromPeerId) {
	       			searchCompleted = true;
	       		}
			}
			dout.writeBoolean(searchCompleted);
			respSocket.close();
			if (!searchCompleted) {
				next_peer_port = Integer.parseInt(prop.getProperty("peer"+next_peer_id+".serverport"));
	       		fc = new ForwardClient(myPeerId, MF, next_peer_port);
				fc.start();
				fc.join();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sendFile() throws Exception {
        int bytes = 0;
        File file = new File(sharedDir+"/"+MF.fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        dout.writeLong(file.length());
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fileInputStream.read(buffer)) != -1) {
        	dout.write(buffer, 0, bytes);
        	dout.flush();
        }
        fileInputStream.close();
	}
}
