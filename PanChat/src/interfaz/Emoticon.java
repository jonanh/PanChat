package interfaz;
//creo que si sólo metemos esto puede que esta clase no sea necesaria
public class Emoticon {
	char clave[];
	String ruta;
	
	public Emoticon(char[] clave,String ruta){
		this.clave=clave;
		this.ruta=ruta;
	}
	
	public char[] obtenerClave(){
		return clave;
	}
	
	public String obtenerRuta(){
		return ruta;
	}
}
