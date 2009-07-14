package panchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Test01 {
	public static void main(String[] args) {

		try {
			System.out.println("Creando Server Socket");

			final ServerSocket serverSocket = new ServerSocket(50000);

			Thread thread = new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						Socket socket;
						try {
							socket = serverSocket.accept();

							InetAddress inet = socket.getInetAddress();
							System.out.println("Conectado con: " + inet);

							ObjectInputStream ois = new ObjectInputStream(
									socket.getInputStream());
							ObjectOutputStream oos = new ObjectOutputStream(
									socket.getOutputStream());

							// String mensajeRecibido = br.readLine();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});

			thread.start();

			// Creamos el socket

			Socket s = new Socket("localhost", 50000);

			System.out.println("Creando OIS/OOS");
			
			// Creamos los object streams
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Funcionaaaa!!!");
	}
}
