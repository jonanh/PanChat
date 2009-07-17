package panchat.share.protocolo;

import java.io.Serializable;

import panchat.addressing.users.Usuario;

public class UsuarioCanal implements Serializable {

	private static final long serialVersionUID = 1L;

	public Usuario usuario;
	public boolean registrar;

	public UsuarioCanal(Usuario usuario, boolean registrar) {
		this.usuario = usuario;
		this.registrar = registrar;
	}
}
