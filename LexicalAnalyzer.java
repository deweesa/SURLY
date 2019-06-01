/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
import java.io.File;
import java.util.LinkedList;
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

            else if(command.contains("PROJECT")) {
               Project(command);
            }

            else if(command.contains("JOIN")) {
               JoinParser pJoin = new JoinParser(command);
               String tempName = pJoin.parseTempName();
               String[] baseRelations = pJoin.parseBaseRelations();
               String[] joinCondition = pJoin.parseJoinCondition();

               Relation baseRelationLeft;
               Relation baseRelationRight;
               try {
                  baseRelationLeft = database.getRelation(baseRelations[0]);
                  baseRelationRight = database.getRelation(baseRelations[1]);
               } catch (Exception e) {
                  System.out.println(e);
                  return;
               }

               LinkedList<Attribute> leftSchema = (LinkedList) baseRelationLeft.getSchema().clone();
               LinkedList<Attribute> rightSchema = (LinkedList) baseRelationRight.getSchema().clone();
               LinkedList<Attribute> tempSchema = new LinkedList<>();

               Attribute leftAttr, rightAttr;

               String leftName;
               String rightName;

               for(int i = 0; i < leftSchema.size(); i++) {
                  leftAttr = leftSchema.get(i);
                  leftName = leftAttr.getName();
                  for(int j = 0; j < rightSchema.size(); j++) {
                     rightAttr = rightSchema.get(j);
                     rightName = rightAttr.getName();
                     if(leftName.equals(rightName)) {
                        leftName = baseRelationLeft.getName()+"."+leftName;
                        rightName = baseRelationRight.getName()+"."+rightName;

                        Attribute newLeft = new Attribute(leftName, leftAttr.getDatatype(), leftAttr.getLength());
                        Attribute newRight = new Attribute(rightName, rightAttr.getDatatype(), rightAttr.getLength());

                        leftSchema.set(i, newLeft);
                        rightSchema.set(j, newRight);
                     }
                  }
               }

               tempSchema.addAll(leftSchema);
               tempSchema.addAll(rightSchema);

               for(int i = 0; i < tempSchema.size(); i++){
                  System.out.println(tempSchema.get(i));
               }

               LinkedList<Tuple> leftTuples = (LinkedList) baseRelationLeft.getTuples().clone();
               LinkedList<Tuple> rightTuples = (LinkedList) baseRelationRight.getTuples().clone();

               for(int i = 0; i < leftTuples.size(); i++) {
                  Tuple tuples = leftTuples.get(i);
                  for(int j = 0; j < tuples.size(); j++) {
                     AttributeValue attr = tuples.get(j);
                     attr.setName(leftSchema.get(j).getName());
                  }

               }

               for(int i = 0; i < rightTuples.size(); i++) {
                  Tuple tuple = rightTuples.get(i);
                  for(int j = 0; j < tuple.size(); j++) {
                     AttributeValue attr = tuple.get(j);
                     attr.setName(rightSchema.get(j).getName());
                  }
               }

               Relation tempLeft = new Relation(baseRelations[0]);
               tempLeft.setSchema(leftSchema);
               tempLeft.setTuples(leftTuples);

               Relation tempRight = new Relation(baseRelations[1]);
               tempRight.setSchema(rightSchema);
               tempRight.setTuples(rightTuples);

               Relation tempJoined = new Relation(tempName);
               tempJoined.setSchema(tempSchema);
               LinkedList<Tuple> joinedTuples = new LinkedList<>();

               int leftColumn = tempLeft.getColumnIndex(joinCondition[0]);
               int rightCoumn = tempRight.getColumnIndex(joinCondition[2]);

               for(int i = 0; i < leftTuples.size(); i++) {
                  Tuple leftTuple = leftTuples.get(i);
                  String leftValue = leftTuple.get(leftColumn).getValue();
                  for(int j = 0; j < rightTuples.size(); j++){
                     Tuple rightTuple = rightTuples.get(j);
                     String rightValue = rightTuple.get(rightCoumn).getValue();

                     if(leftValue.equals(rightValue)) {
                        Tuple joined = new Tuple();
                        joined.addAll(leftTuple);
                        joined.addAll(rightTuple);
                        joinedTuples.add(joined);
                     }

                  }
               }

               tempJoined.setTuples(joinedTuples);
               tempJoined.removeAttribute(joinCondition[2]);

               tempSchema = tempJoined.getSchema();

               for(int i = 0; i < tempSchema.size(); i++) {
                  Attribute attribute = tempSchema.get(i);
                  String name = attribute.getName();

                  if(name.contains(".")) {
                     name = name.substring(name.indexOf(".")+1);
                  }
                  attribute.setName(name);
               }

               for(int i = 0; i < joinedTuples.size(); i++) {
                  Tuple tuple = joinedTuples.get(i);
                  for(int j = 0; j < tuple.size(); j++) {
                     AttributeValue value = tuple.get(j);
                     String name = value.getName();

                     if(name.contains(".")) {
                        name = name.substring(name.indexOf(".")+1);
                     }
                     value.setName(name);
                  }
               }


               database.createTempRelation(tempJoined);
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

   private void Delete(String command) {
      DeleteParser pDelete = new DeleteParser(command);
      String name = pDelete.parseRelationName();
      String Where = pDelete.parseWhereClause();
      WhereParser wDelete = new WhereParser(Where);
      wDelete.parseOrs();
      String [][] parsedByAnds = wDelete.parseAnds();
      Relation relation = null;
      try {
         relation = database.getBaseRelation(name);
      } catch (Exception e) {
         System.out.println(e);
      }
      if(relation != null) {
         relation.delete(parsedByAnds);
      }
   }
   private void Select(String command){
      SelectParser pSelect = new SelectParser(command);
      String tempRelationName = pSelect.parseTempName();
      String baseRelationName = pSelect.parseRelationName();
      String Where = pSelect.parseWhereClause();
      Relation baseRelation;
      try {
         baseRelation =  database.getRelation(baseRelationName);
      } catch (Exception e) {
         System.out.println(e);
         return;
      }
      LinkedList<Attribute> baseSchema = baseRelation.getSchema();
      LinkedList<Tuple> baseTuple = baseRelation.copyTuples();
      WhereParser wSelect = new WhereParser(Where);
      wSelect.parseOrs();
      String [][] parsedByAnds = wSelect.parseAnds();
      Relation relation = null;
      Relation tempRelation = new Relation(tempRelationName);
      tempRelation.setTuples(baseTuple);
      tempRelation.setSchema(baseSchema);
      database.createTempRelation(tempRelation);
      try {
         relation = database.getRelation(tempRelationName);
      } catch (Exception e) {
         System.out.println(e);
      }
      if(relation != null) {
         tempRelation.select(parsedByAnds);
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

   private void Project(String command) {
      ProjectParser pProject = new ProjectParser(command);
      String tempRelationName = pProject.parseTempName();
      String baseRelationName = pProject.parseRelationName();

      Relation baseRelation;
      try {
         baseRelation =  database.getRelation(baseRelationName);
      } catch (Exception e) {
         System.out.println(e);
         return;
      }

      LinkedList<Attribute> baseSchema = baseRelation.getSchema();
      LinkedList<Attribute> tempSchema = new LinkedList<>();

      String[] attributes = pProject.parseAttributes();

      for(int i = 0; i < baseSchema.size(); i++) {
         boolean found = false;
         for(int j = 0; j < attributes.length; j++) {
            if(baseSchema.get(i).getName().equals(attributes[j])){
               found = true;
            }
         }

         if(found){
            tempSchema.add(baseSchema.get(i));
         }
      }

      LinkedList<Tuple> baseTuple = baseRelation.getTuples();
      LinkedList<Tuple> tempTuple = new LinkedList<>();

      for(int i = 0; i < baseTuple.size(); i++) {
         Tuple tuple = baseTuple.get(i);

         for(int j = 0; j < tuple.size(); j++) {
            AttributeValue attrVal = tuple.get(j);
            boolean found = false;
            for(int k = 0; k < attributes.length; k++) {
               if(attributes[k].equals(attrVal.getName())) {
                  found = true;
               }
            }

            if(!found) {
               tuple.remove(j);
               j--;
            }
         }

         tempTuple.add(tuple);
      }

      Relation tempRelation = new Relation(tempRelationName);
      tempRelation.setTuples(tempTuple);
      tempRelation.setSchema(tempSchema);

      database.createTempRelation(tempRelation);
   }
}