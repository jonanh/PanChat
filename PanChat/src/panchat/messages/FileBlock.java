package panchat.messages;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;

public class FileBlock implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int Bloque_TAMANYO = 8 << 10;

	private byte[] data;
	private long blockNum;
	private FileMessage file;

	public FileBlock(FileMessage pFile, long pBlockNum, boolean readData) {
		file = pFile;
		blockNum = pBlockNum;
		if (readData) {
			try {
				read();
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
	public FileMessage getFile() {
		return file;
	}

	/**
	 * 
	 * @return
	 */
	public long getBlockNum() {
		return blockNum;
	}

	/**
	 * 
	 * @param pFichero
	 * @param pNumBloque
	 * @return
	 * @throws IOException
	 */
	public void read() throws IOException {

		InputStream is = new FileInputStream(file.getFile());
		byte[] buffer = new byte[Bloque_TAMANYO];
		is.skip(blockNum * Bloque_TAMANYO);
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
	public void write(String pFileName) throws IOException {

		RandomAccessFile os = new RandomAccessFile(pFileName, "rw");

		/*
		 * Escribimos el bloque donde le corresponde.
		 */

		int bytesAEscribir;
		if ((blockNum + 1) * Bloque_TAMANYO > file.getSize())
			bytesAEscribir = (int) file.getSize() % Bloque_TAMANYO;
		else
			bytesAEscribir = Bloque_TAMANYO;

		os.seek(blockNum * Bloque_TAMANYO);
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
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + (int) (blockNum ^ (blockNum >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileBlock) {

			FileBlock bloqueObj = (FileBlock) obj;
			return this.file.equals(bloqueObj.file)
					&& this.blockNum == bloqueObj.blockNum;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Fichero : " + this.file + " numero_bloque : "
				+ this.blockNum;
	}
}
