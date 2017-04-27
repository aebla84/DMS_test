package dms.deideas.zas.Model;

import java.io.Serializable;

public class WebConfigurator implements Serializable {
    private String maxOrdersAccepted;
    private String maxOrdersVisible;
    private String maxTime;
    private String maxOrdersAccepted_low;
    private String maxOrdersAccepted_mid;
    private String maxOrdersAccepted_top;

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

    public String getMaxOrdersTypeLow() {
        return maxOrdersAccepted_low;
    }

    public void setMaxOrdersTypeLow(String maxOrdersTypeLow) {
        this.maxOrdersAccepted_low = maxOrdersTypeLow;
    }

    public String getMaxOrdersTypeMid() {
        return maxOrdersAccepted_mid;
    }

    public void setMaxOrdersTypeMid(String maxOrdersTypeMid) {
        this.maxOrdersAccepted_mid = maxOrdersTypeMid;
    }

    public String getMaxOrdersTypeTop() {
        return maxOrdersAccepted_top;
    }

    public void setMaxOrdersTypeTop(String maxOrdersTypeTop) {
        this.maxOrdersAccepted_top = maxOrdersTypeTop;
    }

    @Override
    public String toString() {
        return "WebConfigurator{" +
                "maxOrdersAccepted='" + maxOrdersAccepted + '\'' +
                ", maxOrdersVisible='" + maxOrdersVisible + '\'' +
                ", maxTime='" + maxTime + '\'' +
                ", maxOrdersTypeLow='" + maxOrdersAccepted_low + '\'' +
                ", maxOrdersTypeMid='" + maxOrdersAccepted_mid + '\'' +
                ", maxOrdersTypeTop='" + maxOrdersAccepted_top + '\'' +
                '}';
    }
}
