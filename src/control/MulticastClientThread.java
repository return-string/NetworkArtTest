package control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import main.Main;

public class MulticastClientThread extends Thread {
	private MulticastSocket sock;
	private boolean listening = true;
	private InetAddress group;

	public MulticastClientThread(MulticastSocket ms) {
        if (ms==null)  throw new IllegalArgumentException();
        // then set up the group for this socket
        try {
			group = InetAddress.getByName(Main.DEFAULT_IP); // FIXME arbitrary IP suggested by oracle
		} catch (UnknownHostException e) { e.printStackTrace(); }
		this.sock = ms;
	}

	public void run() {
		System.out.println("start c");
		// first, join the party: we have to tell the server we'd like to connect


		InetAddress group = null;
		try {
			group = InetAddress.getByName(Main.DEFAULT_IP); // FIXME
			sock.joinGroup(group);
		} catch (UnknownHostException e2) { e2.printStackTrace();
		} catch (IOException e1) { e1.printStackTrace(); }












		while (listening) {
	        // send request
	        byte[] buf = new byte[256];
	        // where do we send?
		    for (int i = 0; i < 6; i++) {
		        DatagramPacket packet = new DatagramPacket(buf, buf.length);
		        try {
					sock.receive(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					listening = false;
				}

			    // display response
		        String received = new String(packet.getData(), 0, packet.getLength());
		        System.out.println("Quote of the Moment: " + received);
		    }
		}
	    try {
			sock.leaveGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sock.close(); // FIXME is this good design? No it is not!
        System.err.println("closed");
    }

	public void methodA() {
		System.out.println("A");
	}

	public void methodB() {
		System.out.println("B");
	}

	public void writeData(byte[] bs) {
		if (bs[0] == Byte.valueOf("A")) {
			System.out.println("Value of 0 is A");
		} else if (bs[0] == Byte.valueOf("B")) {
			System.out.println("Value of 0 is B");
		} else {
			System.out.println("Value of 0 is not A or B");
		}
	}
}