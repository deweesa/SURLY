import java.util.*;
public class Relation 
{
   private String name;
   private LinkedList<Attribute> schema = new LinkedList<>();
   private LinkedList<Tuple> tuples;
   
   public String getName()
   {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public LinkedList<Attribute> getSchema() {
      return schema;
   }

   public void setSchema(LinkedList<Attribute> schema) {
      this.schema = schema;
   }

   public void addSchema(Attribute attribute)
   {
      schema.add(attribute);
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

   public LinkedList<Tuple> getTuples() {
      return tuples;
   }

   public void delete()
   {
      tuples.clear();
   }
}