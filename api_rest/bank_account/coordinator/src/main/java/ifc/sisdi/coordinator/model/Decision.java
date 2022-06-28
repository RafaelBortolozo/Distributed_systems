package ifc.sisdi.coordinator.model;

public class Decision {
    private String id;
    private boolean decision;

    public Decision(String id, boolean decision) {
        this.id = id;
        this.decision = decision;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }
}
