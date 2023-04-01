/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.metaData;

import usu.temporal.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import usu.NodeId;
import usu.PathId;

/**
 *
 * @author Amani Shatnawi
 */
public class NodeMetaData {
     // forward backword chaine
    private List<TDLN> DLNi_Chain = new ArrayList<TDLN>();
    
    // version list in history document
    private List<Version> hVersions = new ArrayList<Version>();
    
    // version list in the Item document
    private List<Version> iVersions = new ArrayList<Version>();
    
    //dynamic level numbering for history document
    private TDLN DLNh = new TDLN();
    
    private DLNi dlni;
    
    
    
    public NodeMetaData() {
    }

    public NodeMetaData(String str) {
        // split the string and save the correct values
    }
    
    public void addNodeId(NodeId n, PathId p) {
        
    }
    public TDLN get_chain(int index){
        return this.DLNi_Chain.get(index);
    }
    
    public void addToHistoryVersionList(Version verion){
        this.hVersions.add(verion);
    }
    
    public List<Version> getHistoryVersionList(){
        return this.hVersions;
    }
    
    public Version get_hVersion(int index){
        return this.hVersions.get(index);
    }
    
    public void addToItemVersionList(Version verion){
        this.iVersions.add(verion);
    }
    
    public List<Version> getItemVersionList(){
        return this.iVersions;
    }
    
    public Version get_iVersion(int index){
        return this.iVersions.get(index);
    }
    
    public void set_DLNh(TDLN dln){
        DLNh.set_DLN(dln.get_DLN());
        DLNh.set_lifeTime(dln.getLifeTime());
    }
   
    
    public TDLN get_DLNh()
    {
        return this.DLNh;
    }
   
     public void set_DLNi(TDLN dln){
        //DLNi.set_DLN(dln.get_DLN());
        //DLNi.set_lifeTime(dln.getLifeTime());
    }
   
    
    public DLNi get_DLNi()
    {
        return this.dlni;
    }
    
    
    public boolean deleteNode(Instant endingInstant){
        boolean result = false;
        int last_index = hVersions.size();
        Version v;
        //v = new Version(endingInstant, endingInstant);
                
               //hVersions.add(last_index,v);
        
        return result;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        for (int i=0;i <hVersions.size();i++)
        {
            hash ^= hVersions.get(i).hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NodeMetaData other = (NodeMetaData) obj;
        if (!Objects.equals(this.hVersions, other.hVersions)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "NodeMetaData{" + "History versions=" + this.hVersions.toString() + ", Item versions=" + this.iVersions.toString()+"}";
    }
}
