/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usu.metaData;

import usu.temporal.Period;
import usu.temporal.Instant;
import javax.print.attribute.DateTimeSyntax;

/**
 *
 * @author Amani Shatnawi
 */
public class TDLN {
    
    public TDLN()
    {
    }
    
    
     public TDLN(String dln, Period p)
    {
        this.DLN=dln;
        this.lifeTime=p;
    }
    
     
    private String DLN;
    private Period lifeTime;
   
    public void set_DLN(String dln){
        this.DLN = dln;
    }
    
    public String get_DLN(){
        return this.DLN;
    }
    
    
    public void set_lifeTime(Period time){
        this.lifeTime = time;
        
    }
           
    public Instant get_startTime(){
        return this.lifeTime.getStarting();
    }
    
   
    public Instant get_endTime(){
        return this.lifeTime.getEnding();
    }

    public Period getLifeTime() {
        return this.lifeTime;
    }
    
}
