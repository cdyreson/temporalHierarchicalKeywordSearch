/*
 * A parser exception
 */

package usu.grammar;
import org.antlr.runtime.RecognitionException;

/**
 *
 * @author Curtis Dyreson
 */
public class TXKSParseException extends RecognitionException {
   public String msg = "";
   
   public TXKSParseException(String msg) {
       this.msg = msg;
   }
   
}
