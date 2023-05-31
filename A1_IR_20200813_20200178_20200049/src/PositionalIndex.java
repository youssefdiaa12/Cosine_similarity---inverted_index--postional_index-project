import java.util.*;

public class PositionalIndex {
    private Map<String, DictEntry> index;
    public PositionalIndex() {
        this.index = new HashMap<>();


    }

    public void addDocument(int docId, String[] terms) {
        for (int i = 0; i < terms.length; i++) {
            String term = terms[i];
            if (!index.containsKey(term)) {
                index.put(term, new DictEntry(term));
            }
            index.get(term).addPosting(docId, i);

        }
    }


    public Set<Integer> search(String[] query) {
        Set<Integer> result = new HashSet<>();
        if (query.length == 0) {
            return result;
        }
        if (!index.containsKey(query[0].toLowerCase())) {
            return result;
        }
        List<Posting> postings = index.get(query[0].toLowerCase()).getPostings();
        boolean match = true;
        if(query.length==1){
            for (Posting i: postings){
                result.add((i.getDocId()+1));
                System.out.println("For doc " + (i.getDocId()+1));
                System.out.println("the word is " + query[0] + " and the positions are " + i.getPositions());
            }
            return result;
        }
        else {
            for (int i = 1; i < query.length; i++) {
                if (!index.containsKey(query[i].toLowerCase())) {
                    match = false;
                    break;
                }
                postings = index.get(query[i - 1].toLowerCase()).getPostings();
                for (Posting j : postings) {
                    match = true;
                    int temp = j.getDocId();
                    List<Integer> positions = j.getPositions();
                    List<Integer> nextPositions = getNextPositions(temp, query[i].toLowerCase(Locale.ROOT));

                    if (!isSequential(positions, nextPositions)) {
                        match = false;
                    }
                    if (match) {
                        System.out.println("For doc " + (j.getDocId()+1));
                        System.out.println("the word is " + query[i - 1] + " and the positions are " + positions);
                        System.out.println("the word is " + query[i] + " and the next positions are " + nextPositions);

                        result.add((j.getDocId()+1));
                    }
                }
            }
            return result;
        }
    }

    private List<Integer> getNextPositions(int docId, String term) {
        for (Posting posting : index.get(term.toLowerCase()).getPostings()) {
            if (posting.getDocId() == docId) {
                return posting.getPositions();
            }
        }
        return new ArrayList<>();
    }

    private boolean isSequential(List<Integer> positions1, List<Integer> positions2) {
        int i = 0;
        int j = 0;
        while (i < positions1.size() && j < positions2.size()) {


            if (positions1.get(i) + 1 == positions2.get(j)) {
                return true;
            }
            if (positions1.get(i) < positions2.get(j)) {
                i++;
            } else {
                j++;
            }
        }
        return false;
    }


}




