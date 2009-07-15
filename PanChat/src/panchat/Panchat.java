package panchat;

import panchat.addressing.ListaCanales;
import panchat.addressing.ListaUsuarios;
import panchat.addressing.Usuario;
import panchat.linker.CausalLinker;
import panchat.linker.Linker;

public class Panchat {
	private ListaUsuarios listaUsuarios;
	private ListaCanales listaCanales;
	private Usuario usuario;
	private CausalLinker causalLinker;
	private Linker linker;

	/**
	 * 
	 * @param usuario
	 */
	public Panchat(Usuario usuario) {
		this.listaUsuarios = new ListaUsuarios();
		this.listaCanales = new ListaCanales();
		this.usuario = usuario;

		// Nos añadimos a nuestra propia lista de usuarios
		this.listaUsuarios.añadirUsuario(usuario);
	}

	/**
	 * Devuelve la lista de usuarios del chat
	 * 
	 * @return
	 */
	public ListaUsuarios getListaUsuarios() {
		return listaUsuarios;
	}

	/**
	 * Devuelve la lista de canales del chat
	 * 
	 * @return
	 */
	public ListaCanales getListaCanales() {
		return listaCanales;
	}

	/**
	 * Devuelve la lista de canales del chat
	 * 
	 * @return
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Devuelve el causal linker asociado al chat
	 * 
	 * @return
	 */
	public CausalLinker getCausalLinker() {
		return causalLinker;
	}

	/**
	 * Devuelve el linker asociado al chat
	 * 
	 * @return
	 */
	public Linker getLinker() {
		return linker;
	}
}
