package panchat.clocks;

public class MatrixClock_ {
	private int[][] M;
	private int myId;
	private int N;

	public MatrixClock_(int numProc, int id) {
		myId = id;
		N = numProc;
		M = new int[N][N];

		/*
		 * Inicializacion a 0 de todos los elementos.
		 */
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				M[i][j] = 0;

		/*
		 * Inicializacion a 1 del reloj lógico del vector principal
		 */
		M[myId][myId] = 1;
	}

	public void tick() {
		M[myId][myId]++;
	}

	public void sendAction() {
		// include the matrix in the message
		tick();
	}

	public void receiveAction(int[][] W, int srcId) {
		/*
		 * Actualización de los valores de los vectores no principales.
		 */
		for (int i = 0; i < N; i++)
			if (i != myId) {
				for (int j = 0; j < N; j++)
					if (W[i][j] > M[i][j])
						M[i][j] = W[i][j];
			}

		/*
		 * Actualizamos el vector principal con los valores del vector principal
		 * del origen
		 */
		for (int j = 0; j < N; j++) {
			// M[myId][j] = Util.max(M[myId][j], W[srcId][j]);
			if (M[myId][j] < W[srcId][j])
				M[myId][j] = W[srcId][j];
		}

		tick();
	}

	public int getValue(int i, int j) {
		return M[i][j];
	}
}
