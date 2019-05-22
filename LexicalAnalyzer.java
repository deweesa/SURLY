/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
import java.io.File;
import java.util.Scanner;


public class LexicalAnalyzer {
   
   private SurlyDatabase database = new SurlyDatabase();
   
   public void run(String fileName) {
      Scanner sc;
      try {
         sc = new Scanner(new File(fileName));
      } catch (Exception ex) {
         System.out.println("File "+fileName+" not found");
         return;
      }

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
               Print(command);
            }
            else if(command.startsWith("INSERT"))
            {
               Insert(command);
            }
            else if(command.startsWith("RELATION"))
            {
               Relation(command);
            } 
            else if(command.startsWith("DELETE")) {
               Delete(command);
            }

            else if(command.startsWith("DESTROY")) {
               Destroy(command);
            }
         }
      }               
   }

   private void Relation(String command)
   {
      RelationParser pRelation = new RelationParser(command);
      int count = pRelation.parseAttributeCount();
      if(count != -1) {
         Relation relation = pRelation.parseRelation();

         if(relation == null)
         {
            return;
         }

         database.createRelation(relation);

         Relation catalog = database.getCatalog();
         Tuple catalogTuple = new Tuple();
         catalogTuple.add(new AttributeValue("RELATION", pRelation.parseRelationName()));
         catalogTuple.add(new AttributeValue("ATTRIBUTES", Integer.toString(count)));
         catalog.insert(catalogTuple);
      }
      else {
         System.out.println("Bad RELATION syntax: unmatched parens");
      }
   }

   private void Insert(String command)
   {
      InsertParser pInsert = new InsertParser(command);
      Tuple tuple = pInsert.parseTuple();
      String name = pInsert.parseRelationName();
      Relation relation = null;
      try
      {
         relation = database.getRelation(name);
      } catch (Exception e) {
         System.out.println(e);
         return;
      }

      if(relation.getSchema().size() != tuple.size())
      {
         System.out.println("Bad INSERT syntax: tuple count and relation attribute count mismatch");
         return;
      }

      boolean add = true;
      for(int i = 0; i < relation.getSchema().size(); i++)
      {
         tuple.get(i).setName(relation.getSchema().get(i).getName());
         if(relation.getSchema().get(i).getDatatype().equals("NUM"))
         {
            String value = tuple.getValue(relation.getSchema().get(i).getName());
            if(!value.matches("[0-9]+"))
            {
               add = false;
            }
         }
      }

      if (add) {
         tuple.setNames(relation);
         relation.insert(tuple);
      }
   }

   private void Delete(String command)
   {
      DeleteParser pDelete = new DeleteParser(command);

      String name = pDelete.parseRelationName();

      Relation relation = null;
      try
      {
         relation = database.getRelation(name);
      } catch (Exception e)
      {
         System.out.println(e);
      }

      if(relation != null)
      {
         relation.delete();
      }
   }

   private void Destroy(String command)
   {
      DestroyParser pDestroy = new DestroyParser(command);
      String name = pDestroy.parseRelationName();

      database.destroyRelation(name);
   }

   private void Print(String command)
   {
      PrintParser pPrint = new PrintParser(command);
      String[] relations = pPrint.parseRelationNames();

      for(int i = 1; i < relations.length; i++)
      {
         Relation relation;
         try
         {
            relation = database.getRelation(relations[i]);
            relation.print();
         } catch (Exception e) {
            System.out.println(e);
         }
      }
   }
}