package client;
import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.Scanner;

import server.Server;

public class ClientApp {
	private static DataOutputStream dout;
	public static String fileName, sharedDir;
	private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    public static int myPeer_id;
    public static MessageFormat MF;
    public static String dir = "C:/Users/USER/eclipse-workspace 2/p2p App/src/";
    
    public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException  {
    	Scanner scan = new Scanner(System.in);
        InetAddress ip = InetAddress.getByName("localhost");    
  
        System.out.println("Introduire ton id:");
        myPeer_id = scan.nextInt();
        scan.nextLine();
    	sharedDir = dir+"Peer"+Integer.toString(myPeer_id);
        File file = new File(dir+"topology.txt");
    	InputStream is = new FileInputStream(file);
		Properties prop = new Properties();
    	prop.load(is);
    	int next_peer_id = Integer.parseInt(prop.getProperty("peer"+myPeer_id+".next"));
        int next_peer_port = Integer.parseInt(prop.getProperty("peer"+next_peer_id+".serverport"));
    	int serverPort = Integer.parseInt(prop.getProperty("peer"+myPeer_id+".serverport"));
    	int downloadport = Integer.parseInt(prop.getProperty("peer"+myPeer_id+".downloadport"));
    	MF = new MessageFormat();
    	MF.fromPeerId = myPeer_id;
		MF.fromPeerDownloadPort = downloadport;
		
    	Server myServer = new Server(serverPort, sharedDir, myPeer_id);
    	myServer.start();

    	
    	
    	while (true) {
	        System.out.println("Introduire le nom de fichier à chercher :");
	    	fileName  = scan.nextLine();    
	    	MF.fileName = fileName;
	        try{
	        	Socket s = new Socket(ip, next_peer_port);
	        	OutputStream os = s.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
				oos.writeObject(MF);
		
			} catch(IOException io) {
				System.out.println("Problème de connexion");
			}
	        
	        DownloadHandlerServ downloadServ = new DownloadHandlerServ(downloadport, sharedDir, fileName);
	    	downloadServ.start();
	        downloadServ.join();
    	}
  	 
    }
}
