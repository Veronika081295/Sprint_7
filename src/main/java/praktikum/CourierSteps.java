package praktikum;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.given;
import static praktikum.RestApiConstants.*;

public class CourierSteps {
    public static RequestSpecification requestSpec() {
        return given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL);
    }

    public ValidatableResponse createCourier(CourierInfo courierInfo) {
        return requestSpec()
                .body(courierInfo)
                .when()
                .post(POST_COURIER_CREATE)
                .then()
                .log()
                .all();
    }

    public ValidatableResponse courierAuthorization(CourierLoginCredentials courierInfo) {
        return requestSpec()
                .body(courierInfo)
                .when()
                .post(POST_COURIER_LOGIN)
                .then()
                .log()
                .all();
    }


    public ValidatableResponse deleteCourier(int courierId) {
        return requestSpec()
                .when()
                .delete(DELETE_COURIER + courierId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("ok", equalTo(true));
    }
}
