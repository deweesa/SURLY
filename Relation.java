import java.util.LinkedList;

/**
 * Created by malquib2 on 4/12/19.
 */
public class Relation {

   public String name;
   public LinkedList<Attribute> schema = new LinkedList();
   public LinkedList<Tuple> tuples = new LinkedList();

   Relation(String name)
   {
      this.name = name;
   }

   public void insert(Tuple tuple)
   {
      tuples.add(tuple);
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getName() {
      return name;
   }

   public LinkedList<Attribute> getSchema() {
      return schema;
   }

   public void addAttribute(Attribute attr) {
      schema.add(attr);
   }


   public void setSchema(LinkedList schema) {
      this.schema = schema;
   }


   public LinkedList<Tuple> getTuples() {
      return tuples;
   }
   public void setTuples(LinkedList tuples) {
      tuples = tuples;
   }
   public void delete(){
      tuples.clear();
   }

   public void print () throws Exception {

      String breakln = printRelation();

      String innerBreak = "";
      for(int i = 0; i < breakln.length(); i++)
      {
         innerBreak += "-";
      }

      System.out.println(innerBreak);
      printAttributes();
      System.out.println(innerBreak);
      printTuples();
      System.out.println(breakln);
      System.out.println();

   }

   public String printRelation() throws Exception {

      int totalLength = -1;
      String brk = "";
      for(int x = 0 ; x < schema.size(); x++){

         if (schema.get(x).getLength() > schema.get(x).getName().length() ){
            totalLength += schema.get(x).getLength()+1;
         }else{
            totalLength += schema.get(x).getName().length()+1;
         }
      }
      for(int x = 0; x < totalLength+2; x++)
      {
         brk += "*";
      }

      System.out.println(brk);
      System.out.print("|");
      System.out.printf("%-"+ totalLength+"s",name);
      System.out.println("|");

      return brk;
   }

   public void printAttributes() throws Exception {
      for(int y = 0 ; y < schema.size(); y++){

         int length = schema.get(y).getLength();
         String attributeName = schema.get(y).getName();

         System.out.print("|");
         System.out.printf("%-" + length + "s",attributeName);
      }

      System.out.println("|");
   }

   public void printTuples()throws  Exception {
      for (int R = 0; R < tuples.size(); R++) {

         for(int C = 0 ; C < schema.size(); C++){
            int length;
            if(schema.get(C).getLength() > schema.get(C).getName().length() ){
               length = schema.get(C).getLength();
            }else{
               length = schema.get(C).getName().length();
            }

            String value = tuples.get(R).getValue(schema.get(C).getName());

            System.out.print("|");
            System.out.printf("%-" + length + "s",value);
         }

         System.out.println("|");

      }
   }
}