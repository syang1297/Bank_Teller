package cs174a;    
import java.util.*;
import cs174a.Helper.*;

public class Customer {
    private int taxID;
    private Helper helper;
    
    //constructor
    Customer(int tax){
        this.taxID = tax;
    }

    //taxID getter
    int getTaxID(){
        return taxID;
    }


    //get name from db for the customer
    String getName(){
        return "stub";
    }

    //get address from db for customer
    String getAddress(){
        return "stub";
    }

    //get pin from db for customer and unhash
    int getPin(){
        return 0;
    }

    //set pin and hash and store to db for customer
    void setPin(int pin){
        return;
    }

    //set address and store to db for customer
    void setAddress(String addr){
        return;
    }

    //set name and store to db for customer
    void setName(String name){
        return;
    }

    //get accounts associated with customers taxID
    List<Integer> getAccountIDs(int taxID, String type){
        return null;
    }

    //set global date in db
    void setDate(Date date){
        return;
    }

}