package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.Date;
import java.util.Arrays;

import main.Main;
/** The SingleServerThread keeps a port open to listen for new
 * connection requests to the server. */

public class SingleServerThread extends SocketThread {
	private static ServerSocket sock; // our public socket
	private static MulticastServerThread listen; // the private socket, for connected clients
	private volatile Socket[] clients = null;
	private final int maxClients;
	private final int myport;
	private boolean keepPlaying = true;
	private boolean keepConnecting = true;

	/** Creates a new server thread around the given port. */
	public SingleServerThread(int port, int maxClients) {
		myport = port;
		try {
			sock = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (maxClients <= 0) {
			throw new IllegalArgumentException();
		}
		this.maxClients = maxClients;
	}

	public void run() {
		System.out.println("started public socket on port "+ sock.getLocalPort());
		System.out.println("=============================================");
        while (keepConnecting) {
            try {
            	if (sock.isClosed()) {
            		sock = new ServerSocket(myport);
            	}
            	ByteBuffer bb = ByteBuffer.allocate(Main.LARGE_PACKET_SIZE);
                Socket tcpSocket = sock.accept();

                // your message does not actually matter to us! we just care that you've connected.

                // hooray! we have someone on the other end. let's set them up on the broadcast port.
                // TODO separate all this into a method with appropriate error checking
                if (listen == null) {
                	listen = new MulticastServerThread();
                }
                // add the server's information to the return packet
                bb.putInt(listen.getLocalPort());

                // send new port to client
                tcpSocket.getOutputStream().write(bb.array());
                tcpSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            	keepConnecting = false;
            }
        }
        System.out.println("end!");
        try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.err.println("closed");
	}

	/** An exact copy of the current connections, with no
	 * claims made about whether any are closed or null.
	 * @return
	 */
	private Socket[] getCurrentConnections() {
		return Arrays.copyOf(clients, clients.length);
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
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
