/* Asa DeWeese
 * SURLY 0
 * CSCI 330, 12:00pm
 */
import java.io.File;
import java.util.Scanner;

public class LexicalAnalyzer {
   
   private SurlyDatabase database;
   
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
      DeleteParser pDelete;
      DestroyParser pDestroy;
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
               String name = pInsert.parserelationName();
               Relation relation = database.getRelationName(name);
               System.out.println("Inserting " + pInsert.parseAttributeCount() + " attributes to " + name + ".");
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
            else if(command.startsWith("DELETE"))
            {
               pDelete = new DeleteParser(command);
               Relation relation = database.get(pDelete.parseRelationName());
               relation.clear();
         }
      }               
   }
}