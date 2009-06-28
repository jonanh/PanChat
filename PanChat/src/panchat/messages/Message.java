package panchat.messages;

import java.io.Serializable;

public abstract class Message<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private T content;

	public Message(T pMessage) {
		this.content = pMessage;
	}

	public T getContent() {
		return content;
	}
}
