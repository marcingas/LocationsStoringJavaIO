package com.MarcinDev;

import com.MarcinDev.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static Locations locations = new Locations();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<String,String>vocabulary = new HashMap<>();
        vocabulary.put("NORTH", "N");
        vocabulary.put("SOUTH", "S");
        vocabulary.put("WEST", "W");
        vocabulary.put("EAST", "E");
        vocabulary.put("QUIT", "Q");

        int loc =  64;
        while(true){
            System.out.println(locations.get(loc).getDescription());
            if(loc==0){
                break;
            }

            Map<String, Integer> exits = locations.get(loc).getExits();
            System.out.println("Available exits are ");
            for(String exit: exits.keySet()) {
                System.out.print(exit + ", ");
            }
            System.out.println();
            String direction = scanner.nextLine().toUpperCase();
            if(direction.length()>1){
                String[]words = direction.split(" ");
                for(String word: words){
                    if(vocabulary.containsKey(word)){
                        direction= vocabulary.get(word);
                        break;
                    }
                }
            }
            if(exits.containsKey(direction)){
                loc = exits.get(direction);
            } else{
                System.out.println("Cops are there can not go there! ");
            }
        }
    }
}