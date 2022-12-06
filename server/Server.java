package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import client.MessageFormat;

public class Server extends Thread {
    private static DataOutputStream dataOutputStream = null;
    private static DataInputStream dataInputStream = null;
    public static int number_of_peers;
    public static String fileName;
	public static int serverPort;
	public static String sharedDir;
    public static MessageFormat MF;
    public static int myPeerId;
    public static int fromPeerId;

    public static ResponseHandlerClient respClient;
    public static int downloadport;

	public Server(int serverPort, String sharedDir, int myPeerId) {
		super();
		this.serverPort = serverPort;
		this.sharedDir = sharedDir;
		this.myPeerId = myPeerId;
	}
	
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(serverPort);
			while (true) {
				Socket clientSocket = ss.accept();
				dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
				InputStream is = clientSocket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				MF=(MessageFormat)ois.readObject();			
	
				respClient = new ResponseHandlerClient(MF, findFile(), sharedDir, myPeerId);
				respClient.start();
				respClient.join();
			}
		} catch(Exception io) {
			io.printStackTrace();
		}
	}
	
	public boolean findFile() {
		File newfind;
		File directoryObj = new File(sharedDir);
		String[] filesList = directoryObj.list();
		int i = 0;
		newfind = new File(filesList[i]);
		while (!newfind.getName().equals(MF.fileName) && i < filesList.length ){
			newfind = new File(filesList[i]);
			i++;
		}
		if (i <= filesList.length && newfind.getName().equals(MF.fileName)) return true;
		return false;
	}

		
}
	

