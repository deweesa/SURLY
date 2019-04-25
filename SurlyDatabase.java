import java.util.*;
public class SurlyDatabase
{
   private LinkedList<Relation> relations;
   
   public Relation getRelation(String name) throws Exception
   {
      int index = indexOf(name);

      if(indexOf(name) == -1)
      {
         String message = "ERROR: Relation of name "+name+" does not exist.\n";
         message +=       "     Destruction failed";
         throw new Exception(message);
      }
      
      return relations.get(index);
   }
   
   public void destroyRelation(String name)
   {
      int index = indexOf(name);

      if(indexOf(name) != -1)
      {
         System.out.println("ERROR: Relation of name "+name+" does not exist.");
         System.out.println("     Destruction failed");
         return;
      }

      relations.remove(index);
      
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
      
      relations.push(relation);
   }

   private int indexOf(String name)
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
}