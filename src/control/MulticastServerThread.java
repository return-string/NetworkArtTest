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
/** A ServerThread is created to handle incoming data from an unused port number.
 * 	If the port number is in use, the object creation fails.
 *
 * 1. sets up socket in port
 * 2. while running
 * 		listens to socket */
public class MulticastServerThread extends Thread {
	private static MulticastSocket sock;
	private InetAddress group;
	private boolean keepPlaying = true;

	/** Creates a new server thread around the given socket */
	public MulticastServerThread(MulticastSocket ms) {
		if (ms==null) throw new IllegalArgumentException();
		// then set up the group for this socket
        try {
			group = InetAddress.getByName(Main.DEFAULT_IP);
		} catch (UnknownHostException e) { e.printStackTrace(); }
		this.sock = ms;
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
                	group, sock.getLocalPort()); //FIXME not right
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
			sock.leaveGroup(group);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        sock.close();
        System.err.println("closed");
	}
}
