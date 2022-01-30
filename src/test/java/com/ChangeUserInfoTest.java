package com;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static com.UserCredentials.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;


public class ChangeUserInfoTest {

    int success200StatusCode = 200;
    int error403StatusCode = 403;
    String expectedErrorMessage = "User with such email already exists";

    private User user;
    private UserClient userClient;
    String bearerToken;

   // public ChangingDataClient changingDataClient;
   // private static SpaсeUserClient spaсeUserClient;
   // public SpaceUser spaceUser = SpaceUser.getRandom();
   // private static LoginUserClient loginUserClient;
    public String accessToken;
    public String accessUserToken;
    public String refreshToken;
    public User expectedData;
    public ValidatableResponse createdUser;

    // Создание рандомного пользователя
    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        accessToken = userClient.create(user).extract().path("accessToken");
        accessToken = accessToken.substring(7);
        // public SpaceUser expectedData;
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    // РАБОТАЕТ
    @Test
    @DisplayName("Проверяет, что можно изменить EMAIL пользователя")
    public void checkUserEmailCanBeEdited() {
        int expectedStatusCode = 200;
        ValidatableResponse response = userClient.userInfoChange(accessToken, user.setEmail(getUserEmail()));

        int actualStatusCode = response.extract().statusCode();
        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
       // assertThat("Status code is not 200", statusCode, equalTo(200));

        boolean isUserEmailEdited = response.extract().path("success");
        assertTrue("Email пользователя не обновился", isUserEmailEdited);

        String expectedUserEmail = user.getEmail();
        String actualUserEmail = response.extract().path("user.email");
        assertEquals("Ожидаемый email " + expectedUserEmail + ". Фактический " + actualUserEmail,
                expectedUserEmail, actualUserEmail);
    }

