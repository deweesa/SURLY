/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class Attribute implements Cloneable{
   private String name;
   private String dataType;
   private int length;

   Attribute(String name, String dataType, int length)
   {
      this.name = name;
      this.dataType = dataType;
      this.length = length;
   }

   public Object clone() throws CloneNotSupportedException{
      return super.clone();
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDatatype() {
      return dataType;
   }

   public int getLength() {
      return length;
   }

   public String toString()
   {
      return name+"::"+dataType+"::"+length;
   }

}