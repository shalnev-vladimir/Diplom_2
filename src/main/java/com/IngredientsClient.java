package com;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientsClient extends RestAssuredClient {

    private static final String INGREDIENTS_PATH = "ingredients";

//    @Step
//    public Response getIngredients() {
//        Response response = given()
//                .spec(getBaseSpec())
//                .get(INGREDIENTS_PATH);
//        return response;
//    }

    private static final String USER_PATH = "/api/";

    @Step
    public ValidatableResponse getIngredients() {

        return
                given()
                        .spec(getBaseSpec())
                        .when()
                        .get(INGREDIENTS_PATH)
                        .then();
    }

}