    // РАБОТАЕТ
    @Test
    @DisplayName("Проверяет, что можно изменить PASSWORD пользователя")
    public void checkUserPasswordCanBeEdited() {
        int expectedStatusCode = 200;
        ValidatableResponse response = userClient.userInfoChange(accessToken, user.setPassword(getUserPassword()));

        int actualStatusCode = response.extract().statusCode();
        assertEquals("Ожиданемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
        // assertThat("Status code is not 200", statusCode, equalTo(200));

        boolean isUserPasswordUpdated = response.extract().path("success");
        assertTrue("Пароль пользователя не обновился", isUserPasswordUpdated);
    }

    // РАБОТАЕТ
    @Test
    @DisplayName("Проверяет, что можно изменить NAME пользователя")
    public void checkUserNameCanBeEdited() {
        int expectedStatusCode = 200;
        ValidatableResponse response = userClient.userInfoChange(accessToken, user.setName(getUserName()));

        int actualStatusCode = response.extract().statusCode();
        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
        // assertThat("Status code is not 200", statusCode, equalTo(200));

        boolean isUserNameEdited = response.extract().path("success");
        assertTrue("Имя пользователя не обновилось", isUserNameEdited);

        String expectedName = user.getName();
        String actualName = response.extract().path("user.name");
        assertEquals("Ожидаемое имя " + expectedName + ". Фактическое " + actualName, expectedName, actualName);
    }

    @Test
    @DisplayName("Меняет сразу все данные пользователя: name, email, password")
    public void editingAllUserData() {
        // Создание пользователя, авторизуемся и получаем его токен
        userClient.create(user);
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        bearerToken = login.extract().path("accessToken");

        // меняем password, email, and name
        ValidatableResponse editedUser = userClient.editInfo(UserCredentials.getUserWithPasswordEmailAndName(user),
                bearerToken);

        // достаем статус код и значение поля success
        int actualCode = editedUser.extract().statusCode();
        boolean isSuccessTrue = editedUser.extract().path("success");

        assertEquals("Ожидаемый статус код " + success200StatusCode + ". Фактический " + actualCode,
                success200StatusCode, actualCode);
        assertTrue("Должно вернуться true, но возвращается false", isSuccessTrue);
    }

    // РАБОТАЕТ!!!
    @Test
    @Description("Проверяет, что неавторизованный пользователь не может менять информацию о себе ")
    public void userInfoCanNotBeChangedWithoutAuthorizationNegativeTest() {
        // bearerToken = "";
        String expectedErrorMessage = "You should be authorised";
        int expectedStatusCode = 401;

        userClient.create(user);
        // Получение информации о пользователе
        // ValidatableResponse info = userClient.userInfoChange(bearerToken, User.getRandom());
        ValidatableResponse info = userClient.editInfoWithoutToken(user);

        // Получение статус код, значение поля success и сообщение об ошибке
        int actualStatusCode = info.extract().statusCode();
        boolean getUserInfo = info.extract().path("success");
        String actualErrorMessage = info.extract().path("message");

        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
        assertFalse("Ожидаемый ответ false, по факту true", getUserInfo);
        assertEquals("Ожидаемое сообщение об ошибке " + expectedErrorMessage + ". Фактическое " + actualErrorMessage,
                expectedErrorMessage, actualErrorMessage);
    }

    //--------------------------------------------------------------------------------------

    //
//    @Test
//    @Description("Изменение данных пользователя. Одинаковый емаил")
//    public void userInfoCanNotBeChangedWithSameEmailNegativeTest() {
//
//        // Создание пользователя
//        userClient.create(user);
//        // Авторизация пользователя
//        ValidatableResponse login = userClient.login(UserCredentials.from(user));
//        // Получение токена пользователя
//        bearerToken = login.extract().path("accessToken");
//        // Получение информации о пользователе
//        ValidatableResponse info = userClient.editInfo(UserCredentials.getUserWithEmail(user), bearerToken);
//        // Получение статус кода из тела информации о пользователе
//        int actualStatusCode = info.extract().statusCode();
//        // Получение тела ответа при запросе информации о пользователе
//        boolean isSuccessFieldFalse = info.extract().path("success");
//        String actualErrorMessage = info.extract().path("message");
//
//        // Проверка что статус код соответсвует ожиданиям
//        System.out.println(actualStatusCode);
//        assertEquals("Expected error message is 403. But actual is " + actualStatusCode,
//                error403StatusCode, actualStatusCode);
//        // assertThat("Status code is incorrect", statusCode, equalTo(403));
//        // Проверка что информация о пользователе запросилась
//        System.out.println(isSuccessFieldFalse);
//        assertFalse("Success field must have false value", isSuccessFieldFalse);
//        // Проверка тела сообщения
//        System.out.println(actualErrorMessage);
//        assertEquals("Expected error message is " + expectedErrorMessage + ". But actual is " + actualErrorMessage,
//                expectedErrorMessage, actualErrorMessage);
//
//    }

    // РАБОТАЕТ!!!
//    @Test
//    @DisplayName("Меняет сразу все данные пользователя: name, email, password")
//    public void editingAllUserData() {
//        // Создание пользователя, авторизуемся и получаем его токен
//        userClient.create(user);
//        ValidatableResponse login = userClient.login(UserCredentials.from(user));
//        bearerToken = login.extract().path("accessToken");
//
//        // меняем password, email, and name
//        ValidatableResponse editedUser = userClient.editInfo(UserCredentials.getUserWithPasswordEmailAndName(user),
//                bearerToken);
//
//        // достаем статус код и значение поля success
//        int actualCode = editedUser.extract().statusCode();
//        boolean isSuccessTrue = editedUser.extract().path("success");
//
//        assertEquals("Ожидаемый статус код " + success200StatusCode + ". Фактический " + actualCode,
//                success200StatusCode, actualCode);
//        assertTrue("Должно вернуться true, но возвращается false", isSuccessTrue);
//    }
//
//    // РАБОТАЕТ!!!
//    @Test
//    @Description("Проверяет, что неавторизованный пользователь не может менять информацию о себе ")
//    public void userInfoCanNotBeChangedWithoutAuthorizationNegativeTest() {
//        // bearerToken = "";
//        String expectedErrorMessage = "You should be authorised";
//        int expectedStatusCode = 401;
//
//        userClient.create(user);
//        // Получение информации о пользователе
//       // ValidatableResponse info = userClient.userInfoChange(bearerToken, User.getRandom());
//        ValidatableResponse info = userClient.editInfoWithoutToken(user);
//
//        // Получение статус код, значение поля success и сообщение об ошибке
//        int actualStatusCode = info.extract().statusCode();
//        boolean getUserInfo = info.extract().path("success");
//        String actualErrorMessage = info.extract().path("message");
//
//        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
//                expectedStatusCode, actualStatusCode);
//        assertFalse("Ожидаемый ответ false, по факту true", getUserInfo);
//        assertEquals("Ожидаемое сообщение об ошибке " + expectedErrorMessage + ". Фактическое " + actualErrorMessage,
//                expectedErrorMessage, actualErrorMessage);
//    }

    // НЕ РАБОТАЕТ
//    @Test
//    @DisplayName("Check not successful editing all user data with email already exist")
//    public void editingSpaceUserInfoWithAlreadyExistEmail() {
//        String message = "User with such email already exists";
//        //Arrange
//        // User user = User.getRandom();
//        User anotherOneUser = User.getRandom();
//        ValidatableResponse createdUser2 = userClient.create(anotherOneUser); // userClient.create(user);
//        //Act
//        expectedData = new User(anotherOneUser.getEmail(), user.getPassword(), user.getName());
//        ValidatableResponse editedData = userClient.editInfo(expectedData, accessUserToken);
//        //Assert
//        assertEquals("Статус-код не == 403!",403, editedData.extract().statusCode());
//        assertFalse("Параметр success != false", editedData.extract().path("success"));
//        assertEquals("Сообщение не соответствует требованиям!", message, editedData.extract().path("message"));
//        //Act
//        ValidatableResponse editedUserLoggedIn = userClient.login(new UserCredentials(expectedData.getEmail(), user.getPassword()));
//        //Assert
//        assertEquals("Юзер залогинился с несуществующими данными!",401, editedUserLoggedIn.extract().statusCode());
//    }

}

