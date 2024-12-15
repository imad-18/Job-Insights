package data_analyse;

public class PairData {
	private String key;
    private Float value;

    public PairData(String key, Float value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Float getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(Float value) {
        this.value = value;
    }
}
