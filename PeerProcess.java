import java.io.IOException;

import com.bittorrent.main.*;

public class PeerProcess {

	public static void main (String args[]) throws IOException {
		PeerProcessExecutor peerProcessExecutor = null;
		if (args.length > 0) {
			peerProcessExecutor = new PeerProcessExecutor(args[0]);
		}
		else {
			//default value
			peerProcessExecutor = new PeerProcessExecutor("1001");
		}
		peerProcessExecutor.init();
	}
}
