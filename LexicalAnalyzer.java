/* Asa DeWeese
 * SURLY 0
 * CSCI 330, 12:00pm
 */
import java.io.File;
import java.util.Scanner;

public class LexicalAnalyzer {
   public void run(String fileName) {
      Scanner sc;
      try {
         sc = new Scanner(new File(fileName));
      } catch (Exception ex) {
         System.out.println("File "+fileName+" not found");
         return;
      }
      
      PrintParser pPrint;
      RelationParser pRelation;
      InsertParser pInsert;
      String command;
      
      //while the file has lines within it read it out, then send
      //corresponding lines to their respective parsers
      while(sc.hasNextLine()) {
         command = sc.nextLine();
         if(!command.startsWith("#") && command.length()>0)
         {
            while(!command.endsWith(";")) 
            {
               try
               {
                  command+=" "+sc.nextLine().trim();
               }
               catch(Exception e)
               {
                  return;
               }
            }
            
            if(command.startsWith("PRINT")) 
            {
               pPrint = new PrintParser(command);
               String[] relations = pPrint.parseRelationNames();
               if(relations.length > 0)
               {
                  System.out.print("Printing "+relations.length+" relations: "+relations[0]);
                  for(int i = 1; i < relations.length; i++)
                     System.out.print(", "+relations[i]);
                  System.out.print(".\n");
               }
            }
            else if(command.startsWith("INSERT"))
            {
               pInsert = new InsertParser(command);
               System.out.println("Inserting " + pInsert.parseAttributeCount() + " attributes to " + pInsert.parseRelationName() + ".");
            }
            else if(command.startsWith("RELATION"))
            {
               pRelation = new RelationParser(command);
               int count = pRelation.parseAttributeCount();
               if(count != -1)
                  System.out.println("Creating " + pRelation.parseRelationName() + " with " + count + " attributes.");
               else
                  System.out.println("Bad RELATION syntax: unmatched parens");
            }    
         }
      }               
   }
}