import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.*;

@DisplayName("Courier Login Tests")
public class CourierLoginTests {
    int courierId;
    protected CourierRandomDataGenerator courierRandomDataGenerator = new CourierRandomDataGenerator();
    private CourierInfo courierInfo;
    protected CourierSteps courierSteps;
    private CourierAssertions courierAssertions;
    private CourierLoginCredentials courierLoginCredentials;

    @Before
    @Step("Create random courier and get his ID")
    public void setUp() {
        courierSteps = new CourierSteps();
        courierInfo = courierRandomDataGenerator.generateRandomDataForCourierCreation();
        courierSteps.createCourier(courierInfo);
        courierLoginCredentials = CourierLoginCredentials.from(courierInfo);
        ValidatableResponse courierLoginResponse = courierSteps.courierAuthorization(courierLoginCredentials);
        courierId = courierLoginResponse.extract().path("id");
        courierAssertions = new CourierAssertions();
    }

    @After
    public void cleanData() {
        if (courierId != 0) {
            Allure.step("Deleting created courier with ID: " + courierId);
            if (isNumeric(courierId)) {
                courierSteps.deleteCourier(courierId);
            } else {
                Allure.step("Invalid courier ID format: " + courierId);
            }
        } else {
            Allure.step("No created courier to delete (ID is 0)");
        }
    }

    private boolean isNumeric(int str) {
        return String.valueOf(str).chars().allMatch(Character::isDigit);
    }


    @DisplayName("Successful Courier Login")
    @Description("Verify successful login")
    @Test
    public void successfulCourierLoginTest() {
        ValidatableResponse courierAuthorization = courierSteps.courierAuthorization(courierLoginCredentials);
        courierId = courierAuthorization.extract().path("id");
        Allure.step("Assert successful courier login for: (courier ID=" + courierId +")");
        courierAssertions.assertSuccessfulCourierLoginAndTakeId(courierAuthorization);
    }

    @DisplayName("Courier Login with Missing Data")
    @Description("Verify error when logging in with empty fields")
    @Test
    public void courierLoginWithMissingDataTest() {
        Allure.step("Attempt to login with missing data: (courier ID=" + courierId +")");
        ValidatableResponse courierLogin = courierSteps.courierAuthorization(new CourierLoginCredentials("", ""));
        Allure.step("Assert error login with missing data: (courier ID=" + courierId +")");
        courierAssertions.assertErrorLoginCourierWithoutCredentials(courierLogin);
    }

    @DisplayName("Login with Non-Existing Data")
    @Description("Verify error when logging in with non-existing data")
    @Test
    public void loginWithNonExistingDataTest() {
        Allure.step("Attempt to login with non-existing data: (courier ID=" + courierId +")");
        ValidatableResponse courierLogin = courierSteps.courierAuthorization(new CourierLoginCredentials("non-existing-login", "non-existing-password"));
        Allure.step("Assert error login with non-existing data: (courier ID=" + courierId +")");
        courierAssertions.assertErrorLoginCourierWithInvalidCredentials(courierLogin);
    }

    @DisplayName("Login with Empty Login")
    @Description("Verify error when logging in with empty login")
    @Test
    public void loginWithEmptyLoginTest() {
        Allure.step("Attempt to login with empty login: (courier ID=" + courierId +")");
        ValidatableResponse courierLogin = courierSteps.courierAuthorization(new CourierLoginCredentials("", courierInfo.getPassword()));
        Allure.step("Assert error login with empty login: (courier ID=" + courierId +")");
        courierAssertions.assertErrorLoginCourierWithoutCredentials(courierLogin);
    }

    @DisplayName("Login with Empty Password")
    @Description("Verify error when logging in with empty password")
    @Test
    public void loginWithEmptyPasswordTest() {
        Allure.step("Attempt to login with empty password: (courier ID=" + courierId +")");
        ValidatableResponse courierLogin = courierSteps.courierAuthorization(new CourierLoginCredentials(courierInfo.getLogin(), ""));
        Allure.step("Assert error login with empty password: (courier ID=" + courierId +")");
        courierAssertions.assertErrorLoginCourierWithoutCredentials(courierLogin);
    }
}
