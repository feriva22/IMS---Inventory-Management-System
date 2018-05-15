import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static String[][] inventoryData = new String[100][7];
    private static Scanner input = new Scanner(System.in);
    private static int i=0;

    /**
     * Method loadData()
     * for load all data from csv file "datainventory.csv" on the root project folder
     * to array 2 dimenssions called inventoryData with buffer [100][7]
     */

    public static void loadData(){
        try {
            Scanner data = new Scanner(new File("datainventory.csv"));
            String line = "";
            while ((line = data.nextLine()) != null) {

                //use comma as separator
                inventoryData[i] = line.split(",");
                i++;
            }

        }
        catch(final Exception e){

        }
    }

    /**
     * function delData()
     * for reset all array data (not database)
     * @return true if no error , otherwise false if have error
     */
    public static boolean delData(){
        try{
            for (int baris=0;baris<inventoryData.length;baris++){
                for (int kolom=0;kolom<inventoryData[baris].length;kolom++){
                    inventoryData[baris][kolom] = null;
                }
            }
        }
        catch (final Exception e){
            return false;
        }
        return true;

    }

    /**
     * Method saveData()
     * for save all data from array inventoryData to csv file "datainventory.csv" again
     */
    public static void saveData() {
        try {
            Writer wr = new FileWriter("datainventory.csv");

            for (int baris=0;baris<i;baris++){
                for (int kolom=0;kolom<inventoryData[0].length;kolom++){
                    wr.write(inventoryData[baris][kolom]+",");
                }
                wr.write("\n");
            }
            wr.flush();
            wr.close();

        }
        catch (final Exception e) {
        }
    }


    /**
     * Method clsScreen()
     * for clear all character on the screen terminal
     * notice : in v1.0 just work in terminal linux and cmd Windows
     */
    public static void clsScreen() {
        try {
            String os = System.getProperty("os.name");
            String[] osnya = os.split(" ");
            if (osnya[0].equals("Linux")) {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            } else if (osnya[0].equals("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        }
        catch (final Exception e){
            System.out.println("Maaf ada kesalahan dalam menghapus screen");
        }

    }

    /**
     * Method readInventory()
     * method for get inventory list from array inventoryData
     */
    public static void readInventory(){


        //printout table header
        System.out.printf("%-10s| %-8s| %-18s| %-12s| %-8s| %-18s| %-20s|%n",
                "Category","Brand","Item Description","Model Number","Stock","Unit Cost (Rp)","Total Cost (Rp)");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        for (int baris = 0;baris<=i-1;baris++) {
            System.out.printf("%-10s| %-8s| %-18s| %-12s| %-8s| %-18s| %-20s|%n",
                    inventoryData[baris][1], inventoryData[baris][2], inventoryData[baris][3], inventoryData[baris][4],
                    inventoryData[baris][5], inventoryData[baris][6], (Integer.parseInt(inventoryData[baris][5]) * Integer.parseInt(inventoryData[baris][6])));

        }


    }


    /**
     *
     * Function getEOQ
     * function for calculate best total amount quantitiy
     * @param D
     * @param S
     * @param H
     * @return
     */
    public static double getEOQ(int D, int S, int H){

        return Math.sqrt((2*D*S)/H);
    }


    /**
     * Method addItem()
     * method for add a item in database
     */
    public static void addItem (){

        String[] datatunggal = new String[7];

        datatunggal[0] = String.valueOf(Integer.parseInt(inventoryData[i-1][0])+1);
        System.out.printf("Masukkan Kategori : ");
        input.nextLine();
        datatunggal[1]=input.nextLine();
        System.out.printf("Masukkan Brand : ");
        datatunggal[2]=input.nextLine();
        System.out.printf("Masukkan Item Description : ");
        datatunggal[3]=input.nextLine();
        System.out.printf("Masukkan Model Number : ");
        datatunggal[4]=input.nextLine();
        System.out.printf("Masukkan Stock : ");
        datatunggal[5]=input.nextLine();
        System.out.printf("Masukkan Unit Cost (Rp) : ");
        datatunggal[6]=input.nextLine();

        for(int baris = 0;baris<inventoryData.length;baris++){
            if (inventoryData[baris][0] == null) {
                for (int l = 0; l < inventoryData[0].length; l++) {
                    inventoryData[baris][l] = datatunggal[l];
                }
                i++;
                break;
            }

        }
    }

    /**
     * method delitem()
     * for delete an item in inventory
     * @param modelName model name of a item
     */
    public static void delitem(String modelName){
        try {
            String[][] matriksTmp = new String[100][7];
            int row = 0;
            int tmp = i;
            for (int baris = 0; baris < tmp; baris++) {
                int col = 0;
                if (inventoryData[baris][4].contains(modelName)) {
                    i--;
                    continue;
                } else {
                    for (int kolom = 0; kolom < inventoryData[0].length; kolom++) {
                        matriksTmp[row][col] = inventoryData[baris][kolom];
                        col++;
                    }
                }
                row++;
            }

            if (delData()) {
                for (int baris = 0; baris < i; baris++) {
                    for (int kolom = 0; kolom < inventoryData[baris].length; kolom++) {
                        inventoryData[baris][kolom] = matriksTmp[baris][kolom];
                    }
                }
            }

            System.out.println("Success delete item");
        }
        catch (final Exception e){
            System.out.println("Sorry cannot delete item");
        }

    }

    /**
     * method Main()
     * main method in program java
     *
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException {
        clsScreen();
        System.out.println("IMS (Inventory Management System) ");
        System.out.println("Running on system : "+System.getProperty("os.name"));
        loadData();
        boolean notExit = true;
        String command = "";
        System.out.println("Enter command , type \"?\" for help");

        while(notExit){
            System.out.print("cmd =>");

            try {
                command = input.next();
            }
            catch (InputMismatchException e){
                System.out.println(e.getMessage());
            }

            if(command.compareToIgnoreCase("?") == 0){
                clsScreen();
                System.out.println("Inventory Commands:");
                System.out.println("Cmd\tCommand\tParams\tDescription");
                System.out.println("~~~\t~~~~~~~\t~~~~~~\t~~~~~~~~~~~");
                System.out.println("li\tlistitems\t\tList all items on inventory");
                System.out.println("additem\tadd item\t\tAdd a item");
                System.out.println("delitem\tdelete item\t\tDelete a item");
                System.out.println();
                System.out.println("Management Commands:");
                System.out.println("Cmd\tCommand\tParams\tDescription");
                System.out.println("~~~\t~~~~~~~\t~~~~~~\t~~~~~~~~~~~");
                System.out.println("?\thelp\t\tGet Help");
                System.out.println("savedata\tsave data\t\tSave data to Database");
                System.out.println("exit\texitapp\t\tExit the application");
            }
            else if(command.compareToIgnoreCase("li") == 0){
                clsScreen();
                readInventory();
            }
            else if(command.compareToIgnoreCase("additem") == 0){
                clsScreen();
                addItem();
                clsScreen();
                readInventory();

            }
            else if(command.compareToIgnoreCase("delitem") == 0){
                System.out.print("Input the Model Name : ");
                String modName = input.next();
                delitem(modName);


            }
            else if (command.compareToIgnoreCase("savedata") == 0){
                saveData();
            }
            else if(command.compareToIgnoreCase("exit") == 0){
                notExit = false;
            }
            else {
                System.out.println("Command not found");
            }

        }

        System.out.println("Thanks has using this Software");


    }
}

