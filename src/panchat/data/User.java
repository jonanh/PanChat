package panchat.data;

import java.io.Serializable;
import java.util.UUID;

import panchat.messages.register.UserMessage;

public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = 1L;

	public UUID uuid;
	public transient String ip;
	public transient int port;
	public transient String nickName;

	public User(String nickName) {
		this.ip = null;
		this.port = 0;
		this.nickName = nickName;
		this.uuid = UUID.randomUUID();
	}

	public User(String ip, int port, String nickName) {
		this.ip = ip;
		this.port = port;
		this.nickName = nickName;
		this.uuid = UUID.randomUUID();
	}

	public User(String ip, int port, String nickName, UUID uuid) {
		this.ip = ip;
		this.port = port;
		this.nickName = nickName;
		this.uuid = uuid;
	}

	@Override
	public int compareTo(User o) {
		return uuid.compareTo(o.uuid);
	}

	@Override
	public String toString() {
		return nickName;
	}

	public String toStringComplete() {
		return "[nick: " + nickName + ", Address : " + ip + ", Port : " + port
				+ ", UUID :" + uuid + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User usuario = (User) obj;
			return uuid.equals(usuario.uuid);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return this.uuid.hashCode();
	}

	/**
	 * 
	 * @param register
	 *            Indica si queremos registar o desregistrar el usuario.
	 * 
	 * @return Devuelve una version serializable del usuario.
	 */
	public UserMessage userMessage(boolean register) {
		UserMessage user = new UserMessage(this, register);
		return user;
	}
}
