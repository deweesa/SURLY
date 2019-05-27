import java.util.*;
public class Tuple{
   private LinkedList<AttributeValue> values = new LinkedList<>();

   public void add(AttributeValue attributeValue)
   {
      values.add(attributeValue);
   }

   public AttributeValue get(int i)
   {
      return values.get(i);
   }
   
   public String getValue(String attributeName)
   { 
      int size = values.size();
      for(int i = 0; i < size; i++)
      {
         if(values.get(i).getName().equals(attributeName))
         {
            return values.get(i).getValue();
         }
      }

      return "";
   }

   public int size()
   {
      return values.size();
   }

   public void setNames(Relation relation)
   {
      int size = size();
      for(int i = 0; i < size; i++)
      {
         Attribute attrDefn = relation.getSchema().get(i);
         AttributeValue attr = values.get(i);
         attr.setName(attrDefn.getName());
         attr.lengthValidation(attrDefn.getLength());
      }
   }

   public void remove(int i) {
      values.remove(i);
   }

   public String toString()
   {
      return values.toString();
   }
}
   