package panchat.share.protocolo;

import java.io.Serializable;

public enum Protocolo implements Serializable {

	/*
	 * Peticiones
	 */

	/*
	 * Cliente : Solicita LISTADO_FICHEROS
	 * 
	 * Servidor : Responde Lista_Ficheros.class
	 */
	LISTADO_FICHEROS,

	/*
	 * Cliente : Solitita PETICION_FICHERO Fichero.class Integer.class
	 * 
	 * Servidor :
	 * 
	 * Si existe el fichero y dicho bloque :
	 * 
	 * - Responde Bloque.class
	 * 
	 * else
	 * 
	 * - Responde ERROR_FICHERO_NO_DISPONIBLE
	 */
	PETICION_FICHERO,

	/*
	 * Cliente : Solitita PETICION_FICHERO_CONTINUAR
	 * 
	 * Servidor :
	 * 
	 * Si se hab�a realizado una peticion a un fichero existente y existe un
	 * proximo bloque :
	 * 
	 * - Responde Bloque.class
	 * 
	 * else
	 * 
	 * - Responde ERROR_FICHERO_NO_DISPONIBLE
	 */
	PETICION_FICHERO_CONTINUAR,

	/*
	 * Errores
	 */
	ERROR_FICHERO_NO_DISPONIBLE, ERROR_TIMEOUT_CANCELADO, ERROR_CONEXION_TIMEOUT_CANCELADO, ERROR_PROTOCOLO_ERRONEO,

	/*
	 * Control de sesiones
	 * 
	 * FIXME Sin definir y sin usar actualmente
	 */
	INICIAR_SESION, ACK_SESION,

	/*
	 * Control
	 */

	/*
	 * FIXME Sin definir y sin usar actualmente
	 */
	LISTADO_CONEXIONES, MATAR_CONEXION,

	/*
	 * Cliente : Solicita TERMINAR
	 * 
	 * Servidor : Responde TERMINAR
	 */
	TERMINAR;
}
