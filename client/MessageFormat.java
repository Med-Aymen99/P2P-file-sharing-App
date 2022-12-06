package client;
import java.io.Serializable;

public class MessageFormat implements Serializable {
	public String fileName;
	public int fromPeerId;
	public int fromPeerDownloadPort;
	//int TTL_value;
}
