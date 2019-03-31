import java.io.IOException;

import com.bittorrent.main.*;

public class PeerProcess {

	private static boolean simulate = true;

	public static void main (String args[]) throws IOException, InterruptedException {
		String[] peers = new String[] {"1001", "1002"};
		PeerProcessExecutor peerProcessExecutor = null;
		if (simulate) {
			for (String peerId: peers) {
				Thread t = new Thread(new PeerProcessExecutor(peerId));
				t.start();
			}
		}
		else {
			if (args.length > 0) {
				peerProcessExecutor = new PeerProcessExecutor(args[0]);
			} else {
				//default value
				peerProcessExecutor = new PeerProcessExecutor("1001");
			}
			peerProcessExecutor.init();
		}
	}
}
