package concurrent.and.distributed.computing;

import java.io.*;
public class SynchLinker extends Linker {
    final static int passive = 0, active = 1;
    int state = active;
    private boolean granted;
    public SynchLinker(String basename, int id, int numProc)
    throws Exception {
        super(basename, id, numProc);
    }
    public synchronized void sendMsg(int destId, String tag,String msg) {
        if (destId < myId) { // big message
            waitForActive();
            super.sendMsg(destId, "app", " ");
            super.sendMsg(destId, tag, msg);            
            state = passive;
        } else { // small message
            granted = false;
            super.sendMsg(destId, "request", " ");
            while (!granted) Util.myWait(this);// wait for permission
            super.sendMsg(destId, "app", " ");
            super.sendMsg(destId, tag, msg);
        }
    }
    synchronized void turnActive(){
        state = active; notifyAll();
    }
    synchronized void waitForActive(){
        while (state != active) Util.myWait(this);
    }
    synchronized void grant(){
        granted = true; notifyAll();
    }
    public Msg receiveMsg(int fromId) throws IOException {
        boolean done = false;
        Msg m = null;
        while (!done) { // app msg received
            m = super.receiveMsg(fromId);
            String tag = m.getTag();
            if (tag.equals("app")) {
                if (m.getSrcId() > myId) { // big message
                    waitForActive();
                    m = super.receiveMsg(fromId);
                    super.sendMsg(fromId, "ack", " ");
                } else { // small message
                    m = super.receiveMsg(fromId);
                    turnActive();
                }
                done = true;
            } else if (tag.equals("ack")) turnActive();
            else if (tag.equals("request")) {
                waitForActive();
                super.sendMsg(fromId, "permission", " ");
            } else if (tag.equals("permission")) grant();
        }
        return m;
    }
}
