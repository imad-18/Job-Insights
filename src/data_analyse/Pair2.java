package data_analyse;

public class Pair2 {
	private Boolean key;
    private String value;

    public Pair2(Boolean key, String value) {
        this.key = key;
        this.value = value;
    }

    public Boolean getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(Boolean key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}