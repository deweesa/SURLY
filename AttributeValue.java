/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class AttributeValue {
   private String name;
   private String value;

   AttributeValue(String name, String value)
   {
       this.name = name;
       this.value = value;
   }

   AttributeValue(String value)
   {
       this.name = "";
       this.value = value;
   }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
      return name;
   }

   public String getValue() {
      return value;
   }

   public void lengthValidation(int length)
   {
       if(value.length() > length)
       {
           value = value.substring(0, length-1);
       }
   }

   public String toString()
   {
       return name+":"+value;
   }
}