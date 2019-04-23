import java.util.*;
public class Relation 
{
   private String name;
   private LinkedList<Attribute> schema;
   private LinkedList<Tuple> tuples;
   
   public String getName()
   {
      return name;
   }
   
   public void print()
   {
      System.out.println("WORK IN PROGRESS");
   }
   
   public void insert(Tuple tuple)
   {
      tuples.add(tuple);
      return;
   }
   
   public void delete()
   {
      tuples.clear();
   }
}