package panchat.simulation.view.listener;

import java.awt.event.MouseEvent;
import java.util.EnumMap;

import order.Message.Type;

import panchat.simulation.arrows.SimpleMultipleArrow;
import panchat.simulation.arrows.SingleArrow;
import panchat.simulation.arrows.TotalArrow;
import panchat.simulation.view.CellPosition;
import panchat.simulation.view.Position;
import panchat.simulation.view.SimulationView;

/**
 * Esta clase controla el comportamiento del View cuando hay que crear nuevas
 * flechas.
 * 
 * Durante el proceso de creación de flechas, se permite :
 * 
 * - Crear nuevas flechas en casilla vacias.
 * 
 * - Añadir nuevas flechas desde el comienzo de una flecha previamente
 * existente, creando flechas multiples.
 * 
 * - Mover los puntos finales de una flecha. Si una flecha se mueve a una
 * posición inválida, esta volverá tal y como estaba.
 * 
 * Notas :
 * 
 * Cada flecha define si la nueva posición es válida o no mediante su método
 * isValid(SimulationData). De este modo cada tipo de flecha puede determinar si
 * en la posición nueva sería o válida.
 * 
 * La encarga de fijarse definitivamente sobre el Model es la propia flecha
 * mediante el método move(SimulationData), de este modo flechas con un
 * comportamiento más complejo pueden gestionar como quieren moverse.
 * 
 */
public class CreateListener extends MoveListener {

	private EnumMap<Type, Boolean> properties = new EnumMap<Type, Boolean>(
			Type.class);

	public CreateListener(SimulationView simulationView) {
		super(simulationView);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Obtenemos la posición y la flecha en esa posición
		Position pos = simulationView.getPosition(e);

		// Si la posicion es una celda
		if (pos instanceof CellPosition) {

			CellPosition cell = (CellPosition) pos;
			drawingArrow = simulationModel.deleteArrow(cell);

			// Guardamos las posiciones de movimiento.
			lastPosition = cell;

			// Si no existe ninguna flecha, creamos una flecha nueva :
			if (drawingArrow == null) {

				if (properties.get(Type.TOTAL) != null
						&& properties.get(Type.TOTAL)) {

					simulationModel.addArrow(new TotalArrow(cell,
							simulationModel));

				} else {

					// Creamos una nueva flecha multiple
					drawingArrow = new SimpleMultipleArrow(cell);

					// Copiamos las propiedades en el mensaje creado.
					drawingArrow.getProperties().putAll(properties);

					// Creamos una nueva posicion final
					movingCell = new CellPosition(-1, -1);

					// Añadimos a la flecha multiple una flecha simple
					drawingArrow.addArrow(new SingleArrow(cell, movingCell));
					simulationView.setDrawingArrow(drawingArrow);

					movingCell.set(cell);

					// Actualizamos la posicion de la SimulationView, de manera
					// que
					// dibuje la iluminación cuando pasa el cursor por encima
					simulationView.setPosition(simulationView.getPosition(e),
							drawingArrow.isValid(simulationModel));
				}
			}
			// si hemos pinchado en el comienzo de la flecha
			else if (drawingArrow.getInitialPos().equals(cell)) {

				// Si la flecha es de tipo SimpleMultipleArrow añadimos una
				// nueva flecha
				if (drawingArrow instanceof SimpleMultipleArrow) {

					// Creamos una nueva posicion final
					movingCell = new CellPosition(-1, -1);

					// Guardamos copia
					moveArrow = drawingArrow.clone();

					// Añadimos a la flecha multiple una flecha simple
					drawingArrow.addArrow(new SingleArrow(cell, movingCell));
					simulationView.setDrawingArrow(drawingArrow);

					// Notificamos a la flecha que estamos moviendo la casilla
					// movingCell
					drawingArrow.move(movingCell);

					// Guardamos las posiciones de movimiento.
					movingCell.set(cell);

					// Actualizamos la posicion de la SimulationView, de manera
					// que
					// dibuje la iluminación cuando pasa el cursor por encima
					simulationView.setPosition(simulationView.getPosition(e),
							drawingArrow.isValid(simulationModel));
				}

			}
			// Si hemos pinchado en otra posicion, movemos la flecha.
			else {
				super.mousePressed(e);
			}

		} else
			// Actualizamos la posicion de la SimulationView, de manera que
			// dibuje la iluminación cuando pasa el cursor por encima
			simulationView.setPosition(simulationView.getPosition(e), true);
	}

	/**
	 * @return the properties
	 */
	public EnumMap<Type, Boolean> getProperties() {
		return properties;
	}
}
