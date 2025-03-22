package Calls;

import io.restassured.response.Response;

import static Utils.APIRequestSpec.getRequestSpec;
import static Utils.APIRequestSpec.getResponseSpec;
import static Utils.StringValues.token;
import static Utils.URL.myCredoAccountList;
import static io.restassured.RestAssured.given;

public class MyCredoGetAccountList {


    public static Response  GetAccountListWithPersonID () {

        Response response = given()
                .spec(getRequestSpec(myCredoAccountList, token))
                .when()
                .get("/graphql");

        response.then().log().all().spec(getResponseSpec());
        return response;
    }

    public static Response  PostAccountListWithPersonID (String Query) {
        Response response = given()
                .spec(getRequestSpec(myCredoAccountList, token))
                .body(Query)
                .when()
                .post("/graphql");

        response.then().spec(getResponseSpec());
        return response;
    }
}
