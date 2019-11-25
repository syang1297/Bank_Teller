package cs174a;    

public class ATM {

    private Customer customer;
    private Helper helper;

    ATM(){

    }

    //takes inserted pin and checks it against customer's getpin()
    boolean verifyPin(int pin){
        return false;
    }

    //checks if account can accept deposits and if so, deposits balance into accountID
    //if account is already marked as closed for the month, reject deposit
    //update account balance
    //check accountID belongs to customer and that it's checkings or savings account
    void deposit(double balance, int accountID){

    }

    //checks accountID is for a pocket account
    //takes specified amount of money out of the account it's linked w/
    //make sure the account it's linked w/ won't close after money is moved (reject if so)
    //return if money transfer was successful or not
    //check accountID belongs to customer
    //check if feePaid, if not, add $5
    boolean topUp(int accountID, double amount){
        return false;
    }

    //subtracts amount from account associated w/ accountID
    //check that there is enough money in the account
    //mark account for closing if it's less than $.01
    //check accountID belongs to customer and account is checkings or savings
    boolean withdraw(int accountID, double amount){
        return false;
    }

    //check accountID belongs to customer and is a pocket account
    //subtract amount from account balance
    //check if feepaid, if not add $5
    boolean purchase(int accountID, double amount){
        return false;
    }

    //check both accounts belongs to customer
    //check that both accounts are either checkings or savings
    //check amount is able to be moved and mark account as closed if it needs to be
    //also make sure init account is not marked for closed
    //subtract amount from accountID and add to destination account
    //return is successful or not successful
    boolean transfer(int accountID, int destinationID, double amount){
        return false;
    }

    //move amount from pocket account into account
    //make sure account is the one linked w/ that pocket account
    //check amount is not more than pocket account balance
    //apply 3% fee
    boolean collect(int accountID, int pocketID, double amount){
        return false;
    }

    //make sure current customer owns accountID and both accounts are checkings/savings
    //apply 2% fee
    //subtracts amount from accountID and adds to destinationId
    boolean wire(int accountID, int destinationID, double amount){
        return false;
    }

    //make sure both accounts are pocket accounts or else return false
    //take amount out of pocketID and add to friend TaxID
    boolean payFriend(int pocketID, int friendTaxID, int friendID, double amount){
        return false;
    }


}