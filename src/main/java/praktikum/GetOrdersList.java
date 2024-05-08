package praktikum;

import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class GetOrdersList {
    public void getOrdersListWithoutCourierId(ValidatableResponse response) {
        System.out.println("Received response status code: " + response.extract().statusCode());

        response.assertThat()
                .statusCode(SC_OK)
                .body("orders", notNullValue());

        int ordersCount = response.extract().path("orders.size()");
        System.out.println("Number of orders received: " + ordersCount);

        boolean hasOrderId = false;

        for (int i = 0; i < ordersCount; i++) {
            Integer orderId = response.extract().path("orders[" + i + "].id");
            if (orderId != null) {
                hasOrderId = true;
                break;
            }
        }

        if (!hasOrderId) {
            throw new AssertionError("No order with ID found in the response");
        }
    }

    public void getOrdersListWithInvalidCourierId(ValidatableResponse response, int courierId) {
        System.out.println("Received response status code: " + response.extract().statusCode());

        // Check if the status code is 404
        if (response.extract().statusCode() == SC_NOT_FOUND) {
            // Assert the error message for invalid courier ID
            response.assertThat()
                    .body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"));
        } else {
            // If the status code is not 404, then it's an unexpected response
            throw new AssertionError("Unexpected response for invalid courier ID");
        }
    }
}
