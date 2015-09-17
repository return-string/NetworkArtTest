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

public class MulticastServerThread extends SocketThread {
	private static MulticastSocket sock; // FIXME what is this static declaration. why is it here. get rid of it.
	private boolean keepPlaying = true;

	/** Creates a new server thread around the given socket */
	public MulticastServerThread() {
		try {
			sock = new MulticastSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sock == null) throw new IllegalArgumentException();
	}

	/** MULTICAST VERSION */
	public void run() {
		System.out.println("start s");
		int count = 0;
        while (keepPlaying) {
        	long then = System.currentTimeMillis();
            try {
            	ByteBuffer bb = ByteBuffer.allocate(Main.LARGE_PACKET_SIZE);

                // figure out message...
                String dString = null;
                if (Math.random() < 0.5) { dString = "A"; }
                else { dString = "B"; }
                bb.put(dString.getBytes());
                DatagramPacket packet = new DatagramPacket(dString.getBytes(),dString.length(),
                	sock.getInetAddress(), sock.getLocalPort()); //FIXME not right

                // send your packet out to everyone
                sock.send(packet);
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

	public InetAddress getInetAddress() {
		if(socketIsSafe()) {
			return sock.getInetAddress();
		}
		return null;
	}

	public int getLocalPort() {
		if (socketIsSafe()) {
			return sock.getLocalPort();
		}
		return -1;
	}

	private boolean socketIsSafe() {
		if (sock != null) {
			return !sock.isClosed() && sock.isBound() && sock.isConnected();
		}
		return false;
	}

	@Override
	protected void close() {
		sock.close();
	}
}
