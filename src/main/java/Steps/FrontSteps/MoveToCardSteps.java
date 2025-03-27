package Steps.FrontSteps;

import Elements.MoveToCard;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.time.Duration;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.exist;


public class MoveToCardSteps extends MoveToCard {

    @Step
    public MoveToCardSteps clickProducts () {
        products.click();
        return this;
    }

    @Step
    public MoveToCardSteps openProdList () {

        for (SelenideElement product : loadPage) {
            if (product.shouldBe(Condition.clickable, clickable, Condition.interactable).isDisplayed()) {
                loadPage.first().click();
            }
        }
        return this;
    }

    @Step
    public MoveToCardSteps moveToProduct() {
        if (moveToProduct.first().is(clickable, Duration.ofSeconds(2))) {
            moveToProduct.first().click();
        }

            while (true) {
                if (loadPage.first().is(exist, Duration.ofSeconds(3))) {
                    loadPage.first().click();

                    moveToProduct.first()
                            .shouldBe(Condition.clickable, Duration.ofSeconds(5))
                            .click();
                }
                if (!loadPage.first().is(exist, Duration.ofSeconds(2))) {
                    break;
                }
                System.out.println("Page reloaded back to loadPage, retrying...");
            }

        return this;
    }

    @Step
    public boolean assertIsOnRightPage() {
        Assert.assertTrue(assertPage.getText().contains("ანგარიშის დეტალები"));
        return true;
    }

}
