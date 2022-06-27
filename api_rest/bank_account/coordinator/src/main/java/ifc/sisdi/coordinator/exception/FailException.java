package ifc.sisdi.coordinator.exception;

@SuppressWarnings("serial")
public class FailException extends RuntimeException {
    public FailException () {
        super("fail no 2PC");
    }
}
