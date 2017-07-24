package Java.Connection;

import java.io.Serializable;

public class Message implements Serializable {
    private MessageType type;
    private Object additional;
    private Object data;

    public Message(MessageType type, Object data) {
        this.type = type;
        this.data = data;
    }

    public void setAdditional(Object additional) {
        this.additional = additional;
    }

    public MessageType getType() {
        return type;
    }

    public Object getAdditional() {
        return additional;
    }

    public Object getData() {
        return data;
    }
}
