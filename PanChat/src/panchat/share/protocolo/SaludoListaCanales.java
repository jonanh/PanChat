package panchat.share.protocolo;

import java.io.Serializable;
import java.util.LinkedList;

import panchat.data.Canal;

public class SaludoListaCanales implements Serializable {

	private static final long serialVersionUID = 1L;

	public LinkedList<Canal> lista;

	public SaludoListaCanales(LinkedList<Canal> pLista) {
		this.lista = pLista;
	}
}
