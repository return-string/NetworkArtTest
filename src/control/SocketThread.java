package control;

import java.net.InetAddress;

abstract class SocketThread extends Thread {

	/** Returns the address of the socket. */
	public abstract InetAddress getInetAddress();

	/** Returns the port to which clients should connect to listen to the socket. */
	public abstract int getLocalPort();

	/** Helper method that makes sure the socket is set up correctly before use. */
	public abstract boolean isSocketSafe();

	/** Closes the thread's socket. Should only be called before discarding the thread. */
	protected abstract void close();
}
