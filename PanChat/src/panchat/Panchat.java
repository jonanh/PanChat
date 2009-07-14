package panchat;

import panchat.addressing.ListaCanales;
import panchat.addressing.ListaUsuarios;
import panchat.addressing.Usuario;

public class Panchat {
	private ListaUsuarios listaUsuarios;
	private ListaCanales listaCanales;
	private Usuario usuario;
	
	/**
	 * 
	 * @param usuario
	 */
	public Panchat(Usuario usuario){
		this.listaUsuarios = new ListaUsuarios();
		this.listaCanales = new ListaCanales();	
		this.usuario = usuario;
		
		// Nos añadimos a nuestra propia lista de usuarios
		this.listaUsuarios.añadirUsuario(usuario);
	}

	/**
	 * 
	 * @return
	 */
	public ListaUsuarios getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * 
	 * @return
	 */
	public ListaCanales getListaCanales() {
		return listaCanales;
	}

	/**
	 * 
	 * @return
	 */
	public Usuario getUsuario() {
		return usuario;
	}
}
