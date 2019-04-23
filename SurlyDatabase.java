import java.util.*;
public class SurlyDatabase
{
   private LinkedList<Relation> relations;
   
   public Relation getRelation(String name)
   {
      Relation R = new Relation();
      int size = relations.size();
      for(int i = 0; i < size; i++)
      {
         if(relations.get(i).getName().equals(name))
         { 
            R = relations.get(i);
         }
      }
      
      return R;
   }
   
   public void destroyRelation(String name)
   {
      int size = realtions.size();
      for(int i = 0; i < size; i++)
      {
         if(relations.get(i).getname().equals(name))
         {
            realtions.get(i).remove(i);
         }
      }
      
   }
   
   public void createRelation(Relation relation)
   {
      int size = relations.size();
      for(int i = 0; i < size; i++)
      {
         if(relations.get(i).getName().equals(name))
         {
            relations.get(i).
   }
}