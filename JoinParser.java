import java.util.Scanner;

/**
 * Created by deweesa on 5/30/19.
 */
public class JoinParser {

    private String input;
    private Scanner sc;

    JoinParser(String input) {
        this.input = input;
    }

    public String parseTempName(){
        int equalsIndex = input.indexOf("=");
        return input.substring(0,equalsIndex).trim();
    }

    public String[] parseBaseRelations(){
        String relations;
        String[] parsedRelations;

        int start = input.indexOf("JOIN")+4;
        int end = input.indexOf("ON");

        relations = input.substring(start, end);

        parsedRelations = relations.split(",");
        for(int i = 0; i < parsedRelations.length; i++){
            parsedRelations[i] = parsedRelations[i].trim();
        }

        return parsedRelations;
    }

    public String[] parseJoinCondition(){
        String joinCondition[] = new String[3];

        int start = input.indexOf("ON")+2;
        int end = input.indexOf(";");

        String conditional = input.substring(start, end);

        sc = new Scanner(conditional);
        for(int i = 0; i < 3; i++) {
            joinCondition[i] = sc.next();
            System.out.println(joinCondition[i]);
        }

        return joinCondition;
    }


}
