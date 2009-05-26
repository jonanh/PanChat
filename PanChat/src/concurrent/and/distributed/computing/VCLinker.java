package concurrent.and.distributed.computing;

public class VCLinker extends Linker {
    public VectorClock vc;
    int receiveTag[] = null;
    public VCLinker(String basename, int id, int N) throws Exception {
        super(basename, id, N);
        vc = new VectorClock(N, id);
        receiveTag = new int[N];
    }
    public void sendMsg(int destId, String tag, String msg) {
        super.sendMsg(destId, "vector", vc.toString());
        super.sendMsg(destId, tag, msg);
        vc.sendAction();
    }
    public void simpleSendMsg(int destId, String tag, String msg) {
        super.sendMsg(destId, tag, msg);
     }
    public Msg receiveMsg(int fromId) throws java.io.IOException {
        Msg m1 = super.receiveMsg(fromId);
        if (m1.getTag().equals("vector")) {
            Util.readArray(m1.getMessage(), receiveTag);
            vc.receiveAction(receiveTag);
            Msg m = super.receiveMsg(fromId);//app message
            return m;
        }
        else return m1;
    }
}
