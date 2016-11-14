package dms.deideas.zas.Model;

import java.io.Serializable;

public class WebConfigurator implements Serializable {
    private String maxOrdersAccepted;
    private String maxOrdersVisible;
    private String maxTime;

    public String getMaxOrdersAccepted() {
        return maxOrdersAccepted;
    }

    public void setMaxOrdersAccepted(String maxOrdersAccepted) {
        this.maxOrdersAccepted = maxOrdersAccepted;
    }

    public String getMaxOrdersVisible() {
        return maxOrdersVisible;
    }

    public void setMaxOrdersVisible(String maxOrdersVisible) {
        this.maxOrdersVisible = maxOrdersVisible;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public String toString() {
        return "WebConfigurator{" +
                "maxOrdersAccepted='" + maxOrdersAccepted + '\'' +
                ", maxOrdersVisible='" + maxOrdersVisible + '\'' +
                ", maxTime='" + maxTime + '\'' +
                '}';
    }
}
