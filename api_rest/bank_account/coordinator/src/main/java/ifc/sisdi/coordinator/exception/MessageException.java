package ifc.sisdi.coordinator.exception;

@SuppressWarnings("serial")
public class MessageException extends RuntimeException{
    public MessageException(String message){
        super("MessageException: " + message);
    }
}
