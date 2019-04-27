

import java.util.*;
import java.io.*;
/**
 * Created by malquib2 on 4/4/19.
 */
public class RelationParser {


    private String input;
    private String[] attrDefns;

    public RelationParser(String input)
    {
        this.input = input;
    }

    public Relation parseRelation()
    {
        Relation relation = new Relation(parseRelationName());
        int beginAttr = input.indexOf('(')+1;
        int endAttr = input.lastIndexOf(')');
        String attributes = input.substring(beginAttr, endAttr);

        System.out.println("__"+attributes+"__");

        attrDefns = attributes.split(",");
        for(int i = 0; i < attrDefns.length; i++)
        {
            attrDefns[i] = attrDefns[i].trim();
            System.out.println(attrDefns[i]);

            String[] pieces = attrDefns[i].split("\\s+");

            String name = pieces[0];
            String dataType = pieces[1];
            int length = Integer.parseInt(pieces[2]);

            relation.addSchema(new Attribute(name, dataType, length));

        }
        System.out.println(relation);
        return relation;
    }

    public int parseAttributeCount()
    {
        return 1;
    }

    public String parseRelationName()
    {
        return input.split(" ")[1];
    }

    /*public String newest;
    public String [] parsedBySpaces;

    public Relation toBeReturnedRelation = new Relation(parseRelationName());
    public Attribute needsToBeAdded = new Attribute();

    public RelationParser(String input){
        newest = input;
        parsedBySpaces = parse();
        int Attributes = parseAttributeCount();
        //System.out.println("Creating " + needToUse[1] +" with " + Attributes +" attributes");

    }

    public String [] parse(){
        String temp = "[ ]+";

        String[] out = newest.split(temp);

        return out;
    }

    public String parseRelationName()
    {
        return parsedBySpaces[1];
    }

    public int parseAttributeCount() {
        int Attributes = 0;
        toBeReturnedRelation.setName(parsedBySpaces[1]);

        for(int i = 2; i < parsedBySpaces.length; i++){
            if(((i % 3) == 2)){
                String atName =" ";
                if (i == 2){
                    atName = parsedBySpaces[i].substring(1,parsedBySpaces[i].length());
                }else{
                    atName = parsedBySpaces[i];
                }

                needsToBeAdded.setName(atName);

            }
            else if (((i % 3) == 0)){
                needsToBeAdded.setDatatype(parsedBySpaces[i]);
            }
            else if (((i % 3) == 1)){
                String number = " ";

                if(String.valueOf(parsedBySpaces[i].charAt(parsedBySpaces[i].length()-1)).equals(",")){

                    number = parsedBySpaces[i].substring(0,parsedBySpaces[i].length()-1);

                }
                else if (String.valueOf(parsedBySpaces[i].charAt(parsedBySpaces[i].length()-1)).equals(";")){

                    number = parsedBySpaces[i].substring(0,parsedBySpaces[i].length()-2);

                }
                int length = Integer.parseInt(number);

                needsToBeAdded.setLength(length);
                toBeReturnedRelation.addSchema(needsToBeAdded);
                Attributes++;
            }
        }

        System.out.println("Relation " + toBeReturnedRelation.getSchema());

        return Attributes;

    }

    public Relation parseRelation() {
        return toBeReturnedRelation;
    }*/
}