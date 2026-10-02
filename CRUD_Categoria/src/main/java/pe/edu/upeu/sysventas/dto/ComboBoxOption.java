package pe.edu.upeu.sysventas.dto;


public class ComboBoxOption {
    private String key;
    private String value;

    @Override
    public String toString() {
        return value;
    }

    public ComboBoxOption() {}
    public ComboBoxOption(String key, String value) {
        this.key = key;
        this.value = value;
    }
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
