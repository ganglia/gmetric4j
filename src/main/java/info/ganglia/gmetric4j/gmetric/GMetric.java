package info.ganglia.gmetric4j.gmetric;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

/**
 * Implements the Ganglia gmetric command in java
 */
public class GMetric implements Closeable {
    public enum UDPAddressingMode {
        MULTICAST, UNICAST;

        public static UDPAddressingMode getModeForAddress(String addr) throws IOException {
            InetAddress _addr = InetAddress.getByName(addr);

            if (_addr.isMulticastAddress())
                return MULTICAST;

            return UNICAST;
        }
    };

    private Protocol protocol;

    /**
     * Constructs a GMetric
     * 
     * @param group
     *            the host/group to send the event to
     * @param port
     *            the port to send the event to
     * @param mode
     *            the mode
     * @param ttl
     *            time to live value
     */
    public GMetric(String group, int port, UDPAddressingMode mode, int ttl) throws IOException {
        this(group, port, mode, ttl, null);
    }

    /**
     * Constructs a GMetric
     * 
     * @param group
     *            the host/group to send the event to
     * @param port
     *            the port to send the event to
     * @param mode
     *            the mode
     * @param ttl
     *            time to live value
     * @param nif
     *            networkinterface to use send the event to
     */
    public GMetric(String group, int port, UDPAddressingMode mode, int ttl, String nif) throws IOException {
        this(group, port, mode, ttl, nif, true);
    }

    /**
     * Constructs a GMetric
     * 
     * @param group
     *            the host/group to send the event to
     * @param port
     *            the port to send the event to
     * @param mode
     *            adressing mode to be used (UNICAST/MULTICAST)
     * @param ttl
     *            time to live value
     * @param nif
     *            networkinterface to use send the event to
     * @param ganglia311
     *            protocol version true=v3.1, false=v3.0
     */
    public GMetric(String group, int port, UDPAddressingMode mode, int ttl, String nif, boolean ganglia311) throws IOException {
        this(group, port, mode, ttl, nif, ganglia311, null);
    }

    /**
     * Constructs a GMetric
     * 
     * @param group
     *            the host/group to send the event to
     * @param port
     *            the port to send the event to
     * @param mode
     *            adressing mode to be used (UNICAST/MULTICAST)
     * @param ttl
     *            time to live value
     * @param nif 
     *            networkinterface to use send the event to
     * @param ganglia311
     *            protocol version true=v3.1, false=v3.0
     * @param uuid
     *            uuid for the host
     */
    public GMetric(String group, int port, UDPAddressingMode mode, int ttl, String nif, boolean ganglia311, UUID uuid)
            throws IOException {
        if (!ganglia311)
            this.protocol = new Protocolv30x(group, port, mode, ttl, nif);
        else
            this.protocol = new Protocolv31x(group, port, mode, ttl, nif, 5, uuid, null);
    }

    /**
     * Constructs a GMetric
     * 
     * @param group
     *            the host/group to send the event to
     * @param port
     *            the port to send the event to
     * @param mode
     *            adressing mode to be used (UNICAST/MULTICAST)
     * @param ttl
     *            time to live value
     * @param ganglia311
     *            protocol version true=v3.1, false=v3.0
     * @param uuid
     *            uuid for the host
     * @param spoof
     *            spoofing information IP:hostname
     */
    public GMetric(String group, int port, UDPAddressingMode mode, int ttl, String nif, boolean ganglia311, UUID uuid, String spoof)
            throws IOException {
        if (!ganglia311)
            this.protocol = new Protocolv30x(group, port, mode, ttl, nif);
        else
            this.protocol = new Protocolv31x(group, port, mode, ttl, nif, 5, uuid, spoof);
    }

    /**
     * The Ganglia Metric Client (gmetric) announces a metric
     * 
     * @param name
     *            Name of the metric
     * @param value
     *            Value of the metric
     * @param type
     *            Type of the metric. Either
     *            string|int8|uint8|int16|uint16|int32|uint32|float|double
     * @param units
     *            Unit of measure for the value
     * @param slope
     *            Either zero|positive|negative|both
     * @param tmax
     *            The maximum time in seconds between gmetric calls
     * @param dmax
     *            The lifetime in seconds of this metric
     * @param group
     *            Group Name of the metric
     * @throws java.lang.Exception
     */
    public void announce(String name, String value, GMetricType type, String units, GMetricSlope slope, int tmax,
            int dmax, String group) throws GangliaException {
        try {
            protocol.announce(name, value, type, units, slope, tmax, dmax, group);
        } catch (Exception ex) {
            throw new GangliaException("Exception announcing metric", ex);
        }
    }

    /**
     * Announces a metric
     * 
     * @param name
     *            Name of the metric
     * @param value
     *            Value of the metric
     * @param group
     *            Group Name of the metric
     * @throws ganglia.GangliaException
     */
    public void announce(String name, int value, String group) throws GangliaException {
        this.announce(name, Integer.toString(value), GMetricType.INT32, "", GMetricSlope.BOTH, 60, 0, group);
    }

    /**
     * Announces a metric
     * 
     * @param name
     *            Name of the metric
     * @param value
     *            Value of the metric
     * @param group
     *            Group Name of the metric
     * @throws ganglia.GangliaException
     */
    public void announce(String name, long value, String group) throws GangliaException {
        this.announce(name, Long.toString(value), GMetricType.DOUBLE, "", GMetricSlope.BOTH, 60, 0, group);
    }

    /**
     * Announces a metric
     * 
     * @param name
     *            Name of the metric
     * @param value
     *            Value of the metric
     * @param group
     *            Group Name of the metric
     * @throws ganglia.GangliaException
     */
    public void announce(String name, float value, String group) throws GangliaException {
        this.announce(name, Float.toString(value), GMetricType.FLOAT, "", GMetricSlope.BOTH, 60, 0, group);
    }

    /**
     * Announces a metric
     * 
     * @param name
     *            Name of the metric
     * @param value
     *            Value of the metric
     * @param group
     *            Group Name of the metric
     * @throws ganglia.GangliaException
     */
    public void announce(String name, double value, String group) throws GangliaException {
        this.announce(name, Double.toString(value), GMetricType.DOUBLE, "", GMetricSlope.BOTH, 60, 0, group);
    }

    /**
     * Closes the underlying protocol.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        if (this.protocol != null) {
            this.protocol.close();
        }
    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable {
        // help gc out here a bit to prevent socket leaks
        close();
    }

    /**
     * Main method that sends a test metric
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GMetric gm = null;
        try {
            gm = new GMetric("239.2.11.71", 8649, UDPAddressingMode.MULTICAST, 1);
            // gm.announce("heartbeat", "0", GMetricType.UINT32, "",
            // GMetricSlope.ZERO, 0, 0, "core");
            gm.announce("BOILINGPOINT", "100", GMetricType.STRING, "CELSIUS", GMetricSlope.BOTH, 0, 0, "TESTGROUP");
            gm.announce("INTTEST", (int) Integer.MAX_VALUE, "TESTGROUP");
            gm.announce("LONGTEST", (long) Long.MAX_VALUE, "TESTGROUP");
            gm.announce("FLOATTEST", (float) Float.MAX_VALUE, "TESTGROUP");
            gm.announce("DOUBLETEST", (double) Double.MAX_VALUE, "TESTGROUP");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            gm.close();
        }
    }
}
