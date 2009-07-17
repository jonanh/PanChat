package panchat.share.protocolo;

import java.io.Serializable;

import panchat.addressing.users.Usuario;

public class SaludoUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	private Usuario usuario;
	private boolean registrar;

	/**
	 * Crear un nuevo mensaje para registrar clientes
	 * 
	 * @param usuario
	 * @param registrar
	 */
	public SaludoUsuario(Usuario usuario, boolean registrar) {
		this.usuario = usuario;
		this.registrar = registrar;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public boolean isRegistrar() {
		return registrar;
	}
}
