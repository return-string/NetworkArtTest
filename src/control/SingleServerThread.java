package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Date;

import main.Main;
/** The SingleServerThread keeps a port open to listen for new
 * connection requests to the server. */

public class SingleServerThread extends Thread {
	private static DatagramSocket sock; // our public socket
	private static MulticastServerThread listen; // the private socket, for connected clients
	private boolean keepPlaying = true;

	/** Creates a new server thread around the given socket */
	public SingleServerThread(DatagramSocket s) {
		if (s==null) {
			throw new IllegalArgumentException();
		}
		sock = s;
	}

	public void run() {
		System.out.println("start public socket on port"+ sock.getLocalPort());
        while (keepPlaying) {
            try {
            	ByteBuffer bb = ByteBuffer.allocate(1024);
                // receive client request
                DatagramPacket packet = new DatagramPacket(bb.array(), bb.capacity());
                sock.receive(packet);

                // if someone's connecting to this port, let's direct them to the permanent port
                // TODO separate all this into a method with appropriate error checking
                // set up the thread if necessary
                if (listen==null) {
                	listen = new MulticastServerThread(new MulticastSocket());
                }
                InetAddress addr = listen.getInetAddress();
                int port = listen.getLocalPort();
                System.out.println(addr.getHostAddress()+", "+port);
                System.out.println("length of ip array = "+addr.getAddress().length);
                // add the server's information to the return packet
                bb.putInt(port);
                bb.put(addr.getAddress());

                // send address and port to client
                System.out.println("packet address: "+packet.getAddress()+", port: "+packet.getPort());
                packet = new DatagramPacket(bb.array(), bb.capacity(), packet.getAddress(), packet.getPort());
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
