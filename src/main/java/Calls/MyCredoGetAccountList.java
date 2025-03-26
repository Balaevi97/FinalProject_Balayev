package Calls;

import io.restassured.response.Response;

import static Steps.FrontSteps.LogInSteps.token;
import static Utils.APIRequestSpec.*;
import static Utils.URL.myCredo_API;
import static io.restassured.RestAssured.given;

public class MyCredoGetAccountList {

    public static Response  PostAccountListWithPersonID (String Query) {
        Response response = given()
                .spec(getRequestSpec(myCredo_API, token))
                .body(Query)
                .when()
                .post("/graphql");

        response.then().spec(getResponseSpec());
        return response;
    }
}
