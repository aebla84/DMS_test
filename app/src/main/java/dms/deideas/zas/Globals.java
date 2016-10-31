package dms.deideas.zas;

/**
 * Created by dmadmin on 13/06/2016.
 */
public class Globals {
    private static Globals instance;

    // Global variable
    private int serviceCode;
    private int screenCode;
    private int idFragment;
    private int idFragmentDetail;
    private int idFragmentHistorical;

    // Restrict the constructor from being instantiated
    private Globals() {
    }

    public static synchronized Globals getInstance() {
        if (instance == null) {
            instance = new Globals();
        }
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    public int getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(int serviceCode) {
        this.serviceCode = serviceCode;
    }

    public int getScreenCode() {
        return screenCode;
    }

    public void setScreenCode(int screenCode) {
        this.screenCode = screenCode;
    }

    public int getIdFragment() {
        return idFragment;
    }

    public void setIdFragment(int idFragment) {
        this.idFragment = idFragment;
    }

    public int getIdFragmentDetail() {
        return idFragmentDetail;
    }

    public void setIdFragmentDetail(int idFragmentDetail) {
        this.idFragmentDetail = idFragmentDetail;
    }

    public int getIdFragmentHistorical() {
        return idFragmentHistorical;
    }

    public void setIdFragmentHistorical(int idFragmentHistorical) {
        this.idFragmentHistorical = idFragmentHistorical;
    }
}
