 package ganglia.gmetric;

import ganglia.gmetric.GMetric.UDPAddressingMode;
import ganglia.xdr.v30x.Ganglia_gmetric_message;
import ganglia.xdr.v30x.Ganglia_message;
import ganglia.xdr.v30x.Ganglia_message_formats;

import org.acplt.oncrpc.XdrBufferEncodingStream;

public class Protocolv30x extends AbstractProtocol {

	private static final int MAX_BUFFER_SIZE = 1024 ;
    private XdrBufferEncodingStream xdr = new XdrBufferEncodingStream( MAX_BUFFER_SIZE );
    
    public Protocolv30x( String group, int port, UDPAddressingMode mode, int ttl ) {
    	super(group, port, mode, ttl);
    }
    
	@Override
	public void announce( String name, String value,
			GMetricType type, String units, GMetricSlope slope, int tmax,
			int dmax, String groupName) throws Exception {
        encodeGMetric( name, value, type, units, slope, tmax, dmax );
        send( xdr.getXdrData(), xdr.getXdrLength() , true);
	}
	
	/**
     * Encodes the metric using the classes generated by remotetea
     */
    private void encodeGMetric( String name, 
            String value, 
            GMetricType type,
            String units,
            GMetricSlope slope,
            int tmax,
            int dmax )
            throws Exception {
        Ganglia_message msg = new Ganglia_message() ;
        Ganglia_gmetric_message gmetric_msg = new Ganglia_gmetric_message() ;
        
        msg.id = Ganglia_message_formats.metric_user_defined ;
        msg.gmetric = gmetric_msg ;
        gmetric_msg.name = name ;
        gmetric_msg.value = value ;
        gmetric_msg.type = type.getGangliaType() ;
        gmetric_msg.units = units ;
        gmetric_msg.slope = slope.getGangliaSlope() ;
        gmetric_msg.tmax = tmax ;
        gmetric_msg.dmax = dmax ;
        
        xdr.beginEncoding(udpAddr, port) ;
        msg.xdrEncode(xdr);
        xdr.endEncoding();
    }

}