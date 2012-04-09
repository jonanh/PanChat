package panchat.ui.chat;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import panchat.Panchat;
import panchat.data.ChatRoom;

public class ChatRoomWindow extends JFrame implements
		IVentanaConversacion {

	private static final long serialVersionUID = 1L;

	private final Panchat panchat;

	private final ChatRoom canal;

	private PanelConversacion panelConversacion;

	public ChatRoomWindow(Panchat pPanchat, ChatRoom pCanal) {

		this.panchat = pPanchat;

		this.canal = pCanal;

		this.getContentPane().setLayout(new BorderLayout());

		// Creamos el panel de la conversacion y lo añadimos a la ventana
		panelConversacion = new PanelConversacion(this);

		this.add(panelConversacion, BorderLayout.CENTER);

		// Creamos el panel del canal
		ChatRoomPanel panelCanal = new ChatRoomPanel(panchat, canal);

		this.getContentPane().add(panelCanal, BorderLayout.EAST);

		pack();

		// Establecemos el nombre de la ventana
		String nombreVentana = panchat.getUsuario().nickName
				+ " conversando en el canal " + canal.getName();
		this.setTitle(nombreVentana);

		/*
		 * Establecemos el método de cerrado como destruir los recursos de la
		 * ventana al cerrar
		 */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		/*
		 * Cambiamos el listener de la ventana para gestionar nosotros la
		 * correcta terminación de la aplicación
		 */
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				panchat.accionCerrarConversacionCanal(canal);
			}
		});

		setSize(750, 500);
		setVisible(true);
	}

	@Override
	public void eventoNuevoComentario(String comentario) {
		String nickName = panchat.getUsuario().nickName;
		String entrada = "<" + nickName + "> " + comentario;

		panchat.escribirComentarioCanal(canal, entrada);
		this.escribirComentario(entrada);
	}

	/**
	 * Escribir nuevo comentario
	 * 
	 * @param comentario
	 */
	public void escribirComentario(String comentario) {
		panelConversacion.escribirComentario(comentario);
	}
}