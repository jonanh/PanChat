package order.layer;

import order.Message;

public class TotalMessage {

	/*
	 * Tramas para cada una de las fases de la ordenación total.
	 */
	public static class TotalSendMsg implements Message.Fifo, Message.Total {
		int clock;
		TotalUndeliverable content;

		@Override
		public String toString() {
			return "TotalSend(" + content.msgReference + ")";
		}
	}

	public static class TotalProposalMsg implements Message.Fifo, Message.Total {
		int clock;
		int msgReference;

		@Override
		public String toString() {
			return "TotalProposal(" + msgReference + ")";
		}
	}

	public static class TotalFinalMsg implements Message.Fifo, Message.Total {
		int clock;
		int msgReference;

		@Override
		public String toString() {
			return "TotalFinal(" + msgReference + ")";
		}
	}

}
