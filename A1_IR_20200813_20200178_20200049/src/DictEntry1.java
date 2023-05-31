class DictEntry1 {
    public int[] doc_freq;
    public int term_freq;
    public Posting1 pList;
    public String word;

    DictEntry1() {
        doc_freq = new int[InvertedIndex.docCount];
        term_freq = 0;
        pList = null;
    }
}