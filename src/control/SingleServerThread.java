package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
public class SingleServerThread extends Thread {
	// old
	private static DatagramSocket sock; // in/out slot OBSOLETE
	// endold

	private InetAddress group;
	private BufferedReader in; // source of our data
	private boolean keepPlaying = true;

	/** Creates a new server thread around the given socket */
	public SingleServerThread(DatagramSocket s) {
		if (s==null) {
			throw new IllegalArgumentException();
		}
		sock = s;
	}

	public void run() {
		System.out.println("start s");
        while (keepPlaying) {
            try {
                byte[] buf = new byte[256];
                // receive request
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                sock.receive(packet);

                // figure out response
                String dString = null;
                if (Math.random() < 0.5)
                    dString = "A";
                else
                    dString = "B";
                buf = dString.getBytes();

                // send response to client
                packet = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());
                sock.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            	keepPlaying = false;
            }
            System.out.println("end!");
        }
        sock.close();
        System.err.println("closed");
	}
}
