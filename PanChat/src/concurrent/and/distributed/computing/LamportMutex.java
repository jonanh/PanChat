package concurrent.and.distributed.computing;

public class LamportMutex extends Process implements Lock {
    DirectClock v;
    int[] q; // request queue
    public LamportMutex(Linker initComm) {
        super(initComm);
        v = new DirectClock(N, myId);
        q = new int[N];
        for (int j = 0; j < N; j++) 
            q[j] = Symbols.Infinity;
    }
    public synchronized void requestCS() {
        v.tick();
        q[myId] = v.getValue(myId);
        broadcastMsg("request", q[myId]);
        while (!okayCS())
            myWait();
    }
    public synchronized void releaseCS() {
        q[myId] = Symbols.Infinity;
        broadcastMsg("release", v.getValue(myId));
    }
    boolean okayCS() {
        for (int j = 0; j < N; j++){
            if (isGreater(q[myId], myId, q[j], j))
                return false;
            if (isGreater(q[myId], myId, v.getValue(j), j))
                return false;
        }
        return true;
    }
    boolean isGreater(int entry1, int pid1, int entry2, int pid2) {
        if (entry2 == Symbols.Infinity) return false;
        return ((entry1 > entry2) 
                || ((entry1 == entry2) && (pid1 > pid2)));
    }
    public synchronized void handleMsg(Msg m, int src, String tag) {
        int timeStamp = m.getMessageInt();
        v.receiveAction(src, timeStamp);
        if (tag.equals("request")) {
            q[src] = timeStamp;
            sendMsg(src, "ack", v.getValue(myId));
        } else if (tag.equals("release"))
            q[src] = Symbols.Infinity;
        notify(); // okayCS() may be true now
    }
}
