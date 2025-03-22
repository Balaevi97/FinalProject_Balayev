package Utils;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.given;

public class APIRequestSpec {

    public static RequestSpecification getRequestSpec(String BASE_URL, String accessToken) {
        return   given()
                .baseUri(BASE_URL)
                .contentType("application/graphql-response+json; charset=utf-8")
                .accept("application/graphql-response+json")
                .header("Content-type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "*/*")
                .header("Accept-Encoding", "gzip, deflate, br");
    }

    public static ResponseSpecification getResponseSpec() {
        return new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/graphql-response+json; charset=utf-8")
                .build();
    }



}
