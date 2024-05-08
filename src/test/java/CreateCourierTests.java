import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.*;

@DisplayName("Create Courier Tests")
public class CreateCourierTests {
    int courierId;
    protected CourierRandomDataGenerator courierRandomDataGenerator = new CourierRandomDataGenerator();
    private CourierInfo courierInfo;
    protected CourierSteps courierSteps;
    private CourierAssertions courierAssertions;

    @Before
    @Step("Courier random data generation")
    public void setUp() {
        courierSteps = new CourierSteps();
        courierInfo = courierRandomDataGenerator.generateRandomDataForCourierCreation();
        Allure.step("Generated courier data: Login=" + courierInfo.getLogin() + ", Name=" + courierInfo.getFirstname() + ", Password=" + courierInfo.getPassword());
        courierAssertions = new CourierAssertions();
    }

    @After
    public void cleanData() {
        if (courierId != 0) {
            Allure.step("Deleting courier with ID: " + courierId);
            courierSteps.deleteCourier(courierId);
        } else {
            Allure.step("No courier to delete (ID is 0)");
        }
    }

    @DisplayName("Create New Courier")
    @Description("Verify successful creation of a new courier")
    @Test
    public void createNewCourierTest() {
        ValidatableResponse responseCourierCreate = courierSteps.createCourier(courierInfo);
        CourierLoginCredentials courierLoginCredentials = CourierLoginCredentials.from(courierInfo);
        courierId = courierSteps.courierAuthorization(courierLoginCredentials).extract().path("id");
        Allure.step("Assert courier creation");
        courierAssertions.assertCreateCourierOkResponse(responseCourierCreate);
    }

    @DisplayName("Create Courier Without Login")
    @Description("Verify error when creating courier without login")
    @Test
    public void createCourierWithoutLoginTest() {
        courierInfo.setLogin(null);
        ValidatableResponse response = courierSteps.createCourier(courierInfo);
        Allure.step("Assert courier creation without login");
        courierAssertions.assertCreateCourierWithInvalidData(response);
    }

    @DisplayName("Create Courier Without Password")
    @Description("Verify error when creating courier without password")
    @Test
    public void createCourierWithoutPasswordTest() {
        courierInfo.setPassword(null);
        ValidatableResponse response = courierSteps.createCourier(courierInfo);
        Allure.step("Assert courier creation without password");
        courierAssertions.assertCreateCourierWithInvalidData(response);
    }

    @DisplayName("Create Courier With Empty Login and Password")
    @Description("Verify error when creating courier with empty login and password")
    @Test
    public void createCourierWithoutPasswordAndLoginTest() {
        courierInfo.setPassword(null);
        courierInfo.setLogin(null);
        ValidatableResponse response = courierSteps.createCourier(courierInfo);
        Allure.step("Assert courier creation with empty login and password");
        courierAssertions.assertCreateCourierWithInvalidData(response);
    }

    @Test
    @DisplayName("Fail to Create Duplicated Courier")
    @Description("Verify error when creating an existing courier")
    public void createCourierWithExistingLoginTest() {
        courierSteps.createCourier(courierInfo);
        ValidatableResponse response = courierSteps.createCourier(courierInfo);
        Allure.step("Assert duplicated courier creation");
        courierAssertions.assertCreateDuplicatedCourier(response);
    }
}
