import org.w3c.dom.Attr;

import java.util.LinkedList;

/**
 * Created by malquib2 on 4/12/19.
 */
public class Relation implements Cloneable{

   private String name;
   private LinkedList<Attribute> schema = new LinkedList();
   private LinkedList<Tuple> tuples = new LinkedList();

   Relation(String name)
   {
      this.name = name;
   }

   public void add(Tuple tuple)
   {
      tuples.add(tuple);
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }

   public LinkedList<Attribute> copySchema(){
      LinkedList<Attribute> copy = new LinkedList<>();
      for (int i = 0 ; i < schema.size() ; i++){
         try {
            copy.add((Attribute) schema.get(i).clone());
         } catch (Exception e) {
            System.out.print(e);
         }
      }
      return copy;
   }

   public LinkedList<Tuple> copyTuples(){
      LinkedList<Tuple> copy = new LinkedList<Tuple>();
      for (int i = 0 ; i < tuples.size() ; i++){
         copy.add(tuples.get(i));
      }
      return copy;
   }

   public LinkedList<Attribute> getSchema() {
      return schema;
   }

   public int getColumnIndex(String name){
      for(int i = 0; i < schema.size(); i++) {
         Attribute attr = schema.get(i);
         if(name.equals(attr.getName())) return i;
      }

      return -1;
   }

