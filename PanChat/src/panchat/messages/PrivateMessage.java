package panchat.messages;

import panchat.adressing.Address;

public class PrivateMessage<T> extends Message<T> {

	private static final long serialVersionUID = 1L;

	private Address address;

	public PrivateMessage(T pMessage, Address pAddress) {
		super(pMessage);
		this.address = pAddress;
	}

	public Address getAddress() {
		return address;
	}

}
