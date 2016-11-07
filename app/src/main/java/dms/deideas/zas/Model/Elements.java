package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by dmadmin on 21/07/2016.
 */
public class Elements implements Serializable {

    private Distance distance;
    private Duration duration;
    private String status;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