   public void updateNames(String leftTable, String rightTable){
      String name_one, name_two;
      for(int i = 0; i < schema.size(); i++){
         for(int j = i+1; j < schema.size(); j++){
            name_one = schema.get(i).getName();
            name_two = schema.get(j).getName();
            if(name_one.equals(name_two)){
               name_one = leftTable+"."+name_one;
               name_two = rightTable+"."+name_two;
               Attribute attribute_one = schema.get(i);
               Attribute attribute_two = schema.get(j);
               attribute_one.setName(name_one);
               attribute_two.setName(name_two);
               for(int k = 0; k < tuples.size(); k++)
               {
                  Tuple tuple = tuples.get(k);
                  AttributeValue attributeValue_one = tuple.get(i);
                  AttributeValue attributeValue_two = tuple.get(j);
                  attributeValue_one.setName(name_one);
                  attributeValue_two.setName(name_two);
               }
            }
         }
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public void removeLastAttribute(String atName){
      int index = -1;

      for(int i = schema.size()-1 ; i > 0; i--){
         if(schema.get(i).getName().equals(atName)){
            index = i;
         }
      }

      if(index != -1){
         schema.remove(index);
         for(int x = 0 ; x< tuples.size() ; x++){
            tuples.get(x).remove(index);
         }
      }
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
      this.tuples = tuples;
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
         Attribute attribute = schema.get(x);
         String attributeName = attribute.getName();
         if (attribute.getLength() > attributeName.length() ){
            totalLength += attribute.getLength()+1;
         }else{
            totalLength += attributeName.length()+1;
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
         Attribute attribute = schema.get(y);
         int length = attribute.getLength();
         String attributeName = attribute.getName();

         System.out.print("|");
         System.out.printf("%-" + length + "s",attributeName);
      }

      System.out.println("|");
   }

   public void printTuples()throws  Exception {
      for (int R = 0; R < tuples.size(); R++) {

         for(int C = 0 ; C < schema.size(); C++){
            int length;
            Attribute attribute = schema.get(C);
            String attributeName = attribute.getName();
            if(attribute.getLength() > attributeName.length() ){
               length = attribute.getLength();
            }else{
               length = attributeName.length();
            }

            Tuple tuple = tuples.get(R);
            AttributeValue attributeValue = tuple.get(C);
            String value = attributeValue.getValue();

            System.out.print("|");
            System.out.printf("%-" + length + "s",value);
         }

         System.out.println("|");

      }
   }

   public void delete(String[][] deleteStack){

      for (int i = tuples.size()-1 ; i >= 0  ; i--){
         if (checkIfDelete(tuples.get(i),deleteStack)){
            tuples.remove(i);
         }
      }

   }

   public boolean checkIfDelete(Tuple test , String[][] deleteStack){

      String [] temp;

      for(int x = 0 ; x < deleteStack.length ; x++){
         boolean fullAndCheck = true;
         for(int y = 0 ; y < deleteStack[x].length ; y++){
            if (deleteStack[x][y].contains(" = ")){

               temp = deleteStack[x][y].split("=");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);

               String value = test.getValue(temp[0]);

               if (value.equals(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }


            }else if(deleteStack[x][y].contains(" != ")){

               temp = deleteStack[x][y].split("!=");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);

               String value = test.getValue(temp[0]);

               if(!value.equals(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }


            }else if(deleteStack[x][y].contains(" < ")){

               temp = deleteStack[x][y].split("<");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);

               String value = test.getValue(temp[0]);

               int castToInt = Integer.parseInt(value);

               if(castToInt < Integer.parseInt(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }


            }else if(deleteStack[x][y].contains(" > ")){
               temp = deleteStack[x][y].split(">");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);

               String value = test.getValue(temp[0]);

               int castToInt = Integer.parseInt(value);

               if(castToInt > Integer.parseInt(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }

            }else if(deleteStack[x][y].contains(" <= ")){

               temp = deleteStack[x][y].split("<=");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);

               String value = test.getValue(temp[0]);

               int castToInt = Integer.parseInt(value);

               if(castToInt <= Integer.parseInt(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }

            }else if(deleteStack[x][y].contains(" >= ")) {
               temp = deleteStack[x][y].split(">=");
               temp[0] = temp[0].substring(1, temp[0].length() - 1);
               temp[1] = temp[1].substring(1, temp[1].length() - 1);

               String value = test.getValue(temp[0]);

               int castToInt = Integer.parseInt(value);

               if (castToInt >= Integer.parseInt(temp[1]) && fullAndCheck) {
                  fullAndCheck = true;
               } else {
                  fullAndCheck = false;
               }
            }
         }
         if(fullAndCheck){
            return fullAndCheck;
         }
      }
      return false;
   }

   public void select(String[][] selectStack) {
      for (int i = tuples.size()-1 ; i >= 0  ; i--){
         if (!checkIfSelect(tuples.get(i),selectStack)){
            tuples.remove(i);
         }
      }
   }
   public boolean checkIfSelect(Tuple test , String[][] selectStack){
      String [] temp;
      for(int x = 0 ; x < selectStack.length ; x++){
         boolean fullAndCheck = true;
         for(int y = 0 ; y < selectStack[x].length ; y++){
            if (selectStack[x][y].contains(" = ")){
               temp = selectStack[x][y].split("=");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);
               String value = test.getValue(temp[0]);
               if (value.equals(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }
            }else if(selectStack[x][y].contains(" != ")){
               temp = selectStack[x][y].split("!=");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);
               String value = test.getValue(temp[0]);
               if(!value.equals(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }
            }else if(selectStack[x][y].contains(" < ")){
               temp = selectStack[x][y].split("<");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);
               String value = test.getValue(temp[0]);
               int castToInt = Integer.parseInt(value);
               if(castToInt < Integer.parseInt(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }
            }else if(selectStack[x][y].contains(" > ")){
               temp = selectStack[x][y].split(">");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);
               String value = test.getValue(temp[0]);
               int castToInt = Integer.parseInt(value);
               if(castToInt > Integer.parseInt(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }
            }else if(selectStack[x][y].contains(" <= ")){
               temp = selectStack[x][y].split("<=");
               temp[0] = temp[0].substring(1,temp[0].length()-1);
               temp[1] = temp[1].substring(1,temp[1].length()-1);
               String value = test.getValue(temp[0]);
               int castToInt = Integer.parseInt(value);
               if(castToInt <= Integer.parseInt(temp[1]) && fullAndCheck){
                  fullAndCheck = true;
               }else{
                  fullAndCheck = false;
               }
            }else if(selectStack[x][y].contains(" >= ")) {
               temp = selectStack[x][y].split(">=");
               temp[0] = temp[0].substring(1, temp[0].length() - 1);
               temp[1] = temp[1].substring(1, temp[1].length() - 1);
               String value = test.getValue(temp[0]);
               int castToInt = Integer.parseInt(value);
               if (castToInt >= Integer.parseInt(temp[1]) && fullAndCheck)
               {
                  fullAndCheck = true;
               } else {
                  fullAndCheck = false;
               }
            }
         }
         if(fullAndCheck){
            return fullAndCheck;
         }
      }
      return false;
   }
}