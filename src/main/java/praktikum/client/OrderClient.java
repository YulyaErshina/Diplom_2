package praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.data.IngredientsForCreateNewBurger;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {

    private static final String ORDER_PATH = "/api/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(IngredientsForCreateNewBurger ingredientsForCreateNewBurger, String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .body(ingredientsForCreateNewBurger)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получения списка заказов, когда пользователь авторизован")
    public ValidatableResponse userOrderInfo(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получения списка заказов, когда пользователь не авторизован")
    public ValidatableResponse userOrderInfoWithoutToken() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }
}