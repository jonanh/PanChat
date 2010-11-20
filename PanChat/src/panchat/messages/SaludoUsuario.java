package panchat.messages;

import java.io.Serializable;

import panchat.data.User;

public class SaludoUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	public User usuario;
	public boolean registrar;

	public SaludoUsuario(User pUsuario, boolean isRegistrar) {
		this.usuario = pUsuario;
		this.registrar = isRegistrar;
	}
}
