package main;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import control.SingleClientThread;
import control.SingleServerThread;

public class Main {
	public static final String GAME_NAME = "Wherefore Art Thou";
	public static final int WAIT_PERIOD = 20;
	public static final int BROADCAST_PERIOD = 2000; // cranked up to obscene levels to make output easier to read
	public static final int MAX_DATAGRAM_LEN = 65507;
	public static final String SERVER_DEF_PUBLIC_IP = "224.0.113.0"; // arbitrary; anything from 224.0.0.0 to 239.255.255.255 but not 224.*0 itself.
	public static final String SERVER_DEF_PRIVATE_IP = "224.1.113.0";
	//public static final String CLIENT_DEF_PUBLIC_IP = "225.0.113.0";
	public static final String CLIENT_DEF_PRIVATE_IP = "225.1.113.0";
	public static final int SERVER_DEF_PUBLIC_PORT = 32768;
	public static final int SERVER_DEF_PRIVATE_PORT = 37777;
	public static final int CLIENT_DEF_PUBLIC_PORT = 32769;
	public static final int CLIENT_DEF_PRIVATE_PORT = 39999;
	
	public static final int LARGE_PACKET_SIZE = 1024;

	public static void main (String[] args) {
		// mostly stolen from Dave's PacMan code.
		String filename = null;
		boolean server = false;
		int maxClients = 0;
		String serverURL = null;
		int gameClock = WAIT_PERIOD;
		int broadcastClock = BROADCAST_PERIOD;
		int publicServerPort = 32768; // default

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
					publicServerPort = Integer.parseInt(args[++i]);
				}
			} else {
				filename = args[i];
			}
		}

		// Sanity checks, also stolen directly from Dave's PacMan code
		if(serverURL != null && server) {
			System.out.println("Cannot be a server and connect to another server!");
			System.exit(1);
		} else if(serverURL != null && gameClock != WAIT_PERIOD) {
			System.out.println("Cannot overide clock period when connecting to server.");
			System.exit(1);
		}
//		we'll need this later but not right now
//		else if(serverURL == null && filename == null) {
//			System.out.println("Board file must be provided for single user, or server mode.");
//			System.exit(1);
//		}

		// public connection stuff begins

		if (server) {
			runPublicSocket(publicServerPort); // TODO will need parameters for game file
		} else {
			DatagramSocket sock = null;
			try {
				sock = new DatagramSocket(CLIENT_DEF_PUBLIC_PORT);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			Thread publicClient = new SingleClientThread(sock);
			publicClient.start();
			while (!sock.isClosed()) {
				
			}
		}

		// public connection stuff ends
	}

	private static void runPublicSocket(int port) {
		DatagramSocket sock = null;
		try {
			sock = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		Thread t = new SingleServerThread(sock);
		t.start();
	}
}
