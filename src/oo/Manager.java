package oo;

public class Manager extends Staff {
    public Manager(String name, String idCard, String email, String phoneNumber, String address, String password, String department, Integer userID) {
        super(name, idCard, email, phoneNumber, address, password,userID,department);
        _type = "Manager";
    }
}
