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
        token = """
                eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg2YTA0OGZmNWJiNTYzNmZkMGRhMWY3ODczMGRlZDZjIiwidHlwIjoiSldUIn0.eyJuYmYiOjE3NDIyODU4MDYsImV4cCI6MTc0NDg3NzgwNiwiaXNzIjoiaHR0cDovL3Rlc3RpZGVudGl0eXNzby5teWNyZWRvLmdlOjQwNDQxIiwiYXVkIjpbImh0dHA6Ly90ZXN0aWRlbnRpdHlzc28ubXljcmVkby5nZTo0MDQ0MS9yZXNvdXJjZXMiLCJjcmVkb19hcGkiLCJrZWVwel9hcGkiLCJyZXRhaWxfbG9hbnNfYXBpIiwicmV0YWlsX29mZmVyc19hcGkiLCJyZXRhaWxfdHJhbnNmZXJzX2FwaSJdLCJjbGllbnRfaWQiOiJteWNyZWRvX3dlYl9jbGllbnQiLCJzdWIiOiI3MjQ5MiIsImF1dGhfdGltZSI6MTc0MjI4NTgwNiwiaWRwIjoibG9jYWwiLCJVc2VyTmFtZSI6ImJiYWxhZXZpOTciLCJBY2Nlc3NUeXBlIjoiZnVsbCIsIkN1c3RvbWVySWQiOiIyMTU3MTIxIiwiUGVyc29uYWxOdW1iZXIiOiIwMTg3MDAwMDIyMyIsIlBob25lIjoiNTU1MjI1NjgzIiwiSGFzVGVtcFBhc3N3b3JkIjoiRmFsc2UiLCJDaGFubmVsIjoiV0VCIiwiVHJhY2VJZCI6IjkyOEYxaFJMUERIdjhsWDNIYnBnMXh1a0lGalJGeWFJS29NS1hhNm1KSEE9Iiwic2NvcGUiOlsiY3JlZG9fYXBpIiwia2VlcHpfYXBpIiwicmV0YWlsX2xvYW5zX2FwaSIsInJldGFpbF9vZmZlcnNfYXBpIiwicmV0YWlsX3RyYW5zZmVyc19hcGkiLCJvZmZsaW5lX2FjY2VzcyJdLCJhbXIiOlsicHdkIl19.fiEcOuRkKxrdGnlq2MDfpWH1i0QjgrL68Fo2PApmLq_oxBM4MYXK0tBRGbfa7PBiczGTEORQZ3axMUUxFuRygJcou1wjXLeZ0FfgnYgBwYrHaG4zB_uk3FqPPPhxdPH7jMjF3VpYIOoZ49QR5krvpCDgmYhVQOlisMCGh8MZSz0tmKVsz7vgIG9VgCa_jSYPEI2kVLQV-Ks3bFcQvWP3QJFQbnKotbmBuTRvCxa9CSbXuchFmwJOfJyjyGfG3aWlmX-tDfZE1am0af96q7Sg6KLq5yy3hoyJeoGSqmsqiD-OiPQJILZjZqyuS904wb2S8O8RPGJXeiJH5BUQJQffmQ
                """;
        Response response = given()
                .spec(getRequestSpec(MyCredoAccountList, token))
                .body(Query)
                .when()
                .post("/graphql");

        response.then().spec(getResponseSpec());
        return response;
    }
}
