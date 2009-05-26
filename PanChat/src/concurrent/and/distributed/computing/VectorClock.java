package concurrent.and.distributed.computing;

public class VectorClock {
    public int[] v;
    int myId;
    int N;
    public VectorClock(int numProc, int id) {
        myId = id;
        N = numProc;
        v = new int[numProc];
        for (int i = 0; i < N; i++) v[i] = 0;
        v[myId] = 1;
    }
    public void tick() {
        v[myId]++;
    }
    public void sendAction() {
        //include the vector in the message
        v[myId]++;
    }
    public void receiveAction(int[] sentValue) {
        for (int i = 0; i < N; i++)
            v[i] = Util.max(v[i], sentValue[i]);
        v[myId]++;
    }
    public int getValue(int i) {
        return v[i];
    }
    public String toString(){
        return Util.writeArray(v);
    }
}
