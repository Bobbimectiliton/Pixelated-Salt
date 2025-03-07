package microtransactions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class dataReader {
    private HashMap<Integer, ArrayList<Items>> itemPool;
    private LinkedList<Items> inventory;
    private double money;
    private double score;
    private BufferedReader scan;
    private int curDiff;
    
    public dataReader(){
        //todo: Reads File
        FileHandle filePath = Gdx.files.internal("shop/Data.txt");
        scan = new BufferedReader(new InputStreamReader(filePath.read()));

        if (scan != null) {
            try {
                money = Double.parseDouble(scan.readLine());
                inventory = new LinkedList<>();
                itemPool = new HashMap<>();
                curDiff = 1;

                int numOfRarities = Integer.parseInt(scan.readLine());
                for (int i = 0; i < numOfRarities; i++) {
                    itemPool.put(i,new ArrayList<>());
                }

                String line = scan.readLine();
                System.out.println(line);
                int rarity=0;
                float baseStats=0;
                String category="";
                String description="";
                String name="";
                int numOfItems=0;
                String path = "";
                while (!line.equals("ITEM LIST PAUSE")) {
                    // Print the line to diagnose the issue
                    //System.out.println("Processing line: " + line);

                       if(line.startsWith("name=")){
                        name = line.split("name=")[1];
                       }else if (line.startsWith("quantity=")) {
                        numOfItems = Integer.parseInt(line.split("quantity=")[1]);
                       }else if (line.startsWith("baseStat=")) {
                        baseStats = Float.parseFloat(line.split("baseStat=")[1]);
                       }else if (line.startsWith("type=")) {
                        category = line.split("type=")[1];
                       }else if (line.startsWith("descript=")) {
                        description = line.split("descript=")[1];
                       }else if (line.startsWith("rarity=")) {
                        rarity = Integer.parseInt(line.split("rarity=")[1]);
                       }else if (line.startsWith("path=")) {
                        System.out.println("Path: " + line);
                        path = line.split("path=")[1];
                       }else if (line.startsWith("-")) {
                        Items item = new Items(rarity, baseStats, category, name, description, path);
                        switch (rarity) {
                            case 1:
                                itemPool.get(0).add(item);
                                break;
                            case 2:
                                itemPool.get(1).add(item);
                                break;
                            case 3:
                                itemPool.get(2).add(item);
                                break;
                            case 4:
                                itemPool.get(3).add(item);
                                break;
                            default:
                                System.out.println("Unknown rarity: " + category);
                        }
                        for (int i = 0; i < numOfItems; i++) {
                            inventory.add(item);
                        }
                        rarity=0;
                        baseStats=0;
                        category="";
                        description="";
                        name="";
                        numOfItems=0;
                       }
                       line = scan.readLine();
                }
                score = calcScore();
                System.out.println(score);
            } catch (IOException e) {
                System.out.println("Error reading data from file: " + e.getMessage());
            }
        } else {
            System.out.println("Initialization failed due to missing data file.");
        }
    }

    

    public double calcScore(){
        double total = 0;
        for(Items item : inventory){
            total += item.getScore();
        }
        return total;
    }

    public double getScore(){return score;}

    public double getMoney() {return money;}

    public int getCurDiff(){return curDiff;}

    public LinkedList<Items> getInventory(){return inventory;}

    public void setInventory(LinkedList<Items> inventory){this.inventory = inventory;}

    public HashMap<Integer, ArrayList<Items>> getItemPool(){return itemPool;}
}
