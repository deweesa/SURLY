/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */

import java.util.LinkedList;
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
   
   public Tuple parseTuple()
   {
      LinkedList<String> values = new LinkedList<>();
      String currentValue = "";
      String attributes = input.substring(input.indexOf(' '));

      int i = 0;
      while(attributes.charAt(i) != ';')
      {
          while(attributes.charAt(i) == ' ') i++; //skip spaces

          if(attributes.charAt(i) == '\''){
              i++;
              while(attributes.charAt(i) != '\'' && attributes.charAt(i) != ';')
              {
                  currentValue += attributes.charAt(i);
                  i++;
              }
              i++;

              values.add(currentValue);
              currentValue = "";
          }

          if(attributes.charAt(i) != ' ' && attributes.charAt(i) != '\'') {
              while (attributes.charAt(i) != ' ' && attributes.charAt(i) != ';') {
                  currentValue += attributes.charAt(i);
                  i++;
              }

              values.add(currentValue);
              currentValue = "";
          }
      }

      Tuple tuple = new Tuple();
      for(i = 0; i < values.size(); i ++)
      {
          AttributeValue attr = new AttributeValue(values.get(i));
          tuple.add(attr);
      }

      return tuple;
   }
}