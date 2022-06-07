package praktikum.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.client.UserClient;
import praktikum.data.DataForCreateNewUser;
import praktikum.data.UserCredentials;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

public class ChangingUserDataTests {

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
    @DisplayName("Изменить все данные пользователя, когда он авторизован")
    @Description("Успешное изменение всех полей у пользователя, когда он авторизован")
    public void successfulChangeDataUserEmailAndNameAndPasswordWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать случайные данные полей
        DataForCreateNewUser newUserData = DataForCreateNewUser.getRandom();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(newUserData.email, newUserData.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();

        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(newUserData.email));
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(newUserData.name));
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Изменение email")
    @Description("Успешное изменение email, когда пользователь авторизован")
    public void successfulChangeDataUserEmailWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать данные поля "email"
        DataForCreateNewUser newUserData = DataForCreateNewUser.getEmail();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(newUserData.email));
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(user.name));

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(newUserData.email, user.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Изменение пароля")
    @Description("Успешное изменение пароля, когда пользователь авторизован")
    public void successfulChangeDataUserPasswordWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать данные поля "password"
        DataForCreateNewUser newUserData = DataForCreateNewUser.getPassword();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(user.email));
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(user.name));

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(user.email, newUserData.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Изменение имени пользователя")
    @Description("Успешное изменение имени пользователя, когда он авторизован")
    public void successfulChangeDataUserNameWithLogin() {

        //Создать пользователя
        ValidatableResponse responseCreatedUser = userClient.create(user);

        //Получить access токен
        accessToken = responseCreatedUser.extract().path("accessToken");

        //Сгенерировать данные поля "name"
        DataForCreateNewUser newUserData = DataForCreateNewUser.getName();
        //Изменить данные пользователя
        ValidatableResponse responseChangeData = userClient.changeData(newUserData, accessToken);
        //Получить значение ключа "email"
        String actualEmail = responseChangeData.extract().path("user.email");
        //Проверить значение ключа "email"
        assertThat("У пользователя не изменились данные email", actualEmail, equalTo(user.email));
        //Получить значение ключа "name"
        String actualName = responseChangeData.extract().path("user.name");
        //Проверить значение ключа "name"
        assertThat("У пользователя не изменились данные name", actualName, equalTo(newUserData.name));

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(user.email, user.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeData.extract().statusCode();
        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(200));
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(200));
    }

    @Test
    @DisplayName("Изменение данных юзера, когда он не авторизован")
    @Description("Когда пользователь не авторизован, изменение его данных невозможно")
    public void unsuccessfulChangeDataUserEmailAndNameAndPasswordWithoutLogin() {

        //Создать пользователя
        userClient.create(user);

        //Сгенерировать случайные данные полей
        DataForCreateNewUser newUserData = DataForCreateNewUser.getRandom();
        //Изменить данные пользователя
        ValidatableResponse responseChangeDataWithoutToken = userClient.changeDataWithoutToken(newUserData);
        //Получить значение ключа "success"
        boolean isEmail = responseChangeDataWithoutToken.extract().path("success");
        //Проверить значение ключа "email"
        assertFalse(isEmail);
        //Получить значение ключа "name"
        String message = responseChangeDataWithoutToken.extract().path("message");
        //Проверить значение ключа "message"
        assertThat(message, equalTo("You should be authorised"));

        //Авторизоваться пользователем c новыми данными
        ValidatableResponse responseLoginWithNewData = userClient.login(new UserCredentials(newUserData.email, newUserData.password));

        //Получить статус код запроса на изменение данных
        int statusCodeResponseChangeData = responseChangeDataWithoutToken.extract().statusCode();
        //Проверить статус код запроса на изменение данных
        assertThat(statusCodeResponseChangeData, equalTo(401));
        //Получить статус код запроса на авторизацию с новыми данными
        int statusCodeResponseLoginWithNewData = responseLoginWithNewData.extract().statusCode();
        //Проверить статус код запроса на авторизацию с измененными данными
        assertThat(statusCodeResponseLoginWithNewData, equalTo(401));
    }
}