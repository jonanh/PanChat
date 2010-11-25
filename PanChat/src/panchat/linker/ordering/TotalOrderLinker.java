package panchat.linker.ordering;

public class TotalOrderLinker {

	/*
	 * - To send a multicast message, a process sends a timestamped message to
	 * all the destination processes.
	 * 
	 * - On receiving a message, a process marks it as undeliverable and sends
	 * the value of the logical clock as the proposed timestamp to the
	 * initiator.
	 * 
	 * - When the initiator has received all the proposed timestamps, it takes
	 * the maximum of all proposals and assigns that timestamp as the final
	 * timestamp to that message. This value is sent to all the destinations.
	 * 
	 * - On receiving the final timestamp of a message, it is marked as
	 * deliverable.
	 * 
	 * - A deliverable message is delivered to the site if it has the smallest
	 * timestamp in the message queue.
	 */
}
