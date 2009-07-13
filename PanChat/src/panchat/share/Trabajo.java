package panchat.share;

public abstract class Trabajo implements Comparable<Trabajo> {

	

	public abstract void cancelarTrabajo();

	public abstract long getTrabajoTimeOut();

	long plazo;

	Trabajo() {
		plazo = System.currentTimeMillis() + this.getTrabajoTimeOut();

		if (Configuracion.Trabajo_DEBUG) {
			String mensaje = "Trabajo.class : Nuevo trabajo creado con plazo : "
					+ this.getTrabajoTimeOut();
			System.out.println(mensaje);
		}
	}

	@Override
	public int compareTo(Trabajo o) {
		return (int) (this.plazo - o.plazo);
	}

	@Override
	public String toString() {
		return "(" + Long.toString(plazo - System.currentTimeMillis()) + ","
				+ this.hashCode() + ")";
	}
}
