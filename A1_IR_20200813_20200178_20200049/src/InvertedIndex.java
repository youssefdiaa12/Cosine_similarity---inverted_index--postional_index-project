import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class InvertedIndex {
    private HashMap<String, DictEntry1> index;
    static public int docCount;
    private String[] files;
    private HashMap<Integer, HashMap<String, Integer>> docTermFreqs;

    public InvertedIndex(String[] files) {
        this.files = files;
        index = new HashMap<String, DictEntry1>();
        docCount = files.length;
        docTermFreqs = new HashMap<Integer, HashMap<String, Integer>>();
        buildIndex();
    }

    private void buildIndex() {
        for (int i = 0; i < files.length; i++) {
            int docId = i;
            ArrayList<String> words = parseFile(files[i].toLowerCase(Locale.forLanguageTag(files[i])));
            HashMap<String, Integer> termFreqs = new HashMap<String, Integer>();
            for (String word : words) {
                if (index.containsKey(word)) {
                    DictEntry1 entry1 = index.get(word);
                    entry1.doc_freq[i]++;
                    Posting1 posting = entry1.pList;
                    while (posting.next != null && posting.docId != docId) {
                        posting = posting.next;
                    }
                    if (posting.docId == docId) {
                        posting.tfidf++;
                    } else {
                        posting.next = new Posting1(docId, 1, null);
                    }
                } else {
                    DictEntry1 entry1 = new DictEntry1();
                    entry1.word = word;
                    entry1.doc_freq[i] = 1;
                    entry1.term_freq = 1;
                    entry1.pList = new Posting1(docId, 1, null);
                    index.put(word, entry1);
                }
                termFreqs.put(word, termFreqs.getOrDefault(word, 0) + 1);
            }
            for (String word : termFreqs.keySet()) {
                DictEntry1 entry = index.get(word);
                entry.term_freq++;
            }
            docTermFreqs.put(docId, termFreqs);
        }
    }

    private ArrayList<String> parseFile(String fileName) {
        ArrayList<String> words = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineWords = line.split(" ");
                for (String word : lineWords) {
                    words.add(word);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public List<Pair<String, Double>> rankFilesByCosineSimilarity(Vector<String> query) {
        HashMap<String, Double> docSimilarities = new HashMap<String, Double>();
        for (int i = 0; i < docCount; i++) {
            String fileName = files[i];
            double cosineSimilarity = getCosineSimilarity(docTermFreqs.get(i), query);
            docSimilarities.put(fileName, cosineSimilarity);
        }
        List<Pair<String, Double>> sortedDocs = new ArrayList<>();
        for (String fileName : docSimilarities.keySet()) {
            sortedDocs.add(new Pair<>(fileName, docSimilarities.get(fileName)));
        }
        Collections.sort(sortedDocs, new Comparator<Pair<String, Double>>() {
            public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
                return p2.getValue().compareTo(p1.getValue());
            }
        });
        return sortedDocs;
    }

    private double getCosineSimilarity(HashMap<String, Integer> document, Vector<String> query) {
        HashMap<String, Integer> docTermFreq = new HashMap<>();
        for (String term : document.keySet()) {
            int freq = document.getOrDefault(term, 0);
            docTermFreq.put(term, freq);
        }

        double dotProduct = 0;
        double documentMagnitude = 0;
        double queryMagnitude = 0;
        for (String term : docTermFreq.keySet()) {
            int docTermFreqValue = docTermFreq.get(term);
            if (query.contains(term)) {
                int queryTermFreq = Collections.frequency(new ArrayList<>(query), term);
                dotProduct += queryTermFreq * docTermFreqValue;
                queryMagnitude += queryTermFreq * queryTermFreq;
            }
            documentMagnitude += docTermFreqValue * docTermFreqValue;
        }

        queryMagnitude = Math.sqrt(queryMagnitude);
        documentMagnitude = Math.sqrt(documentMagnitude);

        if (queryMagnitude == 0 || documentMagnitude == 0) {
            return 0;
        }
        return dotProduct / (queryMagnitude * documentMagnitude);
    }

    public void Calc_TF_IDF(Vector<String> query) {
        int totalDocuments = docCount;
        for (String word : query) {
            int documentsWithKeyword = 0;
            for (int i = 0; i < totalDocuments; i++) {
                Map<String, Integer> docTermFrequencies = docTermFreqs.getOrDefault(i, new HashMap<>());
                if (docTermFrequencies.containsKey(word.toLowerCase(Locale.ROOT))) {
                    documentsWithKeyword++;
                }
            }
            double idf = Math.log((double)(files.length) / documentsWithKeyword);

            DictEntry1 entry = index.getOrDefault(word.toLowerCase(Locale.ROOT), new DictEntry1());
            for (int i = 0; i < totalDocuments; i++) {
                Map<String, Integer> docTermFrequencies = docTermFreqs.getOrDefault(i, new HashMap<>());
                int termFrequency = docTermFrequencies.getOrDefault(word, 0);
                float normalizedTF = (float) termFrequency / files[i].split(" ").length;
                float tfidf = (float) (normalizedTF * idf);

                entry.doc_freq[i]++;  // Increment document frequency
                entry.pList = new Posting1(i, tfidf, entry.pList);   // Add posting to head of linked list
                index.put(word.toLowerCase(Locale.ROOT), entry);

                // Print the TF-IDF value
                System.out.println("TF-IDF for word '" + word + "' in document " + (i+1) + ": " + tfidf);
            }
        }
    }
}


