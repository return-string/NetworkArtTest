package control;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import main.Main;

public class SingleClientThread extends Thread {
	private DatagramSocket sock;

	public SingleClientThread(DatagramSocket s) {
        if (s==null) {
        	throw new IllegalArgumentException();
        }
		this.sock = s;
	}

	public void run() {
		System.out.println("start c");
        // send request
        byte[] buf = new byte[256];
        // where do we send?
        DatagramPacket packet = new DatagramPacket(buf, buf.length, sock.getInetAddress(), sock.getPort());
	    //while (true) {
	    	if (packet != null) {
		        try {
					sock.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		            // get response
		        packet = new DatagramPacket(buf, buf.length);
		        try {
					sock.receive(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			    // display response
		        String received = new String(packet.getData(), 0, packet.getLength());
		        System.out.println("Quote of the Moment: " + received);
	        }
	    //}
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
}