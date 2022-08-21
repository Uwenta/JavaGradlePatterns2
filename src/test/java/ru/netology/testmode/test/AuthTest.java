package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {

        open("http://localhost:9999");
        //Configuration.holdBrowserOpen = true;
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("input[name='login']").setValue(registeredUser.getLogin());
        $("input[name='password']").setValue(registeredUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("h2.heading").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("input[name='login']").setValue(notRegisteredUser.getLogin());
        $("input[name='password']").setValue(notRegisteredUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("div[data-test-id='error-notification']").shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("input[name='login']").setValue(blockedUser.getLogin());
        $("input[name='password']").setValue(blockedUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("div[data-test-id='error-notification']").shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.text("Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("input[name='login']").setValue(wrongLogin);
        $("input[name='password']").setValue(registeredUser.getPassword());
        $("button[data-test-id='action-login']").click();
        $("div[data-test-id='error-notification']").shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("input[name='login']").setValue(registeredUser.getLogin());
        $("input[name='password']").setValue(wrongPassword);
        $("button[data-test-id='action-login']").click();
        $("div[data-test-id='error-notification']").shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.text("Неверно указан логин или пароль"));
    }
}
