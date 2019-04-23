/* Asa DeWeese
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class InsertParser{
   
   private String input;
   
   InsertParser(String input)
   {
      this.input = input.substring(6).trim();
   }
   
   public String parseRelationName()
   {
      return input.substring(0, input.indexOf(' '));
   }
   
   public int parseAttributeCount()
   {
      int count = 0, index = 0;
      String attr = input.substring(input.indexOf(' '));
      //System.out.println(attr);
      
      while(attr.charAt(index) != ';')
      {
         while(Character.isWhitespace(attr.charAt(index))) index++;
         count++;
         if(attr.charAt(index) == '\'')
         {
            index++;
            while(attr.charAt(index) != '\'') 
            {
               if(attr.charAt(index) == ';') return -1;
               index++;
            }
            index++;
         } else {
            while(!Character.isWhitespace(attr.charAt(index)) && attr.charAt(index) != ';')
               index++;
         }
      }
      return count;
   }

}