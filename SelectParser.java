/**
 * Created by malquib2 on 5/24/19.
 */
public class SelectParser {
    private String input;
    public SelectParser(String input) {
        this.input = input;
    }
    public String parseTempName() {
        String temp = "[ ]+";
        String[] out = input.split(temp);
        out[0] = out[0].substring(0,out[0].length());
        return out [0];
    }
    public String parseRelationName(){
        String temp = "[ ]+";
        String[] out = input.split(temp);
        out[3] = out[3].substring(0,out[3].length());
        return out [3];
    }
    public String parseWhereClause(){
        String[] out = input.split("WHERE");
        if(out.length<=1){
            return "no where clause";
        }
        else {
            return out [1];
        }
    }
}

