/* Asa DeWeese
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class PrintParser {
   
   private String input;
   
   PrintParser(String input)
   {
      this.input = input.substring(5).trim();
   }
   
   public String[] parseRelationNames()
   {
      String[] result = {"TEST"};
      result = input.split(", ");
      result[result.length-1] = result[result.length-1].replace(";", "");
      return result;
   }

}