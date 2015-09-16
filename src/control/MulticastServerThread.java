package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Date;

import main.Main;
/** A ServerThread is created around an already set-up port number.
 * (Later it'll handle that itself but for now we'll assume
 * that the socket is everything it needs to be.) */

public class MulticastServerThread extends Thread {
	private static MulticastSocket sock; // FIXME what is this static declaration. why is it here. get rid of it.
	private boolean keepPlaying = true;

	/** Creates a new server thread around the given socket */
	public MulticastServerThread(MulticastSocket ms) {
		if (ms==null) throw new IllegalArgumentException();
		// then set up the group for this socket
		MulticastServerThread.sock = ms;
	}

	/** MULTICAST VERSION */
	public void run() {
		System.out.println("start s");
		int count = 0;
        while (keepPlaying) {
        	long then = System.currentTimeMillis();
            try {
            	// create a buffer and a packet. we'll reuse these.
                byte[] buf = new byte[256];

                // figure out message...
                String dString = null;
                if (Math.random() < 0.5)
                    dString = "A";
                else
                    dString = "B";
                buf = dString.getBytes();
                DatagramPacket packet = new DatagramPacket(dString.getBytes(),dString.length(),
                	sock.getInetAddress(), sock.getLocalPort()); //FIXME not right
                sock.send(packet);
            	System.out.println(count +": "+ Byte.valueOf(buf[0]));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("end!");
            	keepPlaying = false;
            }

            // now wait until we have to send the next round of data
            long x = (System.currentTimeMillis() - then);
            while (x < Main.BROADCAST_PERIOD) {
            	x = System.currentTimeMillis() - then;
            }
            count++;
        }
        try {
			sock.leaveGroup(sock.getInetAddress());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sock.close();
        System.err.println("closed");
	}

	/** Returns the address of the multicast socket. */
	public InetAddress getInetAddress() {
		if(socketIsSafe()) {
			return sock.getInetAddress();
		}
		return null;
	}

	/** Returns the port to which clients should connect to listen to the socket. */
	public int getLocalPort() {
		if (socketIsSafe()) {
			return sock.getLocalPort();
		}
		return -1;
	}

	/** Helper method that makes sure the socket is set up correctly before use. */
	private boolean socketIsSafe() {
		if (sock != null) {
			return !sock.isClosed() && sock.isBound() && sock.isConnected();
		}
		return false;
	}
}
