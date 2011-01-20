package simulation;

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

import simulation.model.SimulationArrowModel;

@SuppressWarnings("serial")
public class FileChooser extends JFileChooser {

	private final static String DESCRIPTION = "Simulation (*.sim)";
	private final static String[] EXTENSIONS = new String[] { "sim" };

	class SimpleFileFilter extends FileFilter {

		String[] extensions;

		String description;

		public SimpleFileFilter(String ext) {
			this(new String[] { ext }, null);
		}

		public SimpleFileFilter(String[] exts, String descr) {
			// Clone and lowercase the extensions
			extensions = new String[exts.length];
			for (int i = exts.length - 1; i >= 0; i--) {
				extensions[i] = exts[i].toLowerCase();
			}
			// Make sure we have a valid (if simplistic) description
			description = (descr == null ? exts[0] + " files" : descr);
		}

		public boolean accept(File f) {
			// We always allow directories, regardless of their extension
			if (f.isDirectory()) {
				return true;
			}

			// Ok, it's a regular file, so check the extension
			String name = f.getName().toLowerCase();
			for (int i = extensions.length - 1; i >= 0; i--) {
				if (name.endsWith(extensions[i])) {
					return true;
				}
			}
			return false;
		}

		public String getDescription() {
			return description;
		}
	}

	private FileChooser() {
		this.addChoosableFileFilter(new SimpleFileFilter(EXTENSIONS,
				DESCRIPTION));
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
	public static void saveFile(Component parent,
			SimulationArrowModel simulationData) {
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
