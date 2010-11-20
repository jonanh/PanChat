package panchat.messages;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class FileMessage implements Serializable, Comparable<FileMessage> {

	private static final long serialVersionUID = 1L;

	/*
	 * Atributos
	 */
	private String name;
	private long size;
	private byte[] hash;
	private transient String path;
	private transient File file;

	/**
	 * Construccion del Fichero, a partir de un path indicado a través de un
	 * String pasado por parámetro.
	 * 
	 * Constructora orientada a ser usados por el Servidor.
	 * 
	 * @param pFilePath
	 * @throws IOException
	 */
	public FileMessage(String pFilePath) throws IOException {
		this(new File(pFilePath));
	}

	/**
	 * Construccion del Fichero, a partir de una referencia a un objeto File.
	 * 
	 * Constructora orientada a ser usada por el Servidor.
	 * 
	 * @param pFile
	 * @throws IOException
	 */
	public FileMessage(File pFile) throws IOException {
		this.hash = md5(pFile);
		this.path = pFile.getAbsolutePath();
		this.name = pFile.getName();
		this.size = pFile.length();
		this.file = pFile;
	}

	/**
	 * Construccion del Fichero, a partir del tama�o y del c�digo hash.
	 * 
	 * Constructora orientada a ser usada por el Cliente.
	 * 
	 * @param pFile
	 * @throws IOException
	 */
	public FileMessage(byte[] pHash, long pTamanyo) {
		this.hash = pHash;
		this.size = pTamanyo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileMessage) {

			FileMessage fichObj = (FileMessage) obj;
			return Arrays.equals(this.hash, fichObj.hash)
					&& this.size == fichObj.size;
		}
		return false;
	}

	/**
	 * Devuelve el nombre del fichero.
	 * 
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Devuelve el path absoluto del fichero.
	 * 
	 * @return String
	 */
	public String getAbsolutePath() {
		return path;
	}

	/**
	 * Devuelve el tamaño del fichero.
	 * 
	 * @return long
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Devuelve el hash/md5 correspondiente al fichero.
	 * 
	 * @return byte[]
	 */
	public byte[] getHash() {
		return hash;
	}

	/**
	 * Devuelve la instancia al objeto file, el atributo file no es
	 * serializable, así que atención :-).
	 * 
	 * @return File
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Devuelve el hash/md5 correspondiente al fichero, como String.
	 * 
	 * @return String
	 */
	public String getHashString() {
		return toStringBase16(hash);
	}

	@Override
	public String toString() {
		return this.getName() + "\t " + this.size + "\t"
				+ this.getHashString();
	}

	/*
	 * Funciones privadas :
	 */

	private static byte[] md5(File f) throws IOException {
		MessageDigest digest;

		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.toString());
		}

		InputStream is = new FileInputStream(f);
		byte[] buffer = new byte[8192];
		int read = 0;

		while ((read = is.read(buffer)) > 0) {
			digest.update(buffer, 0, read);
		}
		return digest.digest();
	}

	/**
	 * Convert binary data to a hex-encoded String
	 * 
	 * @param b
	 *            An array containing binary data
	 * @return A String containing the encoded data
	 */
	private static String toStringBase16(byte[] b) {
		final String Base16 = "0123456789ABCDEF";

		ByteArrayOutputStream os = new ByteArrayOutputStream();

		for (byte value : b) {
			byte high = (byte) (value >> 4 & 0xF);
			byte low = (byte) (value & 0xF);
			os.write(Base16.charAt(high));
			os.write(Base16.charAt(low));
		}
		return new String(os.toByteArray());
	}

	@Override
	public int compareTo(FileMessage obj) {
		return (int) (obj.size - this.size);
	}
}
