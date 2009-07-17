package panchat.addressing.users;

import java.io.Serializable;
import java.util.UUID;

public class Usuario implements Serializable, Comparable<Usuario> {

	private static final long serialVersionUID = 1L;

	public UUID uuid;
	public String ip;
	public int port;
	public String nickName;

	public Usuario(String ip, int port, String nickName) {
		this.ip = ip;
		this.port = port;
		this.nickName = nickName;
		this.uuid = UUID.randomUUID();
	}

	@Override
	public int compareTo(Usuario o) {
		return uuid.compareTo(o.uuid);
	}

	@Override
	public String toString() {
		return nickName;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Usuario) {
			Usuario usuario = (Usuario) obj;
			return uuid.equals(usuario.uuid);
		} else
			return false;
	}

}
