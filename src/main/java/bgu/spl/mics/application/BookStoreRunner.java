package bgu.spl.mics.application;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.MicroServicesCounter;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public static void main(String[] args) {
        HashMap<Integer,Customer> customerHashMap = new HashMap<>();
        String filePath = args[0];
        String filecustomers = args[1];
        String fileInventory = args[2];
        String fileOrderReceipts = args[3];
        String fileMoneyRegister = args[4];

        //------------------------------reading from json file---------------------------------------------------------
        Gson gson = new Gson();
        List<Thread> threadList=new LinkedList<>();
        JsonParser jsonParser = new JsonParser();
        try {

            Object obj = jsonParser.parse(new FileReader(filePath));
            JsonObject jsonObject = (JsonObject) obj;
            JsonArray booksJarray = jsonObject.getAsJsonArray("initialInventory");
            BookInventoryInfo[] books = gson.fromJson(booksJarray, BookInventoryInfo[].class);// creating the books array for the store
            Inventory.getInstance().load(books);
            JsonArray vehicalesJarray = jsonObject.getAsJsonArray("initialResources").get(0).getAsJsonObject().getAsJsonArray("vehicles");
            DeliveryVehicle[] deliveryVehicles = gson.fromJson(vehicalesJarray, DeliveryVehicle[].class);// creating the vehicles array for the store
            ResourcesHolder.getInstance().load(deliveryVehicles);
            JsonObject servicesObject = jsonObject.getAsJsonObject("services");

// calling the function which return list of micro services from the json file
            List<MicroService> microServiceList = microServiceListCreator(servicesObject, gson);

            //create customers and API services and adding them to the micro services list
            JsonArray CustomersJarray = servicesObject.getAsJsonArray("customers");
            Customer[]customers= gson.fromJson(CustomersJarray,Customer[].class);
            for(int i =0 ; i<customers.length;i++){
                Customer temp = customers[i];
                temp.setCustomerReceiptList();// setting the receipt list for each customer instead of the Json
                customerHashMap.put(temp.getId(),temp);
                microServiceList.add(new APIService(customers[i],i+1));//creating API service for each customer
            }


//------------------------------------initialize the micro services----------------------------------------------------

            // initialize all the micro services except "time service"
            for(int i=0; i<microServiceList.size(); i++)
            {
                Thread t=new Thread(microServiceList.get(i));
                threadList.add(t);
                t.start();
            }
            // creating the "time service"  from the json file
            JsonObject timeObject = servicesObject.getAsJsonObject("time").getAsJsonObject();
            TimeService time = new TimeService(timeObject.get("speed").getAsInt(),timeObject.get("duration").getAsInt());
            Thread timeThread= new Thread(time);
            //as long as we didnt initialize all the micro services, we dont want to initialize the time service, therefore we are forcing the current
            // thread to sleep until all the services are finishing initialize
            while(MicroServicesCounter.getInstance().getMicroCounter()<microServiceList.size()){
                try {
                    Thread.currentThread().sleep(1000);
                }
                catch (InterruptedException e){}
            }
            //initialize the the time thread, only after all the services finished register, the time thread can initialize and start send tick broadcast (start the orders)
            threadList.add(timeThread);
            timeThread.start();

            // as long as the micro services did not terminate, we dont want to continue with the current thread(the main thread). therefore, we are using the method "join" for all the microservics.
            //so the main thread cant continue as long as all the services did not terminate themselves
            timeThread.join();
            for(int i=0;i<threadList.size();i++) {
                threadList.get(i).join();
            }

            print(filecustomers,customerHashMap);
            Inventory.getInstance().printInventoryToFile(fileInventory);
            MoneyRegister.getInstance().printOrderReceipts(fileOrderReceipts);
            print(fileMoneyRegister,MoneyRegister.getInstance());



        } catch (FileNotFoundException e) {
            System.out.println("fileNotFoundException");
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * function that create and return a list of all the  micro services (except time service and API services) by the gson file
     * <p>
     * @param  gson Gson
     * @param  services  JsonObject
     * @return List of micro services.
     */
    public static List<MicroService> microServiceListCreator(JsonObject services, Gson gson) {// creating all services
        List<MicroService> list = new LinkedList<>();
        for (int i = 1; i <= gson.fromJson(services.get("selling"), Integer.class); i++) {
            list.add(new SellingService(i));
        }
        for (int i = 1; i <= gson.fromJson(services.get("inventoryService"), Integer.class); i++) {
            list.add(new InventoryService(i));
        }
        for (int i = 1; i <= gson.fromJson(services.get("logistics"), Integer.class); i++) {
            list.add(new LogisticsService(i));
        }
        for (int i = 1; i <= gson.fromJson(services.get("resourcesService"), Integer.class); i++) {
            list.add(new ResourceService(i));
        }


        return list;
    }

    /**
     * serializing the object
     * <p>
     * @param  obj object
     * @param  file String
     */
    public static void print (String file , Object obj){
        try(FileOutputStream temp = new FileOutputStream(file); ObjectOutputStream toReturn = new ObjectOutputStream(temp)) {
            toReturn.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
