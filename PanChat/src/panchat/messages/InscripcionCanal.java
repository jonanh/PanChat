package panchat.messages;

import java.io.Serializable;

import panchat.data.Canal;
import panchat.data.Usuario;

public class InscripcionCanal implements Serializable {

	private static final long serialVersionUID = 1L;

	public Canal canal;
	public boolean registrar;
	public Usuario usuario;

	/**
	 * Actualiza el estado de pertenencia de un usuario dentro de un canal
	 */
	public InscripcionCanal(Canal pCanal, Usuario pUsuario, boolean pRegistrar) {
		this.canal = pCanal;
		this.usuario = pUsuario;
		this.registrar = pRegistrar;
	}
}
