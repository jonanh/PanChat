package panchat.messages.register;

import java.io.Serializable;
import java.util.UUID;

import panchat.data.User;

/**
 * Mensaje que representa
 * 
 */
public class UserMessage implements Serializable {

	private static final long serialVersionUID = 1L;

	public String ip;
	public int port;
	public String nickName;
	public boolean register;
	public UUID uuid;

	public UserMessage(User user, boolean pRegister) {
		this.ip = user.ip;
		this.port = user.port;
		this.nickName = user.nickName;
		this.uuid = user.uuid;
		this.register = pRegister;
	}

	/**
	 * @return Devuelve un usuario a partir del mensaje.
	 */
	public User user() {
		User user = new User(ip, port, nickName, uuid);
		return user;
	}
}
