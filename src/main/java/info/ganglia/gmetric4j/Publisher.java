package info.ganglia.gmetric4j;

import info.ganglia.gmetric4j.gmetric.GMetricSlope;
import info.ganglia.gmetric4j.gmetric.GMetricType;
import info.ganglia.gmetric4j.gmetric.GangliaException;

public interface Publisher {
	void publish( String processName, String attributeName, 
			String value, GMetricType type, GMetricSlope slope, String units )
				throws GangliaException;
	
	void publish( String processName, String attributeName, 
			String value, GMetricType type, GMetricSlope slope, int delay,
			int lifetime, String units )
				throws GangliaException;
}
