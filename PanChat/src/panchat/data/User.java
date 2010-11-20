package panchat.data;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable, Comparable<User> {

	private static final long serialVersionUID = 1L;

	public UUID uuid;
	public String ip;
	public int port;
	public String nickName;

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
}
