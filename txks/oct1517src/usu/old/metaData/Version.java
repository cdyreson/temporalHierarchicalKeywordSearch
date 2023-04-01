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
public class Version {
   
    public Version()
    {
    }
    
    private int version_number;
    private Period version_lifeTime;
    
    
    public void set_version(int num){
        version_number = num;
    }
    
    public int get_version(){
        return version_number;
    }
    
    public void set_time(Period time){
        version_lifeTime = time;
   
    }
    
    public Period get_time(){
        return version_lifeTime;
    }
    
    public Instant get_startTime(){
        return this.version_lifeTime.getStarting();
    }
    
    public Instant get_endTime(){
        return this.version_lifeTime.getEnding();
    }
        @Override
    public String toString() {
        return "Version Number = " + Integer.toString(this.version_number)+ " LifeTime = " + this.version_lifeTime.toString()+"\n";
    }
    
    
    public int hashCode() {
        int hash_code=-1;
        String hash;
        
        hash=Integer.toString(version_number)+ Integer.toString(version_lifeTime.hashCode());
        hash_code= Integer.getInteger((hash));
        return hash_code;
    }
    
}
