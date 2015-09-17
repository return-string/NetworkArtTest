package control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import main.Main;

public class SingleClientThread extends SocketThread {
	private DatagramSocket sock;
	private MulticastSocket clientSock;

	public SingleClientThread(DatagramSocket s) {
        if (s==null) {
        	throw new IllegalArgumentException();
        }
		this.sock = s;
	}

	public void run() {
		System.out.println("start c");
    	ByteBuffer bb = ByteBuffer.allocate(Main.LARGE_PACKET_SIZE);
        System.out.println("send empty packet");
        // create yourself a shiny new packet (your socket should already be connected to where you need to go)
	    while (true) {
	        DatagramPacket packet = new DatagramPacket(bb.array(), bb.capacity(), sock.getInetAddress(), sock.getPort());
	        try {
				sock.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // get response
	        packet = new DatagramPacket(bb.array(), bb.capacity());
	        try {
				sock.receive(packet);
		        // parse response
		        String serverIP = "";
		        int serverPort = bb.getInt();

		        for (int i = 0; i < bb.capacity(); i++) {
		        	serverIP = serverIP + bb.getChar();
		        }

		        System.out.println("ip "+serverIP+", port "+serverPort);

		        if (serverPort >= 0) {
		        	break;
		        }

			    // display response
		        String received = new String(packet.getData(), 0, packet.getLength());
		        System.out.println("Quote of the Moment: " + received);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
        sock.close();
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
		return sock.getInetAddress();
	}

	@Override
	public int getLocalPort() {
		return sock.getLocalPort();
	}

	@Override
	public boolean isSocketSafe() {
		return sock != null && !sock.isClosed();
	}

	@Override
	protected void close() {
		sock.close();
	}
}