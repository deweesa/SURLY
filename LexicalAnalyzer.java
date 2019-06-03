/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
import jdk.nashorn.internal.scripts.JO;
import sun.awt.image.ImageWatched;

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
            else if(command.contains("SELECT")) {
               Select(command);
            }
            else if(command.startsWith("DESTROY")) {
               Destroy(command);
            }
            else if(command.contains("PROJECT")) {
               Project(command);
            }
            else if(command.contains("JOIN")) {
               Join(command);
            }

         }
      }               
   }

   private void Relation(String command) {
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
         String relationName = pRelation.parseRelationName();
         catalogTuple.add(new AttributeValue("RELATION", relationName));
         catalogTuple.add(new AttributeValue("ATTRIBUTES", Integer.toString(count)));
         catalog.add(catalogTuple);
      }
      else {
         System.out.println("Bad RELATION syntax: unmatched parens");
      }
   }

   private void Insert(String command) {
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
      LinkedList<Attribute> schema = relation.getSchema();
      int schemaSize = schema.size();

      if(schemaSize != tuple.size())
      {
         System.out.println("Bad INSERT syntax: tuple count and relation attribute count mismatch");
         return;
      }

      boolean add = true;
      for(int i = 0; i < schemaSize; i++)
      {
         Attribute attribute = schema.get(i);
         String attributeName = attribute.getName();
         AttributeValue attributeValue = tuple.get(i);
         attributeValue.setName(attributeName);

         String datatype = attribute.getDatatype();
         if(datatype.equals("NUM"))
         {
            String value = tuple.getValue(attributeName);
            if(!value.matches("[0-9]+"))
            {
               add = false;
            }
         }
      }

      if (add) {
         tuple.setNames(relation);
         relation.add(tuple);
      }
   }

   private void Delete(String command) {
      DeleteParser pDelete = new DeleteParser(command);
      String name = pDelete.parseRelationName();
      String where = pDelete.parseWhereClause();
      Relation relation = null;
      try {
         relation = database.getBaseRelation(name);
      } catch (Exception e) {
         System.out.println(e);
      }
      if(where.equals("")) {
         try {
            relation.delete();
         } catch (Exception e) {
            return;
         }
      }

      WhereParser wDelete = new WhereParser(where);
      wDelete.parseOrs();
      String [][] parsedByAnds = wDelete.parseAnds();
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

   private void Destroy(String command) {
      DestroyParser pDestroy = new DestroyParser(command);
      String name = pDestroy.parseRelationName();

      database.destroyRelation(name);
   }

   private void Print(String command) {
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

      LinkedList<Attribute> tempSchema = (LinkedList) baseRelation.getSchema().clone();
      LinkedList<Attribute> finalSchema = new LinkedList<>();

      String[] attributes = pProject.parseAttributes();

      for(int i = 0; i < tempSchema.size(); i++) {
         boolean found = false;
         for(int j = 0; j < attributes.length; j++) {
            if(tempSchema.get(i).getName().equals(attributes[j])){
               found = true;
            }
         }

         if(found){
            finalSchema.add(tempSchema.get(i));
         }
      }

      LinkedList<Tuple> tempTuples = (LinkedList) baseRelation.getTuples();
      LinkedList<Tuple> finalTuples = new LinkedList<>();
      String valueName;
      for(int i = 0; i < tempTuples.size(); i++) {
         Tuple tuple = new Tuple(tempTuples.get(i));

         for(int j = 0; j < tuple.size(); j++) {
            AttributeValue attrVal = tuple.get(j);
            valueName = attrVal.getName();
            boolean found = false;
            for(int k = 0; k < attributes.length; k++) {
               if(attributes[k].equals(valueName)) {
                  found = true;
               }
            }

            if(!found) {
               tuple.remove(j);
               j--;
            }
         }

         finalTuples.add(tuple);
      }

      Relation newRelation = new Relation(tempRelationName);
      newRelation.setSchema(finalSchema);
      newRelation.setTuples(finalTuples);

      database.createTempRelation(newRelation);
   }

   private void Join(String command) {
      JoinParser pJoin = new JoinParser(command);
      String tempName = pJoin.parseTempName();
      String[] baseRelations = pJoin.parseBaseRelations();
      String[] joinCondition = pJoin.parseJoinCondition();

      int dotIndex = joinCondition[0].indexOf(".");
      String leftAttributeName = joinCondition[0].substring(dotIndex+1);

      dotIndex = joinCondition[2].indexOf(".");
      String rightAttributeName = joinCondition[2].substring(dotIndex+1);

      Relation baseRelationLeft;
      Relation baseRelationRight;
      try {
         baseRelationLeft = (Relation) database.getRelation(baseRelations[0]).clone();
         baseRelationRight = (Relation) database.getRelation(baseRelations[1]).clone();
      } catch (Exception e) {
         System.out.println(e);
         System.out.println("One or more tables don't exist");
         return;
      }

      Relation finalRelation = new Relation(tempName);
      LinkedList<Attribute> finalSchema = new LinkedList();
      LinkedList<Attribute> leftSchema = baseRelationLeft.copySchema(); //(LinkedList) baseRelationLeft.getSchema().clone();
      LinkedList<Attribute> rightSchema = baseRelationRight.copySchema(); //(LinkedList) baseRelationRight.getSchema().clone();
      LinkedList<Tuple> leftTuples = baseRelationLeft.getTuples();
      LinkedList<Tuple> rightTuples = baseRelationRight.getTuples();
      Tuple leftTuple;
      Tuple rightTuple;
      Attribute leftAttribute;
      Attribute rightAttribute;
      AttributeValue leftValue;
      AttributeValue rightValue;

      Tuple newTuple = new Tuple();

      int leftIndex = baseRelationLeft.getColumnIndex(leftAttributeName);
      int rightIndex = baseRelationRight.getColumnIndex(rightAttributeName);

      leftAttribute = leftSchema.get(leftIndex);
      rightAttribute = rightSchema.get(rightIndex);

      if (!pJoin.comparable(leftAttribute, rightAttribute)) {
         System.out.println("Not comparabale");
         return;
      }

      finalSchema.addAll(leftSchema);
      finalSchema.addAll(rightSchema);

      finalRelation.setSchema(finalSchema);

      for (int i = 0; i < leftTuples.size(); i++) {
         leftTuple = new Tuple(leftTuples.get(i));
         leftValue = new AttributeValue(leftTuple.get(leftIndex));

         for (int j = 0; j < rightTuples.size(); j++) {
            rightTuple = rightTuples.get(j);
            rightValue = rightTuple.get(rightIndex);
            if (pJoin.comparison(leftValue, rightValue, joinCondition[1])) {
               newTuple.addAll(leftTuple);
               newTuple.addAll(rightTuple);
               finalRelation.add(newTuple);
               newTuple = new Tuple();
            }
         }
      }

      if(leftAttributeName.equals(rightAttributeName) || joinCondition[1].equals("=")){
         finalRelation.removeLastAttribute(rightAttributeName);
      }
      finalRelation.updateNames(baseRelations[0], baseRelations[1]);
      database.createTempRelation(finalRelation);
   }
}