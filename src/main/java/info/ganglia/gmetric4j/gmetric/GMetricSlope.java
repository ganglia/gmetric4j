/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package info.ganglia.gmetric4j.gmetric;

/**
 *
 * @author humphrej
 */
public enum GMetricSlope {
    //zero|positive|negative|both|derivative
    ZERO(0),
    POSITIVE(1),
    NEGATIVE(2),
    BOTH(3),
    UNSPECIFIED(4),
    DERIVATIVE(5);
    
    private int gangliaSlope ;
    
    GMetricSlope( int gangliaSlope ) {
        this.gangliaSlope = gangliaSlope ;
    }

    public int getGangliaSlope() {
        return gangliaSlope ;
    }
}
