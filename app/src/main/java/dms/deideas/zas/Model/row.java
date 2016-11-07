package dms.deideas.zas.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dmadmin on 21/07/2016.
 */
public class row implements Serializable {

    private List<Elements> elements;

    public List<Elements> getElements() {
        return elements;
    }

    public void setElements(List<Elements> elements) {
        this.elements = elements;
    }
}
