package praktikum;

public class CourierLoginCredentials {
    private String login;
    private String password;

    public CourierLoginCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }
    public static CourierLoginCredentials from(CourierInfo courierInfo) {
        return new CourierLoginCredentials(courierInfo.getLogin(), courierInfo.getPassword());
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
