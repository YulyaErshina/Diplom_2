package praktikum.user;

import praktikum.data.DataForCreateNewUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.client.UserClient;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CreateUserWithoutRequiredFieldsTest {

    DataForCreateNewUser user;
    private UserClient userClient;
    private final int expectedStatus;
    private final boolean expectedSuccess;
    private final String expectedErrorTextMessage;

    public CreateUserWithoutRequiredFieldsTest(DataForCreateNewUser user, int expectedStatus, boolean expectedSuccess, String expectedErrorTextMessage) {
        this.user = user;
        this.expectedStatus = expectedStatus;
        this.expectedSuccess = expectedSuccess;
        this.expectedErrorTextMessage = expectedErrorTextMessage;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {DataForCreateNewUser.getWithEmailAndPassword(), 403, false, "Email, password and name are required fields"},
                {DataForCreateNewUser.getWithPasswordAndName(), 403, false, "Email, password and name are required fields"},
                {DataForCreateNewUser.getWithEmailAndName(), 403, false, "Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Создание пользователя без заполнения одного из обязательных полей")
    @Description("Создание юзера только с: " +
            "1. Email и пароль " +
            "2. Пароль и имя пользователя " +
            "3. Email и имя пользователя")
    public void createUsersWithoutOneRequiredField() {

        //Создать пользователя
        ValidatableResponse response = userClient.create(user);

        //Получить статус кода
        int statusCode = response.extract().statusCode();
        //Проверить статус код
        assertThat(statusCode, equalTo(expectedStatus));
        //Получить значение ключа "success"
        boolean actualSuccess = response.extract().path("success");
        //Проверить значение ключа "success"
        assertThat(actualSuccess, equalTo(expectedSuccess));
        //Получить значение ключа "message"
        String errorTextMessage = response.extract().path("message");
        //Проверить значение ключа "message"
        assertThat(errorTextMessage, equalTo(expectedErrorTextMessage));
    }
}
