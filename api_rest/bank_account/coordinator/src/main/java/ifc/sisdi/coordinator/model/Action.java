package ifc.sisdi.coordinator.model;

public class Action {
    private String id;
    private String operation;
    private int account;
    private double value;

    public Action(String id, String operation, int account, double value) {
        this.id = id;
        this.operation = operation;
        this.account = account;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
