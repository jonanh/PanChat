package panchat.messages.register;

import java.io.Serializable;

import order.Message;

import panchat.data.User;

public class RegisterUserMsg implements Serializable, Message.Unicast {

	private static final long serialVersionUID = 1L;

	public User user;
	public boolean register;

	public RegisterUserMsg(User pUser, boolean pRegister) {
		this.user = pUser;
		this.register = pRegister;
	}
}
