import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    //initialize array variable
    private static String[][] config = new String[6][2];
    private static String[][] inventoryData = new String[100][8];
    private static String[][] orderData = new String[1000][6];
    private static String[][] salesData = new String[1000][6];

    //initialize primitive variable
    private static int totItem = 0;
    private static int totOrd = 0;
    private static int totSales = 0;

    //initialize object variable
    private static Scanner input = new Scanner(System.in);
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static DateFormat clockFormat = new SimpleDateFormat("hh:mm:ss");
    private static Date date = new Date();



    /*-----------------------------------------------------------------------------------------------------------------*/
    /*Database Controller*/

    /**
     * method init()
     * load all database and configuration
     */
    public static void init() {
        try {
            loadConfig();
            //check if company profile is configurable
            if (config[0][1].compareToIgnoreCase("false") == 0) {
                System.out.println("Configuration for company profile not finish, please input !");
                System.out.print("Input Company name : ");
                config[1][1] = input.nextLine();
                System.out.print("What is sell in this company ? ");
                config[2][1] = input.nextLine();
                System.out.print("Input Address of Company : ");
                config[3][1] = input.nextLine();
                System.out.print("Input Number Contact of Company : ");
                config[4][1] = input.nextLine();
                System.out.print("Input Email of Company : ");
                config[5][1] = input.nextLine();
                config[0][1] = "true";
                if (saveConfig()) {
                    System.out.println("Data company saved");
                    Thread.sleep(3000);
                    clsScreen();
                    loadData();
                } else {
                    System.out.println("Error cannot save configuration");
                }
            }
            //if config has configurable
            else {
                loadData();
            }
        } catch (final Exception e) {

        }

    }

    /**
     * function loadConfig()
     * load configuration file
     *
     * @return true if load success, or false if load have trouble
     */
    public static void loadConfig() {
        try {

            //Read file config and save to array config
            Scanner configdata = new Scanner(new File("config.cfg"));
            String cfg = "";
            int x = 0;

            while ((cfg = configdata.nextLine()) != null) {
                //use '=' as separator
                config[x] = cfg.split("=");
                x++;
            }


        } catch (final Exception e) {


        }
    }

    /**
     * fucntion saveConfig()
     * save configuration to config file
     *
     * @return true if save success or false if have trouble
     */
    public static Boolean saveConfig() {
        try {
            Writer cnf = new FileWriter("config.cfg");
            for (int a = 0; a < config.length; a++) {
                cnf.write(config[a][0] + "=" + config[a][1]);
                cnf.write("\n");
            }

            cnf.flush();
            cnf.close();
            return true;

        } catch (final Exception e) {
            return false;
        }
    }

    /**
     * Method loadData()
     * for load all data from csv file "datainventory.csv" on the root project folder
     * to array 2 dimenssions called inventoryData with buffer [100][7]
     */
    public static void loadData() {
        try {

            //load data order of item and save to orderdata array
            totOrd = 0;
            Scanner dataOrd = new Scanner(new File("reportorder.csv"));
            String lineOrd = "";
            dataOrd.nextLine();
            while ( dataOrd.hasNextLine()  ){
                lineOrd = dataOrd.nextLine();
                //use comma as separator
                orderData[totOrd] = lineOrd.split(",");
                totOrd++;
            }

            //load data sales of item and save to salesData array
            totSales = 0;
            Scanner dataSales = new Scanner(new File("reportsales.csv"));
            String lineSales = "";
            dataSales.nextLine();
            while ( dataSales.hasNextLine()  ){
                lineSales = dataSales.nextLine();
                //use comma as separator
                salesData[totSales] = lineSales.split(",");
                totSales++;
            }

            //load data inventory and save to inventorydata array
            totItem = 0;
            Scanner dataInven = new Scanner(new File("datainventory.csv"));
            String lineInven = "";
            dataInven.nextLine();
            while (dataInven.hasNextLine()) {
                lineInven = dataInven.nextLine();
                //use comma as separator
                inventoryData[totItem] = lineInven.split(",");
                totItem++;
            }


        } catch (final Exception e) {
            System.out.println("load data error : "+e);
        }
    }


    /**
     * function delData()
     * for reset all array data (not database)
     *
     * @return true if no error , otherwise false if have error
     */
    public static boolean delData() {
        try {
            for (int baris = 0; baris < inventoryData.length; baris++) {
                for (int kolom = 0; kolom < inventoryData[baris].length; kolom++) {
                    inventoryData[baris][kolom] = null;
                }
            }
        } catch (final Exception e) {
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
            wr.write("id,category,brand,item_desc,mod_name,stock,unit_cost,sales_price,\n");
            wr.flush();
            wr.close();
            Writer wrdata = new FileWriter("datainventory.csv", true);
            for (int baris = 0; baris < totItem; baris++) {
                for (int kolom = 0; kolom < inventoryData[0].length; kolom++) {
                    if (inventoryData[baris][kolom].contains(",")) {
                        inventoryData[baris][kolom] = inventoryData[baris][kolom].replace('c', ' ');
                    }
                    wrdata.write(inventoryData[baris][kolom] + ",");
                }
                wrdata.write("\n");
            }
            wrdata.flush();
            wrdata.close();

        } catch (final Exception e) {
        }
    }

    /**
     * method writeReport
     * for writing report about sales and order
     * @param type type "order" or "sell"
     * @param trans_id transaction id generate by controller order and sell
     * @param item_id item id will be record
     * @param stock stock will be modified
     * @param unit_cost price perunit stock
     */
    public static void writeReport(String type, String trans_id, String item_id, int stock, int unit_cost) {
        try {
            Writer order = new FileWriter("reportorder.csv", true);
            Writer sell = new FileWriter("reportsales.csv", true);
            Date dd = new Date();

            if (type.compareToIgnoreCase("order") == 0) {
                order.write("\n");
                order.write(trans_id + ",");
                order.write(item_id + ",");
                order.write(stock + ",");
                order.write(unit_cost + ",");
                order.write(dateFormat.format(dd) + ",");
                order.write(clockFormat.format(dd) + ",");
                order.flush();
                order.close();
                saveData();
                loadData();

                //calculate average cost of item
                double tmptotorder = 0;
                double tmptotcost =0;
                for (int i=0;i<totOrd;i++){
                    if (orderData[Integer.parseInt(item_id)][1].compareToIgnoreCase(String.valueOf(0)) == 0){
                        tmptotcost += Integer.parseInt(orderData[i][3]);
                        tmptotorder++;
                    }
                }
                //set average cost to array inventory
                inventoryData[Integer.parseInt(item_id)][6] = String.format("%.0f",(tmptotcost/tmptotorder));
                saveData();
                loadData();

            } else if (type.compareToIgnoreCase("sell") == 0) {
                sell.write("\n");
                sell.write(trans_id + ",");
                sell.write(item_id + ",");
                sell.write(stock + ",");
                sell.write(unit_cost + ",");
                sell.write(dateFormat.format(dd) + ",");
                sell.write(clockFormat.format(dd) + ",");
                sell.flush();
                sell.close();
                saveData();
                loadData();
            }


        } catch (final Exception e) {

        }
    }


    /*---------------------------------------------------------------------------------------------------------------*/
    /*UI Controller*/


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
        } catch (final Exception e) {
            System.out.println("sorry there was an error in removing the screen");
        }

    }

    /**
     * function stripString
     * for remove character after char at index maxchar
     * @param text String will be strip
     * @param maxchar max String will be output
     * @return String after modification
     */
    public static String stripString(String text,int maxchar){
        if (text.length() > maxchar){
            String baru = "";
            for (int i=0;i<maxchar-3;i++){
                baru += text.charAt(i);
            }
            for (int j=1;j<=3;j++){
                baru += ".";
            }
            return baru;
        }
        else {
            return text;
        }
    }

    /**
     * Method readInventory()
     * method for get inventory list from array inventoryData
     */
    public static void readInventory() {

        //print company description
        System.out.printf("%-30s %85s\n",config[1][1].toUpperCase(),"Product Sell : "+config[2][1].toUpperCase());
        System.out.println(config[3][1]);
        System.out.printf("%70s","INVENTORY LIST\n");
        System.out.println();
        //printout table header
        System.out.printf("| %-10s| %-8s| %-15s| %-12s| %-5s| %-16s | %-18s| %-20s|%n",
                "Category", "Brand", "Item Desc", "Model Number", "Stock", "Sales Price (Rp)" ,"Average Cost (Rp)", "Total Cost (Rp)");
        System.out.println("|+++++++++++|+++++++++|++++++++++++++++|+++++++++++++|++++++|++++++++" +
                           "++++++++++|+++++++++++++++++++|+++++++++++++++++++++|");
        //printout content of table
        if (totItem > 0) {
            for (int baris = 0; baris <= totItem - 1; baris++) {
                System.out.printf("| %-10s| %-8s| %-15s| %-12s| %-5s| %-16s | %-18s| %-20s|%n",
                        stripString(inventoryData[baris][1],10), stripString(inventoryData[baris][2],8),
                        stripString(inventoryData[baris][3],15), stripString(inventoryData[baris][4],12),
                        stripString(inventoryData[baris][5],5), String.format("%,.0f",Double.parseDouble(inventoryData[baris][7])),
                        String.format("%,.0f",Double.parseDouble(inventoryData[baris][6])),
                        String.format("%,.0f",Double.parseDouble(inventoryData[baris][5]) * Double.parseDouble(inventoryData[baris][6]))
                );
            }
        }else{
            System.out.println("Data Empty");
        }
    }

    /**
     * method detailItem()
     * method for printout detail of a item
     * @param item_id item id
     */
    public static void detailItem(int item_id){
        clsScreen();
        System.out.println("Item details");
        System.out.println("Product Name : "+inventoryData[item_id][3]+" ("+inventoryData[item_id][4]+")");
        System.out.println("Brand : "+inventoryData[item_id][2]);
        System.out.println("Category : "+inventoryData[item_id][1]);
        System.out.println();
        System.out.println("Stock : " + inventoryData[item_id][5]);
        System.out.printf("Average Cost : Rp. %-16s\n",String.format("%,.0f",Double.parseDouble(inventoryData[item_id][6])));
        System.out.printf("Price per unit : Rp. %-16s\n",String.format("%,.0f",Double.parseDouble(inventoryData[item_id][7])));

        System.out.println("Statistic sales ");


    }


    public static void pwd() throws IOException{
        String dir = System.getProperty("user.dir");
        System.out.println("working dir : "+dir);
        File f = new File("."); // current directory

        File[] files = f.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.print("directory:");
            } else {
                System.out.print("     file:");
            }
            System.out.println(file.getCanonicalPath());
        }
    };

    /*----------------------------------------------------------------------------------------------------------------*/
    /*Inventory controller*/

    /**
     * Method addItem()
     * method for add a item in database
     */
    public static void addItem () throws InterruptedException{

        String[] datatunggal = new String[8];

        if(inventoryData[0][0] == null){
            datatunggal[0] = String.valueOf("0");
        } else {
            datatunggal[0] = String.valueOf(Integer.parseInt(inventoryData[totItem - 1][0]) + 1);
        }

        System.out.printf("add categories : ");
        input.nextLine();
        datatunggal[1]=input.nextLine();
        System.out.printf("add brand: ");
        datatunggal[2]=input.nextLine();
        System.out.printf("add Item Description : ");
        datatunggal[3]=input.nextLine();
        System.out.printf("add Model Number : ");
        datatunggal[4]=input.nextLine();
        System.out.printf("add Stock : ");
        datatunggal[5]=input.nextLine();
        System.out.printf("add Unit Cost (Rp) : ");
        datatunggal[6]=input.nextLine();
        System.out.printf("add Sales price : ");
        datatunggal[7]=input.nextLine();


        for(int baris = 0;baris<inventoryData.length;baris++){
            if (inventoryData[baris][0] == null) {
                for (int l = 0; l < inventoryData[0].length; l++) {
                    if (datatunggal[l].contains(",")){
                        datatunggal[l] = datatunggal[l].replace(',',' ');
                    }
                    inventoryData[baris][l] = datatunggal[l];
                }
                totItem++;
                orderStock(Integer.parseInt(datatunggal[0]),0,Integer.parseInt(datatunggal[5]),Integer.parseInt(datatunggal[6]));
                System.out.println("add item success");
                break;
            }
            if (inventoryData[baris][4].compareToIgnoreCase(datatunggal[4]) == 0 || inventoryData[baris][3].compareToIgnoreCase(datatunggal[3]) == 0){
                System.out.println("sorry your input data same with data ");
                break;
            }


        }
    }

    /**
     * method editItem
     * method for edit item in database
     * @param item_id item id
     * @param properties properties will be edited
     * @param data data will be changed
     */
    public static void editItem(int item_id,int properties,String data){

        inventoryData[item_id][properties] = data;
        saveData();
        loadData();
        System.out.println("Success edit item");
    }


    /**
     * method delitem()
     * for delete an item in inventory
     * @param modelName model name of a item
     */
    public static void delitem(String modelName){
        try {
            String[][] matriksTmp = new String[100][8];
            int row = 0;
            int tmp = totItem;
            int cek = 0;
            for (int baris = 0; baris < tmp; baris++) {
                int col = 0;
                if (inventoryData[baris][4].compareToIgnoreCase(modelName) == 0) {
                    totItem--;
                    cek++;
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
                for (int baris = 0; baris < totItem; baris++) {
                    for (int kolom = 0; kolom < inventoryData[baris].length; kolom++) {
                        inventoryData[baris][kolom] = matriksTmp[baris][kolom];
                    }
                }
            }

            if (cek > 0) {
                System.out.println("Success delete item \""+ modelName + "\"");
            }
            else {
                System.out.println("Sorry item \"" + modelName + "\" is not in the database");
            }
        }
        catch (final Exception e){
            System.out.println("Sorry cannot delete item :"+e);
        }

    }

    /**
     * function checkStock
     * used for show stock has been in database or not with position index on database and total of stock
     * @param modelName model name of item
     * @return array 1 dimension with element position index of stock and total of stock
     */
    public static int[] checkStock(String modelName){
        int cek = 0;
        int[] data = new int[2];
        if (inventoryData[0][0] != null) {
            for (cek = 0; cek < totItem; cek++) {
                if (inventoryData[cek][4].compareToIgnoreCase(modelName) == 0) {
                    data[0] = cek;
                    data[1] = Integer.parseInt(inventoryData[cek][5]);
                    return data;

                }
            }
        }

        //if data cannot find in database
        data[0] = -1;
        data[1] = 0;
        return data;


    }

    /**
     * method orderStock()
     * for order stock in a item on databse
     * @param item_id item id
     * @param stocknow stock available on item_id
     * @param quantity quantity stock will be order
     * @param unit_cost cost for per unit
     * @throws InterruptedException
     */
    public static void orderStock(int item_id, int stocknow, int quantity,int unit_cost) throws InterruptedException{

            inventoryData[item_id][5] = String.valueOf(stocknow + quantity);
            writeReport("order","ORDER_"+(totOrd+1),inventoryData[item_id][0],quantity,unit_cost);
            //loading
            char[] loading = {'|','/','-','\\','|','/','-','\\','|'};
            for (char kar : loading){
                System.out.print("\radding Stock "+kar);
                Thread.sleep(500);
            }
            System.out.println("\radding Stock \t (Success)");
        }

    /**
     * method sellStock
     * for sale stock in a item on database
     * @param item_id item id
     * @param stocknow stock available on item_id
     * @param quantity quantity stock will be order
     * @throws InterruptedException
     */
    public static void sellStock(int item_id, int stocknow, int quantity) throws InterruptedException{

        inventoryData[item_id][5] = String.valueOf(stocknow-quantity);
        writeReport("sell","SALES_"+(totSales+1),inventoryData[item_id][0],quantity,Integer.parseInt(inventoryData[item_id][7]));



    }



    /**
     * function getEOQ
     * @param modName
     * @return
     */
    /*public static double getEOQ(String modName){
        try {
            int[] data = checkStock(modName);

            return Math.sqrt((2 * Integer.parseInt(inventoryData[data[0]][6]) * Integer.parseInt(inventoryData[data[0]][8])) / Integer.parseInt(inventoryData[data[0]][7]));
        }
        catch (final Exception e){
            return 0;
        }
    }*/

    /**
     * method Main()
     * main method in program java
     *
     * @param args
     * @throws InterruptedException
     */

    public static void main(String[] args) throws InterruptedException,IOException {
        clsScreen();
        init();
        System.out.println("IMS (Inventory Management System) ");
        System.out.println("Running on system : "+System.getProperty("os.name"));
        System.out.println("Date now : "+dateFormat.format(date));
        boolean notExit = true;
        String command = "";
        System.out.println("Enter command , type \"?\" for help");

        while(notExit) {
            System.out.print("cmd =>");

            try {
                command = input.next();
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }

            if (command.compareToIgnoreCase("?") == 0) {
                clsScreen();
                System.out.println("Inventory Commands:");
                System.out.println("Cmd\tCommand\tParams\tDescription");
                System.out.println("~~~\t~~~~~~~\t~~~~~~\t~~~~~~~~~~~");
                System.out.println("list\tlistitems\t\tList all items on inventory");
                System.out.println("additem\tadd item\t\tAdd a item");
                System.out.println("delitem\tdelete item\t\tDelete a item");
                System.out.println("addstock\tadd stock item\t\tadd stock a item");
                System.out.println("sellstock\tsell item\t\tSell a item");
                //System.out.println("geteoq\tget best quantity\t\tQuantity item per order");
                System.out.println();
                System.out.println("Management Commands:");
                System.out.println("Cmd\tCommand\tParams\tDescription");
                System.out.println("~~~\t~~~~~~~\t~~~~~~\t~~~~~~~~~~~");
                System.out.println("?\thelp\t\tGet Help");
                System.out.println("savedata\tsave data\t\tSave data to Database");
                System.out.println("exit\texitapp\t\tExit the application");
            } else if (command.compareToIgnoreCase("list") == 0) {
                clsScreen();
                readInventory();
            } else if (command.compareToIgnoreCase("additem") == 0) {
                clsScreen();
                addItem();
            } else if (command.compareToIgnoreCase("edititem") == 0) {
                System.out.print("Input the Model Name : ");
                String modName = input.next();
                int[] hasil = checkStock(modName);
                if (hasil[0] >= 0){
                    System.out.println("1. Edit Category");
                    System.out.println("2. Edit Brand");
                    System.out.println("3. Edit Item Description");
                    System.out.println("4. Edit Model Number");
                    System.out.println("5. Edit Sales Price (Rp)");
                    System.out.print("Input your choice [1-5] : ");
                    int choice = input.nextInt();
                    input.nextLine();
                    switch (choice) {
                        case 1:
                            System.out.print("input category : ");
                            String ctg = input.nextLine();
                            System.out.println(hasil[0]);
                            editItem(hasil[0],1,ctg);
                            break;
                        case 2:
                            System.out.print("input brand : ");
                            String brd = input.nextLine();
                            editItem(hasil[0],2,brd);
                            break;
                        case 3:
                            System.out.print("input item description : ");
                            String idc = input.nextLine();
                            editItem(hasil[0],3,idc);
                            break;
                        case 4:
                            System.out.print("input model number : ");
                            String mdn = input.nextLine();
                            editItem(hasil[0],4,mdn);
                            break;
                        case 5:
                            try {
                                System.out.print("input Sales Price (Rp)");
                                int salepr = input.nextInt();
                                editItem(hasil[0], 7, String.valueOf(salepr));
                                break;
                            }catch (InputMismatchException exception){
                                System.out.println("Please input number !");
                            }
                        default:
                            System.out.print("Sorry your input is wrong");
                            break;
                    }
                } else {
                    System.out.println("Sorry your item cannot find in database");
                }

            } else if (command.compareToIgnoreCase("delitem") == 0) {
                System.out.print("Input the Model Name : ");
                String modName = input.next();
                delitem(modName);
            } else if (command.compareToIgnoreCase("detailitem") == 0){
                System.out.print("Input the Model Name : ");
                String modName = input.next();
                int[] hasil = checkStock(modName);
                if (hasil[0] >=0){
                    detailItem(hasil[0]);
                } else {
                    System.out.println("Sorry your item cannot find in database");
                }

            } else if (command.compareToIgnoreCase("orderstock") == 0) {

                int quantity = 0;
                int cost = 0;
                System.out.print("Input the Model Name : ");
                String modName = input.next();
                int[] hasil = checkStock(modName);
                if (hasil[0] >= 0) {
                    while (true) {
                        System.out.print("Input quantity will be order : ");
                        quantity = input.nextInt();
                        if (quantity < 0) {
                            System.out.println("\ryou cannot input minus, please input again !");
                            continue;
                        }
                        break;
                    }
                    while (true) {
                        System.out.print("Input unit cost : ");
                        cost = input.nextInt();
                        if (cost < 0) {
                            System.out.println("\ryou cannot input minus, please input again !");
                            continue;
                        }
                        break;
                    }
                    orderStock(hasil[0], hasil[1], quantity, cost);
                } else {
                    System.out.println("Item is nothing in database");
                }
            }
            else if(command.compareToIgnoreCase("sellstock") == 0){
                System.out.print("Input the Model Name : ");
                String modName = input.next();
                int[] hasil = checkStock(modName);
                if (hasil[0] >= 0 && hasil[1] > 0){
                    System.out.println("Number stocks of "+modName+" : "+hasil[1]);
                    while (input.hasNextLine()) {
                        System.out.print("Input quantity will be sell : ");
                        int quantity = input.nextInt();

                        //loading
                        char[] loading = {'|','/','-','\\','|','/','-','\\','|'};
                        for (char kar : loading){
                            System.out.print("\rsell Stock "+kar);
                            Thread.sleep(500);
                        }

                        if ((hasil[1]-quantity) < 0){
                            System.out.println("\rsell Stock "+quantity+"\t (Fail)");
                            System.out.println("Sorry your input make the stock minus, please input again !");
                            continue;
                        }
                        sellStock(hasil[0],hasil[1],quantity);
                        System.out.println("\rItem has been selled " + quantity + "\t (Success)");
                        break;
                    }
                }
                else if (hasil[1] == 0 ) {
                    System.out.println("The stock of item is empty");
                }
                else if (hasil[0] == -1 ){
                    System.out.println("Sorry the item cannot found in database");
                }
            }
            else if (command.compareToIgnoreCase("savedata") == 0) {
                saveData();
            }
            else if (command.compareToIgnoreCase("pwd") == 0){
                pwd();
            }
            else if(command.compareToIgnoreCase("exit") == 0){
                notExit = false;
            }
            //eoq in development
            /*else if(command.compareToIgnoreCase("geteoq") == 0){
                System.out.print("Input the Model Name :");
                input.nextLine();
                String modName = input.nextLine();
                Double eoq = getEOQ(modName);
                if (eoq != 0) {
                    System.out.printf("%.0f unit per order\n", eoq);
                }
                else {
                    System.out.println("sorry your item not found in database");
                }
            }*/
            else {
                System.out.println("Command not found");
            }
        }
        System.out.println("Thanks has using this Software");
    }
}

