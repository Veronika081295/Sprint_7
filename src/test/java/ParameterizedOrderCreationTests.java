import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.OrderInfo;
import praktikum.OrderSteps;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

@DisplayName("Order Creation Tests")
@RunWith(Parameterized.class)
public class ParameterizedOrderCreationTests {
    private final List<String> color;
    private OrderSteps orderSteps;
    private int track;

    public ParameterizedOrderCreationTests(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUp() {
        orderSteps = new OrderSteps();
    }

    @After
    public void cleanUp() {
        orderSteps.cancelOrder(track);
        Allure.step("Canceled order with track ID: " + track);
    }

    @Parameterized.Parameters(name = "{index}: Color={0}")
    public static Object[][] selectScooterColor() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("BLACK", "GREY")},
                {List.of("GREY")},
                {List.of()}
        };
    }
    @DisplayName("Create New Order")
    @Description("Verify successfully created order")
    @Test
    public void createNewOrderTest() {
        Allure.parameter("Color", color.toString());
        OrderInfo orderInfo = new OrderInfo(color);
        ValidatableResponse response = orderSteps.createOrder(orderInfo);
        track = response.extract().path("track");
        response.assertThat().statusCode(201).body("track", is(notNullValue()));
        Allure.step("Created new order with track ID: " + track + " for color: " + color);
    }
    }
