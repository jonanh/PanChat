package concurrent.and.distributed.computing;

import java.util.*;
public class MsgList extends LinkedList {
    public Msg removeM(int seqNo) {
        SeqMessage sm;
        ListIterator iter = super.listIterator(0);
        while (iter.hasNext()) {
            sm = (SeqMessage) iter.next();
            if (sm.getSeqNo() == seqNo) {
                iter.remove();
                return sm.getMessage();
            }
        }
        return null;
    }
}
