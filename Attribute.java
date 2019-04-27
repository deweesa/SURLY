public class Attribute{
   private String name;
   private String dataType;
   private int length;


   Attribute()
   {
      this.name = "";
      this.dataType = "";
      this.length = 0;
   }
   Attribute(String name, String dataType, int length)
   {
      this.name = name;
      this.dataType = dataType;
      this.length = length;
   }

   Attribute(String dataType, int length)
   {
      this.name = "";
      this.dataType = dataType;
      this.length = 0;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return name;
   }


   public void setDatatype(String datatype) {
      this.dataType = dataType;
   }

   public String getDatatype() {
      return dataType;
   }


   public void setLength(int length) {
      this.length = length;
   }

   public int getLength() {
      return length;
   }

   public String toString()
   {
      return name+"::"+dataType+"::"+length;
   }

}