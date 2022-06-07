package praktikum.user;

import praktikum.data.DataForCreateNewUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.data.UserCredentials;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class LoginUserTests {

    private UserClient userClient;
    private DataForCreateNewUser user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        //Сгенерировать случайные данные полей
        user = DataForCreateNewUser.getRandom();
    }

    @After
    public void tearDown() {
        //Удалить пользователя
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Успешная авторизация юзера")
    @Description("Юзер успешно авторизовался с верно заполнеными полями email и пароль")
    public void successfulAuthorization() {

        //Создать пользователя
        ValidatableResponse createResponse = userClient.create(user);

        //Проверить статус код
        assertThat(createResponse.extract().statusCode(), equalTo(200));

        //Авторизоваться пользователем
        ValidatableResponse response = userClient.login(new UserCredentials(user.email, user.password));

        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Проверить статус код
        assertThat(statusCodeResponse, equalTo(200));
        //Получить значение ключа "success"
        boolean isUserLogged = response.extract().path("success");
        //Проверить значение ключа "success"
        assertTrue("Пользователь не авторизовался", isUserLogged);
        //Получить access токен
        accessToken = response.extract().path("accessToken");
        //Проверить access токен
        assertNotNull(accessToken);
        //Получить refresh токен
        String refreshToken = response.extract().path("refreshToken");
        //Проверить refresh токен
        assertNotNull(refreshToken);
        //Получить значение ключа "email"
        String actualEmail = response.extract().path("user.email");
        //Проверить значение ключа "email"
        assertThat("Пользователь авторизовался под другим email", actualEmail, equalTo(user.email));
        //Получить значение ключа "name"
        String actualName = response.extract().path("user.name");
        //Проверить значение ключа "name"
        assertThat("Пользователь авторизовался под другим name", actualName, equalTo(user.name));
    }

    @Test
    @DisplayName("Неуспешная авторизация юзера")
    @Description("Юзер не может авторизоваться с некорректно заполненными полями email и пароль")
    public void unsuccessfulAuthorizationWithDoNotReallyEmailAndPassword() {

        //Авторизоваться с данными незарегистрированного пользователя
        ValidatableResponse response = userClient.login(UserCredentials.getWithDoNotReallyEmailAndPassword(user));

        //Получить статус код запроса
        int statusCodeResponse = response.extract().statusCode();
        //Получить значение ключа "success"
        boolean isUserUnLogged = response.extract().path("success");
        //Получить значение ключа "message"
        String message = response.extract().path("message");

        //Проверить статус код
        assertThat(statusCodeResponse, equalTo(401));
        //Проверить значение ключа "success"
        assertFalse(isUserUnLogged);
        //Проверить значение ключа "message"
        assertThat(message, equalTo("email or password are incorrect"));
    }
}
