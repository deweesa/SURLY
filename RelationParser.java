

import java.util.*;
import java.io.*;
/**
 * Created by malquib2 on 4/4/19.
 */
public class RelationParser {

    public String newest;
    public String [] parsedBySpaces;

    public Relation toBeReturnedRelation = new Relation();
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

        return Attributes;

    }
}