package panchat.share.protocolo;

import java.io.Serializable;

import panchat.channels.Canal;
import panchat.users.Usuario;

public class UsuarioCanal implements Serializable {

	private static final long serialVersionUID = 1L;

	public Usuario usuario;
	public Canal canal;
	public boolean registrar;

	public UsuarioCanal(Usuario usuario, Canal canal, boolean registrar) {
		this.usuario = usuario;
		this.canal = canal;
		this.registrar = registrar;
	}
}
