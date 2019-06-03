/* Asa DeWeese & Ben Malquist
 * SURLY 0
 * CSCI 330, 12:00pm
 */
public class DeleteParser {
    private String input;
    public DeleteParser(String input) {
        this.input = input;
    }
    public String parseRelationName(){
        String temp = "[ ]+";
        String[] out = input.split(temp);
        if (out.length == 2){

            out[1] = out[1].substring(0,out[1].length()-1);
        }
        return out [1];
    }
    public String parseWhereClause(){
        String[] out = input.split("WHERE");
        if(out.length<=1){
            return "";
        }
        else {
            return out [1];
        }
    }
}

