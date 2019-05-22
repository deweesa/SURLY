/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class RelationParser {

    private String input;

    public RelationParser(String in){
        input = in;

    }

    public String parseRelationName(){
        String [] temp = input.split(" ");
        return temp[1];
    }

    public int parseAttributeCount() {
        if(input.indexOf('(') == -1 || input.indexOf(')') == -1) return -1;
        String attrList = input.substring(input.indexOf('('));
        return attrList.split(",").length;
    }

    public Relation parseRelation() {
        Relation toBeReturnedRelation = new Relation(parseRelationName());

        int start = input.indexOf("(");
        int end = input.indexOf(")");

        String Definitions = input.substring(start + 1, end);
        String[] parsedByCommas = Definitions.split(",");

        String name = null;
        String dataType = null;
        int lengthOfAttribute;

        for (int i = 0; i < parsedByCommas.length; i++) {
            String[] parsedBySpaces = parsedByCommas[i].trim().split("\\s+");

            if(parsedBySpaces.length != 3) return null;
            if(!(parsedBySpaces[1].equals("CHAR") || parsedBySpaces[1].equals("NUM"))) return null;
            if(!(parsedBySpaces[2].matches("[0-9]+"))) return null;

            name = parsedBySpaces[0];

            dataType = parsedBySpaces[1];

            lengthOfAttribute = Integer.parseInt(parsedBySpaces[2]);

            toBeReturnedRelation.getSchema().add(new Attribute(name, dataType, lengthOfAttribute));

        }

        return toBeReturnedRelation;
    }

}