package info.ganglia.gmetric4j.gmetric;


import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The base implementation for a protocol, does the socket work
 */
public abstract class AbstractProtocol implements Protocol {
    protected InetAddress udpAddr = null ;
    protected int port ;
    protected String group ;
    private DatagramSocket socket ;
    private int ttl;
    private UDPAddressingMode mode ;
    private static Logger log =
        Logger.getLogger(AbstractProtocol.class.getName());

	public AbstractProtocol( String group, int port, UDPAddressingMode mode, int ttl ) {
    	this.group = group ;
    	this.port = port ;
    	this.mode = mode ;
    	this.ttl = ttl;
	}
	/**
	 * Sends the provided byte buffer
	 * @param buf a buffer containing the message
	 * @param len the num of bytes to send from the buffer
	 * @throws Exception
	 */
	protected void send( byte[] buf, int len, boolean closeit) throws Exception {
	    if (udpAddr == null )
	        udpAddr = InetAddress.getByName( group ) ;
	
	    DatagramPacket packet = new DatagramPacket( buf, len,
	                            udpAddr, port) ;
	
	    if ( socket == null ) {
	    	if ( mode == UDPAddressingMode.MULTICAST) {
	            MulticastSocket multicastSocket = new MulticastSocket() ;
	            multicastSocket.setTimeToLive(ttl);
	            socket = multicastSocket ;
	    	} else {
				socket = new DatagramSocket() ;
	    	}
	    }
	    log.log(Level.FINEST,"Sending message of length " + len);
	    socket.send( packet ) ;
	    if ( closeit )
	    	socket.close();
	}


	public abstract void announce(String name, String value,
			GMetricType type, String units, GMetricSlope slope, int tmax,
			int dmax, String groupName) throws Exception ;

}
