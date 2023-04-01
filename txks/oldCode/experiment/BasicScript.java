package messiah.experiment;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import messiah.Main;
import messiah.search.SearchAlgoEnum;
import messiah.search.generic.SearchResult;

/**
 *
 * @author truongbaquan
 */
public class BasicScript {

    private static final int RUN_TIMES = 20;
    private static final int RELOAD_TIMES = 4;
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String folder = "C:/Documents and Settings/truongbaquan/My Documents/Research/Research/XmlScripts/XmlAnalyzer/";
        Main controller = new Main();
        
        String[] datasets = new String[] { "mondial" };
        String[] queries = new String[] {
            "London",
            "California Arizona",
            "country Muslim",
            "country name Laos",
            "Fresno longitude",
            "York latitude",
            "city longitude latitude",
            "river Lualaba area length",
        };
        
//        String[] datasets = new String[] { "interpro" };
//        String[] queries = new String[] {
//            "Kringle",
//            "Kringle Domain",
//            "journal Nature",
//            "publication 2002 Science",
//            "Filaggrin parent_list",
//            "publication journal year",
//            "Desmoglein interpro Pemphigus class_list",
//            "Peptidase S1A prothrombin Patthy sec_list",
//        };

//        String[] datasets = new String[] { "dblp" };
//        String[] queries = new String[] {
//            "XML",
//            "Torsten Grust",
//            "Aradhye title",
//            "XML Keyword Search inproceedings",
//            "inproceedings cite",
//            "Aradhye inproceedings cite",
//            "volume XML book",
//            "XQuery XIME proceedings cite",
//        };
        
//        String[] datasets = new String[] { "dblp60" };
//        String[] queries = new String[] {
//            "XML",
//            "XML title",
//            "XML title inproceedings",
//            "XML title inproceedings author",
//            "XML title inproceedings author Torsten",
//            "XML title inproceedings author Torsten Grust",
//            "XML title inproceedings author Torsten Grust 2007",
//        };

//        String[] datasets = new String[] { "interpro-80" , "interpro-60", "interpro-40", "interpro-20", "interpro"};
//        String[] queries = new String[] {
//            "interpro name"
//        };

//        String[] datasets = new String[] { "dblp" , "dblp20", "dblp40", "dblp60", "dblp80"};
//        String[] queries = new String[] {
//            "inproceedings cite"
//        };

        
        
//        String[] datasets = new String[] { "shakespeare"};
//        String[] queries = new String[] {
//            "Hamlet",
//            "Juliet speech",
//            "to be or not to be speaker",
//            "wherefore art thou Romeo Act",
//            "King Lear Tragedy prologue",
//            "play title subtitle",
//            "Othello Act IV epilogue",
//            "Troilus Cressida Act VI prologue epilogue",
//        };
        
        for (String dataset : datasets) {
            if (controller.isDatasetCreated(dataset)) {
                runQuery(controller, dataset, queries);
            } else {
                controller.parseDataset(dataset, new File(folder + dataset + ".xml"), null);
            }
        }
    }

    public static void runQuery(Main controller, String dataset, String... queries) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("output.csv", true)))) {
            writer.println("Query,Result Size,Average Time");
            for (String query : queries) {
                double sum = 0; int size = 0;
                for (int j = 0; j < RELOAD_TIMES; j++) {
                    controller.loadDataset(dataset);
                    for (int i = 0; i < RUN_TIMES; i++) {
                        SearchResult result = controller.search(query, SearchAlgoEnum.CompleteFSLCA, false);
                        if (i == 0 && j == 0) {
                            System.out.println("Result Size = " + result.getSize());
                            size = result.getSize();
                        }
                        System.out.println(result.getSearchTime() + "ms");
                        sum += result.getSearchTime();
                        System.gc();
                    }
                    controller.getCurDataset().close();
                }
                System.out.println("Average Time = " + (sum / (RELOAD_TIMES * RUN_TIMES)) + "ms");
                writer.println(query + "," + size + "," + ((sum / (RELOAD_TIMES * RUN_TIMES))));
                System.out.println("=========================================================");
            }
        } catch (IOException ex) {
            Logger.getLogger(BasicScript.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void loadFiles(Main controller, String[] filenames) {
        for (String filename : filenames) {
            int slashIndex = filename.lastIndexOf('/');
            int periodIndex = filename.lastIndexOf('.');
            String dataset = filename.substring(slashIndex + 1, periodIndex);
            controller.parseDataset(dataset, new File(filename), null);
        }
    }
}
