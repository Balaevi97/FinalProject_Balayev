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
                break;
            }
        }

        return this;
    }
    @Step
    public MoveToCardSteps moveToProduct() {

        try {
            if (moveToProduct.first().is(clickable, Duration.ofSeconds(3))) {
                moveToProduct.first().click();
            } else {
                openProdList ();
                moveToProduct.first().click();
            }
        } catch (Exception e) {
           System.out.println(e.getMessage());
      }

        return this;
    }


    @Step
    public boolean assertIsOnRightPage () {
        if (loadPage.first().is(exist, Duration.ofSeconds(3))) {
            loadPage.first().click();
            moveToProduct.first().click();
        } else {
            Assert.assertTrue(assertPage.shouldBe(Condition.visible, Duration.ofSeconds(5)).getText().contains("ანგარიშის დეტალები"));
        }
        return true;
    }


}
