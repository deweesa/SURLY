/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
import java.util.*;
public class SurlyDatabase
{
   private LinkedList<Relation> relations = new LinkedList<>();
   private LinkedList<Relation> tempRelations = new LinkedList<>();

   SurlyDatabase()
   {
      Attribute relation = new Attribute("RELATION", "CHAR", 10);
      Attribute attributes = new Attribute("ATTRIBUTES", "NUM", 10);
      Relation catalog = new Relation("CATALOG");

      catalog.addAttribute(relation);
      catalog.addAttribute(attributes);
      relations.add(catalog);
   }
   
   public Relation getRelation(String name) throws Exception
   {
      int index = indexOf(name);

      if(index != -1)
      {
         return relations.get(index);
      }

      index = indexOfTemp(name);

      if(index == -1) {
         String message = "ERROR: Relation of name " + name + " does not exist.\n";
         message += "     Get failed";
         throw new Exception(message);
      }

      return tempRelations.get(index);

   }

   public Relation getBaseRelation(String name) throws Exception{
      int index = indexOf(name);
      if(index != -1){
         return relations.get(index);
      }else {
         return null;
      }
   }
   
   public void destroyRelation(String name)
   {
      int index = indexOf(name);

      if(index == -1)
      {
         System.out.println("ERROR: Relation of name "+name+" does not exist.");
         System.out.println("     Destruction failed");
         return;
      }

      relations.remove(index);
      Relation catalog = relations.get(0);
      LinkedList<Tuple> tuple = catalog.getTuples();

      for(int i = 0; i < tuple.size(); i++)
      {
         if(tuple.get(i).getValue("RELATION").equals(name))
         {
            tuple.remove(i);
         }
      }

      
   }
   
   public void createRelation(Relation relation)
   {
      String name = relation.getName();

      if(indexOf(name) != -1)
      {
         System.out.println("ERROR: Relation of same name "+name+" already exists.");
         System.out.println("     Relation failed");
         return;
      }
      
      relations.add(relation);
   }

   public void createTempRelation(Relation relation)
   {
      String name = relation.getName();

      if(indexOfTemp(name) != -1)
      {
         System.out.println("ERROR: Relation of same name "+name+" already exists.");
         System.out.println("     Relation failed");
         return;
      }

      tempRelations.add(relation);
   }

   public int indexOf(String name)
   {
      int size = relations.size();

      for(int i = 0; i < size; i++)
      {
         if(relations.get(i).getName().equals(name))
         {
            return i;
         }
      }

      return -1;
   }

   public int indexOfTemp(String name)
   {
      int size = tempRelations.size();

      for(int i = 0; i < size; i++)
      {
         if(tempRelations.get(i).getName().equals(name))
         {
            return i;
         }
      }

      return -1;
   }

   public Relation getCatalog()
   {
      return relations.get(0);
   }
}