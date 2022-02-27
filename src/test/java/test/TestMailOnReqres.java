package test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

import static apiMethods.getEmail.*;

/**
 * Класс с тест-кейсами
 */
public class TestMailOnReqres {
    /**
     * Первый тест-кейс
     * В eMail записан полученная почта
     * Проверяю на соответствие ожидаемую почту и полученую
     */
    @Test
    public void testMailComplianceFromBody() throws IOException {
        String eMail = getEmailFromBody("George", "Bluth");
        Assert.assertEquals(eMail, "george.bluth@reqres.in", "Почта не совпадает с ожидаемой");
    }

    /**
     * Второй тест-кейс
     * В eMail записан полученная почта
     * Проверяю на соответствие ожидаемую почту и полученую(со второй страницы)
     */
    @Test
    public void testMailComplianceFromURL() throws IOException {
        String eMail = getEmailWithPagination("Michael", "Lawson");
        Assert.assertEquals(eMail, "michael.lawson@reqres", "Почта не совпадает с ожидаемой");
    }
}