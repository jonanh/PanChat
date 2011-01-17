package simulation3;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import simulation3.model.SimulationArrowModel;

@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {

	/**
	 * Esta clase sirve para filtrar ficheros por la extension ".sim"
	 */
	FileFilter filter = new FileFilter() {

		@Override
		public boolean accept(File f) {
			String s = f.getName();
			int i = s.lastIndexOf('.');
			String extension = null;
			if (i > 0 && i < s.length() - 1) {
				extension = s.substring(i + 1).toLowerCase();
			}
			if (extension != null && extension.equals("sim"))
				return true;
			return false;
		}

		@Override
		public String getDescription() {
			return "";
		}
	};

	private FileChooser() {
		this.setFileFilter(filter);
	}

	/**
	 * Devuelve un SimulationModel leido desde un fichero.
	 * 
	 * Le pasamos por parametro parent, de modo que el dialogo se constituya
	 * como una ventana modal sobre la aplicación.
	 * 
	 * @param parent
	 * 
	 * @return
	 */
	public static SimulationArrowModel getFile(Component parent) {

		try {
			FileChooser chooser = new FileChooser();

			int returnVal = chooser.showOpenDialog(parent);

			if (returnVal != JFileChooser.APPROVE_OPTION) {
				return null;
			}

			InputStream is = new FileInputStream(chooser.getSelectedFile());
			ObjectInputStream ois = new ObjectInputStream(is);
			Object obj = ois.readObject();
			ois.close();
			is.close();

			if (obj instanceof SimulationArrowModel)
				return (SimulationArrowModel) obj;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	/**
	 * Escribe un SimulationData en un fichero
	 * 
	 * @param parent
	 * 
	 * @param simulationData
	 */
	public static void saveFile(Component parent, SimulationArrowModel simulationData) {
		try {
			FileChooser chooser = new FileChooser();

			int returnVal = chooser.showSaveDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				OutputStream os = new FileOutputStream(chooser
						.getSelectedFile());
				ObjectOutputStream oos = new ObjectOutputStream(os);

				oos.writeObject(simulationData);
				oos.close();
				os.close();
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			e.printStackTrace();
		}

	}
}
