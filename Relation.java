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

   public void removeAttribute(String atName){
      int index = -1;

      for(int i = 0 ; i < schema.size();i++){
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