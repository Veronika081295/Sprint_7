package praktikum;

import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;

public class CourierAssertions {
    public void assertCreateCourierOkResponse(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_CREATED)
                .body("ok", is(true));
    }

    public void assertCreateDuplicatedCourier(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    public void assertCreateCourierWithInvalidData(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    public void assertSuccessfulCourierLoginAndTakeId(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_OK)
                .body("id", greaterThan(0))
                .extract()
                .path("id");
    }

    public void assertErrorLoginCourierWithInvalidCredentials(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    public void assertErrorLoginCourierWithoutCredentials(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }
}
