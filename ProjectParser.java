import java.util.Scanner;

/**
 * Created by deweesa on 5/25/19.
 */
public class ProjectParser {
    private String input;
    private Scanner sc;

    ProjectParser(String input) {
        this.input = input;
    }

    public String parseRelationName(){
        String name = input.substring(input.indexOf("FROM"));
        name = name.trim();
        sc = new Scanner(name);
        sc.next();
        name = sc.next();
        name = name.substring(0, name.length()-1);
        return name;
    }

    public String[] parseAttributes(){
        String unsplitAttr = input.substring(input.indexOf("PROJECT")+7, input.indexOf("FROM"));
        System.out.println(unsplitAttr);
        String[] attrs = unsplitAttr.split(",");
        for(int i = 0; i < attrs.length; i++) {
            attrs[i] = attrs[i].trim();
        }
        return attrs;
    }

    public String parseTempName(){
        int equalsIndex = input.indexOf("=");
        return input.substring(0,equalsIndex).trim();
    }
}
