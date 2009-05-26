package concurrent.and.distributed.computing;

import java.util.*; import java.net.*; import java.io.*;
public class CausalLinker extends Linker {
    int M[][];
    LinkedList deliveryQ = new LinkedList(); // deliverable messages
    LinkedList pendingQ = new LinkedList(); // messages with matrix
    public CausalLinker(String basename, int id, int numProc)
                                            throws Exception {
        super(basename, id, numProc);
        M = new int[N][N]; Matrix.setZero(M);
    }
    public synchronized void sendMsg(int destId, String tag, String msg){
        M[myId][destId]++;
        super.sendMsg(destId, "matrix", Matrix.write(M));
        super.sendMsg(destId, tag, msg);
    }
    public synchronized void multicast(IntLinkedList destIds, 
                                            String tag, String msg) {
        for (int i=0; i<destIds.size(); i++)
            M[myId][destIds.getEntry(i)]++;
        for (int i=0; i<destIds.size(); i++) {
            int destId = destIds.getEntry(i);
            super.sendMsg(destId, "matrix", Matrix.write(M));
            super.sendMsg(destId, tag, msg);           
        }
    }
    boolean okayToRecv(int W[][], int srcId) {
        if (W[srcId][myId] > M[srcId][myId]+1) return false;
        for (int k = 0; k < N; k++)
            if ((k!=srcId) && (W[k][myId] > M[k][myId])) return false;
        return true;
    }
    synchronized void checkPendingQ() {
        ListIterator iter = pendingQ.listIterator(0);
        while (iter.hasNext()) {
            CausalMessage cm = (CausalMessage) iter.next();
            if (okayToRecv(cm.getMatrix(), cm.getMessage().getSrcId())){
                iter.remove(); deliveryQ.add(cm);
            }
        }
    }    
    // polls the channel given by fromId to add to the pendingQ
    public Msg receiveMsg(int fromId) throws IOException {
        checkPendingQ();
        while (deliveryQ.isEmpty()) {
            Msg matrix = super.receiveMsg(fromId);// matrix
            int [][]W = new int[N][N];
            Matrix.read(matrix.getMessage(), W);
            Msg m1 = super.receiveMsg(fromId);//app message
            pendingQ.add(new CausalMessage(m1, N, W));
            checkPendingQ();
        }
        CausalMessage cm = (CausalMessage) deliveryQ.removeFirst();
        Matrix.setMax(M, cm.getMatrix());
        return cm.getMessage();
    }
}
