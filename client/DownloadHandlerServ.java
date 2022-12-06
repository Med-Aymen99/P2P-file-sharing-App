package client;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

// This a Download Server : has a downloadPort
public class DownloadHandlerServ extends Thread {
		public String sharedDir;
		private String fileName;
		private int downloadport;
		private static FileOutputStream fileOutputStream = null;
		private static DataInputStream dataInputStream = null; 
		private static boolean searchCompleted ;
		
		public DownloadHandlerServ(int downloadport, String sharedDir, String fileName) {
			super();
			this.sharedDir = sharedDir;
			this.downloadport = downloadport;
			this.fileName = fileName;
			

		}

		@Override
		public void run () {
			try {
				searchCompleted = false;
				ServerSocket ss = new ServerSocket(downloadport);
				while (searchCompleted == false) {
					
					Socket clientSocket = ss.accept();
			
					dataInputStream = new DataInputStream(clientSocket.getInputStream());
					boolean status = dataInputStream.readBoolean();
					int uploaderPeerId = dataInputStream.readInt();
					if (status) {
						receiveFile();
						System.out.println(fileName + " est telecharge avec succées par le peer " + uploaderPeerId);
					} else {
						System.out.println(fileName + " est  introuvable dans le peer " + uploaderPeerId);
					}
					searchCompleted = dataInputStream.readBoolean();
		            dataInputStream.close();
				}
				ss.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		private void receiveFile() throws Exception
	    {
	        int bytes = 0;
	        File receivedFile = new File(sharedDir+"/"+fileName);
	        receivedFile.createNewFile();
	        FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
	 
	        long size = dataInputStream.readLong(); // read file size
	        byte[] buffer = new byte[4 * 1024];
	        while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
	
	            fileOutputStream.write(buffer, 0, bytes);
	            size -= bytes;
	        }
	        fileOutputStream.close();
	    }
}
