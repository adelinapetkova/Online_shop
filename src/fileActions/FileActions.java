package fileActions;

import entity.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileActions {
    public static void makeInvoice(HashMap<Product, Integer> cart) {
        try {
            float finalPrice=0;
            File myObj = new File("invoice.txt");
            FileWriter myWriter = new FileWriter("invoice.txt");
            myWriter.append("Products and quantities:\n");
            for (Product product:cart.keySet()){
                finalPrice+=product.getPrice()*cart.get(product);
                myWriter.append(product.getName() + " ---> "+cart.get(product)+"\n");
            }
            myWriter.append("\nFinal price:\n");
            myWriter.append(finalPrice+ " lv.");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static ArrayList<HashMap> readFile() {
        HashMap<String, ArrayList<String>> categoriesAndSubcategories = new HashMap<>();
        HashMap<String, ArrayList<Product>> subcategoriesAndProducts = new HashMap<>();

        String currentCategory="";
        String currentSubcategory="";

        try {
            File myObj = new File("data.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String currentLine = myReader.nextLine();
                if(currentLine.charAt(0)=='<' && currentLine.charAt(1)=='/') {
                    currentCategory = "";
                }else if(currentLine.charAt(0)=='<'){
                    currentCategory=currentLine.substring(1,currentLine.indexOf('>'));
                    categoriesAndSubcategories.put(currentCategory, new ArrayList<>());
                }else if(currentLine.charAt(0)=='_'){
                    currentSubcategory=currentLine.substring(1);
                    categoriesAndSubcategories.get(currentCategory).add(currentSubcategory);
                    subcategoriesAndProducts.put(currentSubcategory, new ArrayList<>());
                }else if(currentLine.charAt(0)=='*'){
                    String productInfo=currentLine.substring(1);
                    String[] data=productInfo.split(",");
                    Product product=new Product(data[0], Float.parseFloat(data[1]), data[2]);
                    subcategoriesAndProducts.get(currentSubcategory).add(product);
                }

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        ArrayList<HashMap> data=new ArrayList<>();
        data.add(categoriesAndSubcategories);
        data.add(subcategoriesAndProducts);

        return data;
    }
}
