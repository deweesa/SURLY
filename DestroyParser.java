/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class DestroyParser {

   private String input;

   public DestroyParser(String input) {

      this.input = input;
   }

   public String parseRelationName(){
      String temp = "[ ]+";

      String[] out = input.split(temp);

      out[1] = out[1].substring(0,out[1].length()-1);

      return out [1];
   }
}