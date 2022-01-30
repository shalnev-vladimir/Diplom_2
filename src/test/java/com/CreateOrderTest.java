package com;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CreateOrderTest {

    private UserClient userClient;
    private User user;
    private OrderClient orderClient;
    private String token;
    IngredientsClient ingredientsClient;
   // private final HashMap<String, List> orderHash = new HashMap<>();

    List<String> ingredients = new ArrayList<>();
    private int orderNumber;


    //Создаем нового рандомного курьера
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandom();
    }


    @Test
    @DisplayName("Checking user can create order with ingredients after authorization")
    public void checkAuthUserCanMakeAnOrderTest() {
        int expectedStatusCode = 200;

        String token = userClient.create(user).extract().path("accessToken");
        ingredients = new IngredientsClient().getIngredients().extract().path("data._id");
        IngredientsHashes orderIngredients = new IngredientsHashes(ingredients.get(0));
        ValidatableResponse response = new OrderClient().createOrderWithToken1(orderIngredients, token);

        int actualStatusCode = response.extract().statusCode();
        boolean isOrderSuccessfullyCreated = response.extract().path("success");
        orderNumber = response.extract().path("order.number");

        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
        assertTrue("Заказ не создан", isOrderSuccessfullyCreated);
        assertThat("Номер заказа пустой", orderNumber, notNullValue());
    }

    @Test
    @DisplayName("This test verifies that it is allowed to create order with ingredients without authorization")
    public void checkUserCanMakeAnOrderWithoutAuthorizationTest() {
        int expectedStatusCode = 200;

        ingredients = new IngredientsClient().getIngredients().extract().path("data._id");
        IngredientsHashes orderIngredients = new IngredientsHashes(ingredients.get(0));
        ValidatableResponse response = new OrderClient().createOrderWithToken1(orderIngredients, "");
        int actualStatusCode = response.extract().statusCode();
        boolean isOrderSuccessfullyCreated = response.extract().path("success");
        int orderNumber = response.extract().path("order.number");

        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
        assertTrue("Заказ не создаля. Должно вернуться true, возвращается false", isOrderSuccessfullyCreated);
        assertThat("Нет номера заказа", orderNumber, notNullValue());
    }

    @Test
    @DisplayName("This test verifies that it is not allowed to create an order with no ingredients")
    public void checkUserCanNotCreateAnOrderWithNoIngredientsTest() {
        int expectedStatusCode = 400;

        String token = userClient.create(user).extract().path("accessToken");
        IngredientsHashes ingredients = new IngredientsHashes(null);
        ValidatableResponse response = new OrderClient().createOrderWithToken1(ingredients, token);
        int actualStatusCode = response.extract().statusCode();
        boolean isOrderNotCreated = response.extract().path("message").equals("Ingredient ids must be provided");

        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
        assertTrue("Заказ создался с пустым списком ингредиентов. Заказ не должен быть создан", isOrderNotCreated);
    }

    @Test
    @DisplayName("CThis test verifies that it is not allowed to create an order with invalid id of ingredients")
    public void checkUserCanNotCreateAnOrderWithInvalidIngredientsIdTest() {
        int expectedStatusCode = 500;

        String token = userClient.create(user).extract().path("accessToken");
        IngredientsHashes ingredients = new IngredientsHashes("what is those?");
        ValidatableResponse response = new OrderClient().createOrderWithToken1(ingredients, token);

        int actualStatusCode = response.extract().statusCode();

        assertEquals("Ожидаемый статус код " + expectedStatusCode + ". Фактический " + actualStatusCode,
                expectedStatusCode, actualStatusCode);
    }


    // Не работает УДАЛИТЬ
    @Test
    @Description("Создаем заказ. Пользователь авторизован.")
    public void createOrderPositiveTest() {

//        // Создание пользователя
//        userClient.create(user);
//        // Авторизация созданного пользователя
//        userClient.login(UserCredentials.from(user));
//        // Создание заказа
//        OrderClient orderClient = new OrderClient();
//        ValidatableResponse response = orderClient.makeOrder(user);
//
//        int statusCode = response.extract().statusCode();
//        boolean isOrderCreated = response.extract().path("success");
//
//        System.out.println(statusCode);
//        System.out.println(isOrderCreated);
//      Создание пользователя
       // userClient.create(user);
        // Авторизация созданного пользователя
//        ValidatableResponse login = userClient.login(UserCredentials.from(user));
//        String token = login.extract().path("accessToken");
//        System.out.println(token);
//        ValidatableResponse responseIngredients = ingredientsClient.getIngredients();
       // List<String> ingredients = responseIngredients.path("data._id");
       // ValidatableResponse response = orderClient.createOrderWithToken(ingredients, "");
        // Создание заказа
        // OrderClient orderClient = new OrderClient();
//        ValidatableResponse responseOrderWithToken = orderClient.createOrderWithToken(token, orderHash);
//        ArrayList<String> actualIngredientsList = responseOrderWithToken.extract().path("order.ingredients._id");

//        assertThat(responseOrderWithToken.extract().statusCode(), equalTo(200));
//        assertTrue(responseOrderWithToken.extract().path("success"));
//        assertNotNull(responseOrderWithToken.extract().path("order.number"));
//        assertEquals("Кол-во ингредиентов разное", numberOfIngredients, actualIngredientsList.size());

    }

}
