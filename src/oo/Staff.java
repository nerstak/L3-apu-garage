package oo;

public abstract class Staff extends User {
    private String _department;

    public Staff(String name, String idCard, String email, String phoneNumber, String address, String password, Integer userID, String _department) {
        super(name, idCard, email, phoneNumber, address, password, userID);
        this._department = _department;
    }

    /**
     * Get Department of staff
     * @return String Department
     */
    public String getDepartment() {
        return _department;
    }
    /**
     * Set Department of staff
     * @param _department String Department
     */
    public void setDepartment(String _department) {
        this._department = _department;
    }
}
