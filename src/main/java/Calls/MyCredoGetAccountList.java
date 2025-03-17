package Calls;

import io.restassured.response.Response;;

import static Steps.FrontSteps.LogInSteps.token;
import static Utils.APIRequestSpec.getRequestSpec;
import static Utils.APIRequestSpec.getResponseSpec;
import static Utils.URL.MyCredoAccountList;
import static io.restassured.RestAssured.given;

public class MyCredoGetAccountList {


    public static Response  GetAccountListWithPersonID () {
        String URL;

        Response response = given()
                .spec(getRequestSpec(MyCredoAccountList, token))
                .when()
                .get("/graphql");

        response.then().log().all().spec(getResponseSpec());
        return response;
    }

    public static Response  PostAccountListWithPersonID (String Query) {
        Response response = given()
                .spec(getRequestSpec(MyCredoAccountList, token))
                .body(Query)
                .when()
                .post("/graphql");

        response.then().spec(getResponseSpec());
        return response;
    }
}
