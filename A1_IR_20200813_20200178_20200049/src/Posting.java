
import java.util.ArrayList;
import java.util.List;

class Posting {
    public int docId;
    public List<Integer> positions;
    int dtf;
    Posting next;

    public Posting(int docId) {
        this.docId = docId;
        this.positions = new ArrayList<>();
        dtf=1;
        next=null;
    }

    public void addPosition(int position) {
        positions.add(position);
    }

    public int getDocId() {
        return docId;
    }

    public List<Integer> getPositions() {
        return positions;
    }
}