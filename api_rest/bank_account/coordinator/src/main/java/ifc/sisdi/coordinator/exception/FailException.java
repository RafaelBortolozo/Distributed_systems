package ifc.sisdi.coordinator.exception;

@SuppressWarnings("serial")
public class FailException extends RuntimeException {
    public FailException () {
        super("2PC Coordinator error");
    }
}
