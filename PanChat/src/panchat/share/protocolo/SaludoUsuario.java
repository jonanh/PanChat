package panchat.share.protocolo;

import java.io.Serializable;

import panchat.data.Usuario;

public class SaludoUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	public Usuario usuario;
	public boolean registrar;

	public SaludoUsuario(Usuario pUsuario, boolean isRegistrar) {
		this.usuario = pUsuario;
		this.registrar = isRegistrar;
	}
}
