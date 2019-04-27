package Main;

public class SuperSetRow {
    private String set;
    private String sup;
    private String conf;

    public SuperSetRow(String set, String sup, String conf) {
        this.set = set;
        this.sup = sup;
        this.conf = conf;
    }

    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public String getSup() {
        return sup;
    }

    public void setSup(String sup) {
        this.sup = sup;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}