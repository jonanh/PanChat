package interfaz.paneles;

import java.awt.Graphics;

public class PanelRecuadro extends MiPanel {

	private static final long serialVersionUID = 1L;

	public PanelRecuadro() {
		super();
	}

	public PanelRecuadro(String ruta) {
		super(ruta);
	}

	public void paint(Graphics g) {

		super.paint(g);

		double posX, posY, tamX, tamY;
		posX = this.getX();
		posY = this.getY();
		tamX = this.getWidth();
		tamY = this.getHeight();
		int i = 0;
		for (; i < 4; i++)
			g.drawRect((int) (posX + i), (int) (posY + i),
					(int) (tamX - 2 * i - 2), (int) (tamY - 2 * i - 2));

	}
}
