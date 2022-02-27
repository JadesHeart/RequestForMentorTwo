package apiMethods;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;


public class getEmail {
    /**
     * Метод для получения мэйла с АПИшки
     * 1) получаю респонс страницы
     * 2) в переменную stringResponse засовываю почту юзера
     *
     * @param fistName имя
     * @param lastName фамилия
     * @return возвращает найденную почту
     */
    public static String getEmailFromBody(String fistName, String lastName) throws IOException {
        Response pageResponse = getAllUsers();
        String stringResponse =
                pageResponse.jsonPath()
                        .getString("data.find{(it.first_name=='" + fistName + "')&&" +
                                "(it.last_name =='" + lastName + "')}.email");
        return stringResponse;
    }

    /**
     * метод с получением почты со всем страниц
     * 1) Получаю количество страниц
     * 2)  Циклом перебераю каждую страницу: начиная с первой и на каждой ищу нужного юзера
     * 3) если нужная почта не найдена на первой странице, то переход на вторую итд
     * 4) если почты вообще нет ни на одной странице, то вернёт "Почты нет на всех страницах."
     *
     * @param fistName имя юзера
     * @param lastName фамилия юзера
     * @return почту полученную по имени и фамилии
     */
    public static String getEmailWithPagination(String fistName, String lastName) throws IOException {
        int total_page =
                getAllUsers()
                        .jsonPath().getInt("total_pages"); // Общее количество страниц
        for (int i = 1; total_page > i - 1; ++i) {
            Response pageResponse = getUsersByPage(i);//запрос к АПИшке
            String stringResponse = pageResponse.jsonPath()
                    .getString("data.find{(it.first_name=='" + fistName + "')&&" +
                            "(it.last_name =='" + lastName + "')}.email");//получаю почту
            if (stringResponse != null) {
                return stringResponse;//если почта нашлась на текущей странице то возвращает её, если нет то продолжает цикл
            }
        }
        return "Почты нет на всех страницах.";//мессендж если ничего не нашлось
    }

    /**
     * Функция респонса с сайте с параметром страницы
     *
     * @return возвращает респонс страницы
     */
    public static Response getUsersByPage(int numberPage) {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://reqres.in/api");

        Response response = given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .when().get("/users?page=" + numberPage);
        return response;
    }

    /**
     * Функция респонса с сайте с параметром всех юзеров
     *
     * @return возвращает респонс страницы
     */
    public static Response getAllUsers() {
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.baseUri("https://reqres.in/api");
        requestSpecification.basePath("/users?per_page=12");

        Response response = given()
                .spec(requestSpecification)
                .contentType(ContentType.JSON)
                .when().get();
        return response;
    }
}
