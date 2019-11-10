package oo;

public class Customer extends User {
    public Customer(String name, String idCard, String email, String phoneNumber, String address, String password, Integer userID) {
        super(name, idCard, email, phoneNumber, address, password,userID);
        _type = "Customer";
    }
}
