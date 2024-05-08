package praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static praktikum.RestApiConstants.*;

public class OrderSteps {
    public static RequestSpecification requestSpecification() {
        return given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    public ValidatableResponse createOrder(OrderInfo orderInfo) {
        return requestSpecification()
                .when()
                .post(POST_ORDER_CREATE)
                .then();
    }

    public ValidatableResponse cancelOrder(int track) {
        return requestSpecification()
                .body(track)
                .when()
                .put(CANCEL_ORDER)
                .then();
    }

    public ValidatableResponse getOrdersList() {
        return requestSpecification()
                .when()
                .get(GET_ORDERS_LIST)
                .then();
    }
    public ValidatableResponse getOrdersListWithInvalidCourierId(int courierId) {
        return requestSpecification()
                .queryParam("courierId", courierId)
                .when()
                .get(GET_ORDERS_LIST)
                .then();
    }
}
