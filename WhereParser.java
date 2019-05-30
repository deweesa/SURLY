/**
 * Created by malquib2 on 5/25/19.
 */
public class WhereParser {

    private String input;

    private String [] parsedByOrs;

    private String[][] parsedByAnds;

    public WhereParser(String in){
        input = in;

    }

    public void parseOrs(){

        parsedByOrs = input.split("or");

    }

    public String[][] parseAnds(){

        parsedByAnds = new String[parsedByOrs.length][];

        String [] temp;

        for (int i = 0 ; i< parsedByOrs.length ; i++){
            temp = parsedByOrs[i].split("and");
            parsedByAnds[i] = temp;
        }
        return parsedByAnds;
    }

}
