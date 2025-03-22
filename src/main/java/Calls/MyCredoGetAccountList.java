package Calls;

import io.restassured.response.Response;

import static Utils.APIRequestSpec.*;
import static Utils.StringValues.token;
import static Utils.URL.myCredo_API;
import static io.restassured.RestAssured.given;

public class MyCredoGetAccountList {


    public static Response  GetAccountListWithPersonID () {

        Response response = given()
                .spec(getRequestSpec(myCredo_API, token))
                .when()
                .get("/graphql");

        response.then().log().all().spec(getResponseSpec());
        return response;
    }

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
