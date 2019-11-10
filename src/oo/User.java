package oo;

import Projet.Main;

import java.util.ArrayList;

public class User implements Comparable{
    private String _name;
    private String _idCard;
    private final Integer _userID;
    private String _email;
    private String _phoneNumber;
    private String _address;
    private String _password;
    protected String _type;

    public User(String name, String idCard, String email, String phoneNumber, String address, String password, Integer userID) {
        this.setName(name);
        this.setIdCard(idCard);
        this.setEmail(email);
        this.setPhoneNumber(phoneNumber);
        this._address = address;
        this._password = password;
        _userID = userID;
        _type = "";
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        if (!_name.isEmpty()) {
            this._name = _name;
        }
    }

    public String getIdCard() {
        return _idCard;
    }

    public void setIdCard(String _idCard) {
        if (!_idCard.isEmpty()) {
            this._idCard = _idCard;
        }
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String _email) {
        this._email = _email;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() == 10) {
            this._phoneNumber = phoneNumber;
        }
    }

    public String getAddress() {
        return _address;
    }

    public void setAddress(String address) {
        this._address = address;
    }

    public boolean checkPassword(String password) {
        return _password.equals(password);
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getPassword() {
        return _password;
    }

    public Integer getUserID() {
        return _userID;
    }

    public String getType() {return _type;}

    /**
     * Search first occurrence of an user with mail
     * @param list ArrayList to look in
     * @param email String of email
     * @param <T> Inherited from User
     * @return User or null
     */
    public static <T extends User> T searchListMail(ArrayList<T> list, String email) {
        for (T u : list) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public int compareTo(Object o) {
        if(((User) o).getUserID() < _userID) {
            return 1;
        } else if (((User) o).getUserID() > _userID) {
            return -1;
        }
        return 0;
    }

    /**
     * Search an user by its ID
     * @param list ArrayList of users
     * @param IDCard String of IDCard to look for
     * @param <T> Inherited of User
     * @return User or null
     */
    public static <T extends User> T searchListIDCard(ArrayList<T> list, String IDCard) {
        for (T u : list) {
            if (u.getIdCard().equals(IDCard)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Search an user in a list by its ID
     * @param list ArrayList to search in
     * @param idUser Integer ID of user to search for
     * @param <T> Inherited of User
     * @return User, User(Deleted), null
     */
    public static <T extends User> T searchListIDUser(ArrayList<T> list, Integer idUser) {
        for (T u : list) {
            if (u.getUserID().equals(idUser)) {
                return u;
            }
        }

        if(idUser <= Main.parameters.getNumberUserCreated()) {
            return (T) new User("Deleted","Deleted","Deleted","Deleted","Deleted","Deleted",idUser);
        } else {
            return null;
        }
    }

    /**
     * Return list of user sharing an email address
     * @param list ArrayList to look in
     * @param <T> Inherited of User
     * @return ArrayList of users
     */
    public static <T extends User> ArrayList<T> searchOccurrenceStringMail(ArrayList<T> list, String email) {
        ArrayList<T> mailList = new ArrayList<>();
        for (T t: list) {
            if(t.getEmail().contains(email))
                mailList.add(t);
        }
        return mailList;
    }
}
