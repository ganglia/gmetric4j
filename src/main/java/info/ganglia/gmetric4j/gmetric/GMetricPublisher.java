package info.ganglia.gmetric4j.gmetric;

import info.ganglia.gmetric4j.Publisher;

public class GMetricPublisher implements Publisher {

	private GMetric gm = null ;
	public GMetricPublisher( GMetric gm ) {
		this.gm = gm ;
	}

	public void publish(String processName, String attributeName, String value,
			GMetricType type, GMetricSlope slope, String units) throws GangliaException {
        gm.announce(attributeName, value, type, units, 
        		slope, 60, 0, processName);
	}

	@Override
	public void publish(String processName, String attributeName, String value,
			GMetricType type, GMetricSlope slope, int delay, String units)
			throws GangliaException {
        gm.announce(attributeName, value, type, units, 
        		slope, delay, 0, processName);	
	}

}
