package info.ganglia.gmetric4j.gmetric;

import info.ganglia.gmetric4j.Publisher;

public class GMetricPublisher implements Publisher {

	private GMetric gm = null ;
	public GMetricPublisher( GMetric gm ) {
		this.gm = gm ;
	}

	public void publish(String processName, String attributeName, String value,
			GMetricType type, GMetricSlope slope, String units) throws GangliaException {
        publish(processName, attributeName, value, type, slope, 60, 0, units);
	}

	@Override
	public void publish(String processName, String attributeName, String value,
			GMetricType type, GMetricSlope slope, int delay, int lifetime, String units)
			throws GangliaException {
        gm.announce(attributeName, value, type, units, 
        		slope, delay, lifetime, processName);
	}

}
