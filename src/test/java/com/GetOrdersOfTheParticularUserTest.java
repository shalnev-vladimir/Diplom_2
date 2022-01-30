package com;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class GetOrdersOfTheParticularUserTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    String accessToken;

    //Создаем нового рандомного пользователя
    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = User.getRandom();
    }

//    @After
//    public void tearDown(){
//        userClient.delete(accessToken.substring(7));
//    }

//    @Test
//    @Description("Проверка что существующий пользователь может авторизоваться")
//    public void userCanLogInUsingValidData() {
//
//        int expectedStatusCode = 200;
//
//        // Создание пользователя
//        userClient.create(user);
//        // Авторизация созданного пользователя
//        ValidatableResponse login = userClient.login(UserCredentials.from(user));
//        String token = login.extract().path("accessToken");
//        // Получаем список заказов пользователя
//        orderClient.getOrdersOfTheUser(user);
//        ValidatableResponse ordersList = orderClient.getOrdersOfTheUser(user);
//        // Получение статус кода ответа статуса заказов
//        int actualStatusCode = ordersList.extract().statusCode();
//        // Получение true (or false) из тела ответа при получении заказов конкретного пользователя
//        boolean checkIfWeGotOrderListOfUser = ordersList.extract().path("success");
//        // Получение токена авторизированого пользователя
//        // String token = login.extract().path("accessToken");
//
//        System.out.println(token);
//        // Проверка что пользователь авторизовался
//        assertTrue ("Expected true, but returns false", checkIfWeGotOrderListOfUser);
//        // Проверка что статус код соответсвует ожиданиям
//        // assertThat ("Status code is incorrect", actualStatusCode, equalTo(200));
//        assertEquals("Expected status code is 200. But actual is " + actualStatusCode,
//                expectedStatusCode, actualStatusCode);
//        // Проверка что токен пользователя не пустой
//      //  assertThat("User access token is null", token, notNullValue());
//    }

    @Test
    @Description("Получение заказов пользователя с авторизацией")
    public void getAuthorizationUserOrdersTest() {
        // Создание, авторизация и получение токена пользователя
        userClient.create(user);
        ValidatableResponse login = userClient.login(UserCredentials.from(user));
        String token = login.extract().path("accessToken");

        // Получаем заказы пользователя
        ValidatableResponse response = orderClient.getAuthorizedUserOrders(token);

        int actualStatusCode = response.extract().statusCode();
        boolean isUserReceivedOrderList = response.extract().path("success");

        assertThat("Ожидаемый статус код 200. Фактический " + actualStatusCode, actualStatusCode, equalTo(200));
        assertTrue("Должно вернуться true, по факту возвращается false. " +
                "Автоизованный пользователь не может получить список заказов", isUserReceivedOrderList);
    }

    @Test
    @Description("Получение заказов пользователя без авторизации")
    public void getNotAuthorizationUserOrdersTest() {
        String expectedErrorMessage = "You should be authorised";

        ValidatableResponse response = orderClient.getUnauthorizedUserOrders();

        int actualStatusCode = response.extract().statusCode();
        boolean isSuccessFalse = response.extract().path("success");
        String actualErrorMessage = response.extract().path("message");

        assertThat(actualStatusCode, equalTo(401));
        assertFalse("Ожидается false, по факту true", isSuccessFalse);
        assertEquals("Ожидаемое сообщение об ошибке " + expectedErrorMessage + ". Фактическое " + actualErrorMessage,
                expectedErrorMessage, actualErrorMessage);
    }

//    @Test
//    public void successGetOrder() {
//        User user = User.getRandom();
//        userClient.create(user);
//        ValidatableResponse responseLogin = userClient.login(new UserCredentials(user.email, user.password));
//        accessToken = responseLogin.path("accessToken");
//        Boolean success = userClient.getOrdersOfTheUser(accessToken);
//        assertEquals(true, success);
//    }
//
//    @Test
//    public void getOrderWithoutToken() {
//        User user = User.getRandom();
//        userClient.create(user);
//        Response responseLogin = userClient.login(new UserCredentials(user.email, user.password));
//        accessToken = responseLogin.path("accessToken");
//        String message = userClient.getOrdersWithoutToken();
//        Assert.assertEquals("You should be authorised", message);
//    }

}
