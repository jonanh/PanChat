package simulation.arrows;

import java.io.Serializable;

import panchat.data.User;

@SuppressWarnings("serial")
public class Snapshot implements Serializable {

	public User process;
	public int tick;
}
