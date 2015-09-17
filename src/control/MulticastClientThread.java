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

public class MulticastClientThread extends SocketThread {
	private MulticastSocket sock;
	private boolean listening = true;
	private InetAddress group;

	public MulticastClientThread(int port) {
        if (port < 0) { throw new IllegalArgumentException(); }
        try {
			sock = new MulticastSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		System.out.println("start c");
		// we're part of the group! let's get to the business of game data exchange
		while (listening) {
	        ByteBuffer bb = ByteBuffer.allocate(Main.LARGE_PACKET_SIZE);
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


		        //TODO YOU WERE HERE


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

	@Override
	public InetAddress getInetAddress() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public int getLocalPort() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	public boolean isSocketSafe() {
		throw new UnsupportedOperationException(); // TODO
	}

	@Override
	protected void close() {
		throw new UnsupportedOperationException(); // TODO

	}
}