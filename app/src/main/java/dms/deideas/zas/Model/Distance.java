package dms.deideas.zas.Model;

import java.io.Serializable;

/**
 * Created by dmadmin on 21/07/2016.
 */
public class Distance implements Serializable {

    private String text;
    private int value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
