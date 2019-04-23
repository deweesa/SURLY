/* Asa DeWeese
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class RelationParser {
   
   private String input;
   
   RelationParser(String input)
   {
      this.input = input.substring(8).trim();
   }
   
   public String parseRelationName()
   {
      return input.substring(0, input.indexOf(' '));
   }
   
   public int parseAttributeCount()
   {
      if(input.indexOf('(') == -1 || input.indexOf(')') == -1) return -1;
      String attrList = input.substring(input.indexOf('('));
      return attrList.split(",").length;
   }
   
   
}