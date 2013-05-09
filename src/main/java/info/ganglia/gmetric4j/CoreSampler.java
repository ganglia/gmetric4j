package info.ganglia.gmetric4j;

import info.ganglia.gmetric4j.gmetric.GMetricSlope;
import info.ganglia.gmetric4j.gmetric.GMetricType;
import info.ganglia.gmetric4j.gmetric.GangliaException;

import java.util.logging.Logger;


public class CoreSampler extends GSampler {
	
	private static Logger log =
        Logger.getLogger(CoreSampler.class.getName());
	
	public CoreSampler() {
		super(0, 30, "core");
	}

	public void run() {
		Publisher gm = getPublisher();
        log.finer("Announcing heartbeat");
        try {
			gm.publish("core", "heartbeat", "0", GMetricType.UINT32, GMetricSlope.ZERO, "");
		} catch (GangliaException e) {
			log.severe("Exception while sending heartbeat");
			e.printStackTrace();
		}

	}

}
