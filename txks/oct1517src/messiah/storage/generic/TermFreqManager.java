package messiah.storage.generic;

//import messiah.parse.generic.TermIndex;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import messiah.database.Database;

/**
 * This manager is in charge of managing (loading and saving) the frequency of every term
 * in a dataset. At one point, at most one dataset is active.
 * @author truongbaquan
 */
public final class TermFreqManager {
    
    private static String curDataset;

    public static String getCurDataset() {
        return curDataset;
    }
    
    private static LinkedHashMap<String, Integer> termFreqs = new LinkedHashMap<>(25000);
    
    public static int getFreq(String term) {
        if (termFreqs == null) throw new IllegalStateException("The term frequency has not been loaded.");
        Integer freq = termFreqs.get(term);
        return (freq != null) ? freq : 0;
    }
    
    public static void readFromBdb(Database db) {
        curDataset = db.getDatasetName();
        
        for (String term : db.termIndex.keySet()) {
            SortedMap nodes = db.termIndex.get(term);
            termFreqs.put(term, nodes.size());
        }
        System.out.println("Loading term frequencies from DB complete!");
    }
    
    
    public static void readFromCsv(File file) throws IOException {
        // check input
        if (!file.exists()) throw new FileNotFoundException("The CSV freq file is not found!");
        if (!file.isFile()) throw new IllegalArgumentException("The input file is not a file!");
        String filename = file.getName();
        filename = filename.toLowerCase();
        if (!filename.endsWith(".csv")) throw new IllegalArgumentException("The input file is not a CSV file!");
        filename = filename.substring(0, filename.lastIndexOf('.')); // remove extension
        if (!filename.endsWith("_freq")) throw new IllegalArgumentException("The input file is not a frequency file!");
        // get dataset name
        curDataset = filename.substring(0, filename.lastIndexOf("_"));
        // clear current cache
        termFreqs.clear();
        // load
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length != 2) {
                    System.err.println("Error line: " + line);
                    continue;
                } else
                    termFreqs.put(tokens[0], Integer.parseInt(tokens[1]));
            }
        }
        System.out.println("Loading term frequencies from CSV file complete!");
    }
    
    public static void writeToCsv(File file) throws IOException {
        // check input
        String filename = file.getName();
        filename = filename.toLowerCase();
        if (!filename.endsWith(".csv")) throw new IllegalArgumentException("The output file is not a CSV file!");
        //filename = filename.substring(0, filename.lastIndexOf('.')); // remove extension
        //if (!filename.endsWith("_freq")) throw new IllegalArgumentException("The output file is not a frequency file!");
        // load
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            for (String term : termFreqs.keySet()) {
                int freq = termFreqs.get(term);
                writer.println(term + "," + freq);
            }
        }
        System.out.println("Saving term frequencies to CSV file complete!");
    }
}
