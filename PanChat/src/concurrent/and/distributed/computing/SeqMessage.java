package concurrent.and.distributed.computing;

public class SeqMessage {
    Msg m;
    int seqNo;
    public SeqMessage(Msg m, int seqNo) {
        this.m = m;
        this.seqNo = seqNo;
    }
    public int getSeqNo() {
        return seqNo;
    }
    public Msg getMessage() {
        return m;
    }
}
