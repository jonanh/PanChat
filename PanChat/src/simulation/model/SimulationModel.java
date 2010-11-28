package simulation.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;

import simulation.arrows.MessageArrow;

/**
 * Clase que representa los datos del simulador :
 * 
 * - Lista de flechas
 * 
 * -
 */
@SuppressWarnings("serial")
public class SimulationModel extends Observable implements Serializable {

	/*
	 * Numero de casillas del tablero
	 */
	public static final int DEFAULT_NUM_PROCESSES = 4;
	public static final int DEFAULT_NUM_TICKS = 9;

	/*
	 * Atributos
	 */
	private ArrayList<Boolean> listaCortes = new ArrayList<Boolean>();
	private ArrayList<ArrayList<MessageArrow>> listaFlechas = new ArrayList<ArrayList<MessageArrow>>();
	private int numTicks = DEFAULT_NUM_TICKS;

	/**
	 * Construimos el objeto de datos de simulacion
	 */
	public SimulationModel() {
		setNumProcesses(DEFAULT_NUM_PROCESSES);
	}

	/*
	 * Número de procesos
	 */
	public int getNumProcesses() {
		return listaFlechas.size();
	}

	public int setNumProcesses(int pNumProcesses) {
		// FIXME debemos comprobar que:
		// numTicks = max( pTimeTicks, TickUltimaFlecha)

		int numProcesses = pNumProcesses - getNumProcesses();

		// Si hay que añadir nuevos procesos :
		if (numProcesses > 0) {
			for (int i = 0; i < numProcesses; i++)
				listaFlechas.add(new ArrayList<MessageArrow>());

			this.hasChanged();

		} // Si hay que eliminar nuevos procesos
		else if (numProcesses < 0) {
			for (int i = getNumProcesses(); i > pNumProcesses; i++)
				listaFlechas.remove(i);

			this.hasChanged();
		}

		this.notifyObservers();

		return getNumProcesses();
	}

	/*
	 * Número de ticks
	 */
	public int getTimeTicks() {
		return this.numTicks;
	}

	public int setTimeTicks(int pTimeTicks) {
		// FIXME debemos comprobar que:
		// numTicks = max( pTimeTicks, TickUltimaFlecha)

		this.numTicks = pTimeTicks;
		return numTicks;
	}
}
