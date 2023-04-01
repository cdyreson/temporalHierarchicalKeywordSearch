/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messiah.experiment;

import messiah.storage.BdbDataset;
import com.sleepycat.je.DatabaseException;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.parse.generic.KeywordIndexBuilder;
import messiah.KeywordInfo;
import messiah.Config;
import messiah.utils.AttributeSorter;
import usu.PathId;
import usu.NodeId;
import usu.dln.NodeIdDLNBinding;
import usu.dln.PathIdDLNBinding;

/**
 *
 * @author truongbaquan
 */
public class FreqScript {

    public static void main(String... args) {
        getFreqs();
//        createFreqFile();
    }

    public static void getFreqs() {
        String[] queries = new String[]{
            "Hamlet",
            "Romeo Juliet",
            "to be or not to be speaker",
            "wherefore art thou Romeo Act",
            "The Tragedy of King Lear prologue",
            "play title subtitle",
            "Antony Cleopatra epilogue",
            "That which we call a rose Act Epilogue"
        };

        HashSet<String> tokens = new HashSet<String>();
        for (String query : queries) {
            String[] split = query.split(Config.TOKENS_DELIMITER);
            for (String str : split) {
                tokens.add(str.toLowerCase());
            }
        }

        PrintWriter writer = null;
        try {
            BdbDataset bdb = new BdbDataset("shakespeare", new NodeIdDLNBinding(), new PathIdDLNBinding(), false);
            KeywordIndex index = new KeywordIndex(bdb);
            TypeIndex typeIndex = new TypeIndex(bdb);
            Map<PathId,TreeSet> typeMap = typeIndex.wrapUnsortedMap(false);
            Map<String, KeywordInfo> map = index.wrapUnsortedMap(false);
            AttributeSorter<String, Integer> sorter = new AttributeSorter();
            for (Map.Entry<String, KeywordInfo> entry : map.entrySet()) {
                sorter.add(entry.getKey(), entry.getValue().getNodeIds().size());
            }
            writer = new PrintWriter(new BufferedWriter(new FileWriter("freq_output.csv")));
            int noSize = sorter.getResults();
            for (int i = 0; i < noSize; i++) {
                String keyword = sorter.getObj(i);
                if (tokens.contains(keyword)) {
                    int freq = sorter.getScore(i);
                    double percentile = ((double) i) * 100 / noSize;
                    int typeFreq = 0;
                Collection<PathId> typeIds = map.get(keyword).getPathIds();
                for (PathId typeId : typeIds) {
                    typeFreq += typeMap.get(typeId).size();
                }
                System.out.println(keyword + "\t" + freq + "\t" + percentile + "\t" + typeFreq);
                writer.println(keyword + "," + freq + "," + percentile + "," + typeFreq);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FreqScript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DatabaseException ex) {
            Logger.getLogger(FreqScript.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    public static void createFreqFile() {
        PrintWriter writer = null;
        try {
            BdbDataset bdb = new BdbDataset("shakesphere", new NodeIdDLNBinding(), new PathIdDLNBinding(), false);
            KeywordIndex index = new KeywordIndex(bdb);
            TypeIndex typeIndex = new TypeIndex(bdb);
            Map<PathId,TreeSet> typeMap = typeIndex.wrapUnsortedMap(false);
            Map<String, KeywordInfo> map = index.wrapUnsortedMap(false);
            AttributeSorter<String, Integer> sorter = new AttributeSorter();
            for (Map.Entry<String, KeywordInfo> entry : map.entrySet()) {
                sorter.add(entry.getKey(), entry.getValue().getNodeIds().size());
            }
            writer = new PrintWriter(new BufferedWriter(new FileWriter("freq.csv")));
            int noSize = sorter.getResults();
            for (int i = 0; i < noSize; i++) {
                String keyword = sorter.getObj(i);
                int freq = sorter.getScore(i);
                double percentile = ((double) i) * 100 / noSize;
                int typeFreq = 0;
                Collection<PathId> typeIds = map.get(keyword).getPathIds();
                for (PathId typeId : typeIds) {
                    typeFreq += typeMap.get(typeId).size();
                }
                System.out.println(keyword + "\t" + freq + "\t" + percentile + "\t" + typeFreq);
                writer.println(keyword + "," + freq + "," + percentile + "," + typeFreq);
            }
        } catch (IOException ex) {
            Logger.getLogger(FreqScript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DatabaseException ex) {
            Logger.getLogger(FreqScript.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
}
