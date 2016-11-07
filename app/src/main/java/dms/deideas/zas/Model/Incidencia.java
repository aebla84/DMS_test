package dms.deideas.zas.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by bnavarro on 18/07/2016.
 */
public class Incidencia implements Serializable {
    private String problem_type;
    private List<String> problems;

    public Incidencia() {

    }

    public String getTypeofincidencia() {
        return problem_type;
    }

    public void setTypeofincidencia(String problem_type) {
        this.problem_type = problem_type;
    }

    public List<String> getLstdescription() {
        return problems;
    }

    public void setLstdescription(List<String> lstdescription) {
        this.problems = lstdescription;
    }

}
