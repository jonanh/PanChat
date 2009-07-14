package panchat.share.protocolo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class Bloque implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int Bloque_TAMANYO = 8 << 10;

	private byte[] data;
	private long numBloque;
	private Fichero fichero;

	public Bloque(Fichero pFichero, long pNumBloque, boolean leerDatos) {
		fichero = pFichero;
		numBloque = pNumBloque;
		if (leerDatos) {
			try {
				leer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * 
	 * @return
	 */
	public Fichero getFichero() {
		return fichero;
	}

	/**
	 * 
	 * @return
	 */
	public long getNumBloque() {
		return numBloque;
	}

	/**
	 * 
	 * @param pFichero
	 * @param pNumBloque
	 * @return
	 * @throws IOException
	 */
	public void leer() throws IOException {

		InputStream is = new FileInputStream(fichero.getFile());
		byte[] buffer = new byte[Bloque_TAMANYO];
		is.skip(numBloque * Bloque_TAMANYO);
		is.read(buffer);
		is.close();

		data = buffer;
	}

	/**
	 * 
	 * @param pFichero
	 * @param pNumBloque
	 * @param data
	 * @throws IOException
	 */
	public void escribir(String pFicheroNombre) throws IOException {

		RandomAccessFile os = new RandomAccessFile(pFicheroNombre, "rw");

		/*
		 * Escribimos el bloque donde le corresponde.
		 */

		int bytesAEscribir;
		if ((numBloque + 1) * Bloque_TAMANYO > fichero.getTamanyo())
			bytesAEscribir = (int) fichero.getTamanyo() % Bloque_TAMANYO;
		else
			bytesAEscribir = Bloque_TAMANYO;

		os.seek(numBloque * Bloque_TAMANYO);
		os.write(data, 0, bytesAEscribir);
		os.close();
	}

	/*
	 * HashCode y Equals
	 */

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fichero == null) ? 0 : fichero.hashCode());
		result = prime * result + (int) (numBloque ^ (numBloque >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Bloque) {

			Bloque bloqueObj = (Bloque) obj;
			return this.fichero.equals(bloqueObj.fichero)
					&& this.numBloque == bloqueObj.numBloque;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Fichero : " + this.fichero + " numero_bloque : "
				+ this.numBloque;
	}
}
