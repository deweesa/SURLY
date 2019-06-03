import java.util.*;
import java.io.*;
/**
 * Created by malquib2 on 4/4/19.
 */
public class PrintParser {

   private String input;

   public PrintParser(String input){
      this.input = input;
   }

   public String[] parseRelationNames() {
      String temp = "[ ]+";

      String[] names = input.split(temp);

      for(int i = 1; i < names.length; i++)
      {
         names[i] = names[i].substring(0, names[i].length()-1);
      }

      return names;
   }
}