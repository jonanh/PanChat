package panchat.messages;

import panchat.adressing.Multicastdomain;

public class MulticastMessage<T> extends Message<T> {

	private static final long serialVersionUID = 1L;

	Multicastdomain domain;

	public MulticastMessage(T pMessage, Multicastdomain pMulticastDomain) {
		super(pMessage);
		domain = pMulticastDomain;
	}
	
	public Multicastdomain getDomain() {
		return domain;
	}

}
