import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String[] files = {"file1.txt", "file2.txt", "file3.txt", "file4.txt", "file5.txt", "file6.txt", "file7.txt", "file8.txt", "file9.txt", "file10.txt"};

        // Create a new PositionalIndex
        PositionalIndex index = new PositionalIndex();

        // Add documents to the index
        for (int i = 0; i < files.length; i++) {
            String file = files[i];
            try {
                String content = new String(Files.readAllBytes(Paths.get(file)));
                String[] terms = content.split("\\s+");
                index.addDocument(i, terms);
            } catch (IOException e) {
                System.err.println("Error reading file: " + file);
            }
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter one or more words to search for (separated by spaces): ");
        String words = scanner.nextLine();
        String[] query = words.split(" ");
        Set<Integer> result = index.search(query);
        System.out.println("Documents where the query terms appear in sequential order: " + result);
        System.out.print("Enter one or more words to rank files by cosine similarity (separated by spaces): ");

        InvertedIndex index1 = new InvertedIndex(files);
      Vector  words1 = new Vector<>(Arrays.asList(scanner.nextLine().split(" ")));
        List<Pair<String, Double>> rankedFiles = index1.rankFilesByCosineSimilarity(words1);
        System.out.println("Files ranked by cosine similarity to query:");
        Vector<String> result1 = new Vector<>();
       Vector <Pair<String, Double>> pair1 = new Vector<>();
        for (Pair<String, Double> pair : rankedFiles) {

            char i = pair.getKey().charAt(4);
            char i1 = pair.getKey().charAt(5);
            String s = "";
            if(i1!='.'){
                 s = String.valueOf(i) + String.valueOf(i1);
            }
            else{
                 s = String.valueOf(i);
            }
            int j = Integer.parseInt(s);
            if (result.contains(j)) {
                pair1.add(pair);
            }
            else{
                result1.add(pair.getKey() + " (cosine similarity = " + 0.0 + ")");
            }
        }
        Comparator<Pair<String, Double>> comparator = new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> p1, Pair<String, Double> p2) {
                return Double.compare(p2.getValue(), p1.getValue());
            }
        };

// Sort the vector using the custom comparator
        Collections.sort(pair1, comparator);
        for (Pair<String, Double> pair : pair1) {
         System.out.println(pair.getKey() + " (cosine similarity = " + pair.getValue() + ")");
        }
        for(String s: result1){
            System.out.println(s);
        }
        System.out.print("Enter one or more words to Calculate TF_IDF for each document (separated by spaces): ");
        Vector<String> words2 = new Vector<>(Arrays.asList(scanner.nextLine().split(" ")));
        index1.Calc_TF_IDF(words2);

    }

}



