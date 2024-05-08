import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import praktikum.GetOrdersList;
import praktikum.OrderSteps;

import static praktikum.RestApiConstants.GET_ORDERS_LIST;

@DisplayName("Get Orders List Tests")
public class GetOrdersListTests {
    private OrderSteps orderSteps;
    private GetOrdersList getOrdersList = new GetOrdersList();

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
    }

    @DisplayName("Get Orders List Without CourierId")
    @Description("Successfully returned list of orders")
    @Test
    public void getOrdersListWithoutCourierIdTest() {
        Allure.step("Sending request to get orders list");
        ValidatableResponse response = orderSteps.getOrdersList();
        GetOrdersList getOrdersList = new GetOrdersList();
        getOrdersList.getOrdersListWithoutCourierId(response);
        Allure.step("Successfully retrieved orders list");
        int ordersCount = response.extract().path("orders.size()");
        Allure.step("Number of orders received: " + ordersCount);
    }
    @DisplayName("Get Orders List When Invalid CourierID")
    @Description("Check error message when invalid CourierID")
    @Test
    public void getOrdersListWithInvalidCourierIdTest() {
        Allure.step("Sending request to get orders list with invalid CourierID");
        System.out.println("Request URL: " + GET_ORDERS_LIST);
        ValidatableResponse response = orderSteps.getOrdersListWithInvalidCourierId(9999);
        getOrdersList.getOrdersListWithInvalidCourierId(response, 9999);
        Allure.step("Received response status code: " + response.extract().statusCode());
        Allure.step("Response Body: " + response.extract().body().asString());
        Allure.step("Error message received when invalid CourierID");
    }
}
