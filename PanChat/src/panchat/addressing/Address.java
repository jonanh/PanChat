package panchat.addressing;

import java.io.Serializable;
import java.util.UUID;

public class Address implements Serializable, Comparable<Address> {

	private static final long serialVersionUID = 1L;

	UUID uuid;
	String ip;
	transient String nickName;

	@Override
	public int compareTo(Address o) {
		if (o instanceof Address)
			return uuid.compareTo(((Address) o).uuid);
		else
			return 0;
	}
}
