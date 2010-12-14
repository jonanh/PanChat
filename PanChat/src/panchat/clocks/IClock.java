package panchat.clocks;

import panchat.data.User;

public interface IClock<T> {

	public void send(User user);

	public void receiveAction(T receivedClock);

	public IClock<T> clone();
}
