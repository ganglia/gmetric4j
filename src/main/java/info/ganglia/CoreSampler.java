package info.ganglia;

import info.ganglia.gmetric.GMetricSlope;
import info.ganglia.gmetric.GMetricType;
import info.ganglia.gmetric.GangliaException;

import java.util.logging.Logger;


public class CoreSampler extends GSampler {
	
	private static Logger log =
        Logger.getLogger(CoreSampler.class.getName());
	
	public CoreSampler() {
		super(0, 30, "core");
	}

	@Override
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
