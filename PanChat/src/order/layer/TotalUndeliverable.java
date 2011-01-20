package order.layer;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import order.Message;

import panchat.data.User;

public class TotalUndeliverable implements Comparable<TotalUndeliverable> {

	protected int msgReference;
	protected int priority;
	protected Message content;
	protected List<User> userList;
	protected List<User> userResponseList = new LinkedList<User>();
	protected boolean deliverable = false;

	public TotalUndeliverable(int msgReference, int priority, Message content,
			List<User> userList) {
		this.msgReference = msgReference;
		this.priority = priority;
		this.content = content;
		this.userList = userList;
	}

	public void addPriority(User user, int priority) {

		if (!userResponseList.contains(user)) {
			this.priority = Math.max(this.priority, priority);
			this.userResponseList.add(user);
		} else
			System.out.println("Un cliente ha propuesto 2 veces");

		if (userList.size() == userResponseList.size())
			this.deliverable = true;
	}

	public void setDeliverable(int finalPriority) {
		this.deliverable = true;
		this.priority = finalPriority;
	}

	@Override
	public int compareTo(TotalUndeliverable o) {
		int result = this.priority - o.priority;
		if (result == 0)
			return this.msgReference - o.msgReference;
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof TotalUndeliverable) {
			TotalUndeliverable obj = (TotalUndeliverable) o;
			return obj.msgReference == this.msgReference;
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + priority + "," + msgReference + ")";
	}

	public TotalUndeliverable clone() {
		TotalUndeliverable clone = new TotalUndeliverable(this.msgReference,
				this.priority, this.content, null);
		return clone;
	}

	public static void main(String[] args) {
		PriorityQueue<TotalUndeliverable> queue = new PriorityQueue<TotalUndeliverable>();
		TotalUndeliverable a = new TotalUndeliverable(0, 30, null, null);
		TotalUndeliverable b = new TotalUndeliverable(1, 20, null, null);
		TotalUndeliverable c = new TotalUndeliverable(2, 40, null, null);
		TotalUndeliverable d = new TotalUndeliverable(3, 10, null, null);
		TotalUndeliverable e = new TotalUndeliverable(4, 10, null, null);
		queue.add(a);
		queue.add(b);
		queue.add(c);
		System.out.println(queue);
		System.out.println(queue.poll());
		queue.add(e);
		queue.add(d);
		System.out.println(queue);
		System.out.println(queue.peek());
	}
}
