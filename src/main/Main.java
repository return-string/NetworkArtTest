package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import java.net.SocketException;

public class Main {
	public static final String GAME_NAME = "Wherefore Art Thou";
	public static final int WAIT_PERIOD = 20;
	public static final int BROADCAST_PERIOD = 2000; // cranked up to obscene levels to make output easier to read
	public static final int MAX_DATAGRAM_LEN = 65507;
	public static final String DEFAULT_IP = "224.0.113.0"; // arbitrary; anything from 224.0.0.0 to 239.255.255.255 but not 224.*0 itself.

	public static void main (String[] args) {
		String filename = null;
		boolean server = false;
		int maxClients = 0;
		String serverURL = null;
		int gameClock = WAIT_PERIOD;
		int broadcastClock = BROADCAST_PERIOD;
		int port = 32768; // default

		for (int i = 0; i != args.length; ++i) {
			if (args[i].startsWith("-")) {
				String arg = args[i];
				if(arg.equals("-help")) {
					System.out.println("help text pending");
					System.exit(0);
				} else if(arg.equals("-server")) {
					server = true;
					maxClients = Integer.parseInt(args[++i]);
				} else if(arg.equals("-connect")) {
					serverURL = args[++i];
				} else if(arg.equals("-clock")) {
					gameClock = Integer.parseInt(args[++i]);
				} else if(arg.equals("-port")) {
					port = Integer.parseInt(args[++i]); // NOT YOUR PORT, THEIR PORT.
				}
			} else {
				filename = args[i];
			}
		}

		MulticastSocket s = null;
		while (s==null) {
			try {
				s = new MulticastSocket(port);
			} catch (SocketException e) {
				// eat this and try again
				System.err.println("err");
				port++;
				s.close();
				s = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.err.println("err");
				port++;
				s.close();
				s = null;
			}
		}

		Thread t = null;
		if (server) {
			System.out.println("Starting "+ GAME_NAME +" Server | "+port);
			t = new control.MulticastServerThread(s);
		} else {
			System.out.println("Starting "+ GAME_NAME +" Client | "+port);
			t = new control.MulticastClientThread(s);
		}
		t.start();
	}
}
