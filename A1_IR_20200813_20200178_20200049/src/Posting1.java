public class Posting1 {
    public int docId;
    public float tfidf;
    public Posting1 next;

    public Posting1(int docId, float tfidf, Posting1 next) {
        this.docId = docId;
        this.tfidf = tfidf;
        this.next = next;
    }
}