import java.util.ArrayList;
import java.util.List;

class DictEntry {
    public String term;
    public List<Posting> postings;
    int[] doc_freq;


    public DictEntry(String term) {
        this.term = term;
        this.postings = new ArrayList<>();
        doc_freq = new int[10];
    }

    public void addPosting(int docId, int position) {
        for (Posting posting : postings) {
            if (posting.getDocId() == docId) {
                posting.addPosition(position);
                return;
            }
        }
        Posting newPosting = new Posting(docId);
        newPosting.addPosition(position);
        postings.add(newPosting);
    }

    public String getTerm() {
        return term;
    }

    public List<Posting> getPostings() {
        return postings;
    }
}