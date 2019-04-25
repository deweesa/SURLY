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
               try {
                  printTable(relations);
               } catch(Exception e) {
                  System.out.println(e);
               }
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
               Tuple tuple = pInsert.parseTuple();
               String name = pInsert.parseRelationName();
               //Relation relation = database.getRelationName(name);
               System.out.println("Inserting " + pInsert.parseAttributeCount() + " attributes to " + name + ".");
               Relation relation;
               try
               {
                  relation = database.getRelation(name);
               } catch (Exception e) {
                  System.out.println(e);
                  break;
               }

               if(relation.getSchema().size() != tuple.size())
               {
                  System.out.println("Bad INSERT syntax: tuple count and relation attribute count mismatch");
               }

               //tuple.setNames(relation);

               System.out.println(tuple);
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
            else if(command.startsWith("DELETE")) {
               //pDelete = new DeleteParser(command);
               //Relation relation = database.get(pDelete.parseRelationName());
               //relation.clear();
            }
         }
      }               
   }

   public void printTable(String [] names) throws Exception {

      for (int i = 0; i< names.length ; i++){

         printRelation(names[i]);
         printAttributes(names[i]);
         printTuples(names[i]);

      }

   }
   public void printRelation(String name) throws Exception {

      int totalLength = 0;

      for(int x = 0 ; x < database.getRelation(name).getSchema().size(); x++){

         totalLength += database.getRelation(name).getSchema().get(x).getLength();

      }

      System.out.print("|");
      System.out.printf("%-"+ totalLength+"s",name);
      System.out.println("|");
   }


   public void printAttributes(String name) throws Exception {
      for(int y = 0 ; y < database.getRelation(name).getSchema().size(); y++){

         int length = database.getRelation(name).getSchema().get(y).getLength();
         String attributeName = database.getRelation(name).getSchema().get(y).getName();

         System.out.print("|");
         System.out.printf("%-" + length + "s",attributeName);
      }

      System.out.println("|");
   }


   public void printTuples(String name)throws Exception {
      for (int R = 0; R < database.getRelation(name).getTuples().size(); R++) {

         for(int C = 0 ; C < database.getRelation(name).getSchema().size(); C++){

            int length = database.getRelation(name).getSchema().get(C).getLength();
            String value = database.getRelation(name).getTuples().get(R).getValue(database.getRelation(name).getSchema().get(C).getName());

            System.out.print("|");
            System.out.printf("%-" + length + "s",value);
         }

         System.out.println("|");



      }
   }
}