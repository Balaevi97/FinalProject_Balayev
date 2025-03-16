package Steps.FrontSteps;

import Elements.MoveToCard;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.time.Duration;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;

public class MoveToCardSteps extends MoveToCard {
    @Step
    public MoveToCardSteps clickProducts () {
        products.click();
        return this;
    }
    @Step
    public MoveToCardSteps openProdList () {
        for (SelenideElement product : loadPage) {
            product.shouldBe(visible, Duration.ofSeconds(10));
            product.shouldBe(clickable, Duration.ofSeconds(10));
        }
        checkProdList.click();
        return this;
    }
    @Step
    public MoveToCardSteps moveToProduct() {
        moveToProduct.first().click();
        return this;
    }

    @Step
    public boolean assertPage () {
        Assert.assertTrue(assertPage.shouldBe(visible, Duration.ofSeconds(5)).getText().contains("ანგარიშის დეტალები"));
        return true;
    }
    public void checkAssert () {
        if (assertPage()) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed");
        }
        System.out.println();
    }


}
