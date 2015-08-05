package info.ganglia.gmetric4j.gmetric;


import info.ganglia.gmetric4j.gmetric.GMetric.UDPAddressingMode;

import java.io.IOException;
import java.net.*;
// import java.util.logging.Level;
// import java.util.logging.Logger;


/**
 * The base implementation for a protocol, does the socket work
 */
public abstract class AbstractProtocol implements Protocol {
    protected InetAddress udpAddr = null ;
    protected int port ;
    protected String group ;
    private DatagramSocket socket ;
    //private static Logger log =
    //    Logger.getLogger(AbstractProtocol.class.getName());

	public AbstractProtocol( String group, int port, UDPAddressingMode mode, int ttl, 
							 String nif ) throws IOException {
    	this.group = group ;
    	this.port = port ;
        this.udpAddr = InetAddress.getByName( group ) ;
        
        if ( mode == UDPAddressingMode.MULTICAST ) {
            MulticastSocket multicastSocket = new MulticastSocket() ;
            multicastSocket.setTimeToLive(ttl);
			if ( nif != null && !nif.isEmpty()) {
				multicastSocket.setNetworkInterface(NetworkInterface.getByName(nif));
			}
            this.socket = multicastSocket ;
        } else {
            this.socket = new DatagramSocket() ;
        }
	}
	
	/**
	 * Closes the underlying socket to prevent socket leaks.
	 */
	public void close() throws IOException {
	    if (this.socket != null) {
	        this.socket.close();
	    }
	}
	
	/**
	 * Sends the provided byte buffer
	 * @param buf a buffer containing the message
	 * @param len the num of bytes to send from the buffer
	 * @throws Exception
	 */
	protected void send( byte[] buf, int len) throws Exception {
	    DatagramPacket packet = new DatagramPacket( buf, len, udpAddr, port) ;
	
	    //log.log(Level.FINEST,"Sending message of length " + len);
	    
	    socket.send( packet ) ;
	    
	}


	public abstract void announce(String name, String value,
			GMetricType type, String units, GMetricSlope slope, int tmax,
			int dmax, String groupName) throws Exception ;

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        // help gc out here a bit to prevent socket leaks
        close();
    }
}
