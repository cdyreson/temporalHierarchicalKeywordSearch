package messiah.search.resultbuilder;

import java.util.Set;
import javax.swing.JTree;
import usu.NodeId;

/**
 *
 * @author truongbaquan
 */
public abstract class ResultBuilder {
    
    public abstract JTree buildResultTree(Set<NodeId> resultSet, String[] keywordSet);
    
}
