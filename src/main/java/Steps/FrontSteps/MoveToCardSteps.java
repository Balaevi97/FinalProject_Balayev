package Steps.FrontSteps;

import Elements.MoveToCard;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.testng.Assert;

import java.time.Duration;

import static com.codeborne.selenide.Condition.clickable;


public class MoveToCardSteps extends MoveToCard {
    @Step
    public MoveToCardSteps clickProducts () {
        products.click();
        return this;
    }
    @Step
    public MoveToCardSteps openProdList () {

        for (SelenideElement product : loadPage) {
            if (product.shouldBe(clickable, Duration.ofSeconds(10)).isDisplayed()) {
                checkProdList.click();
                break;
            }
        }

        return this;
    }
    @Step
    public MoveToCardSteps moveToProduct() {

        if (moveToProduct.filter(Condition.clickable).isEmpty() &&
                moveToProduct.filter(Condition.clickable).isEmpty()) {
            openProdList ();
        }
        moveToProduct.first().click();
        return this;
    }

    @Step
    public boolean assertPage () {
        Assert.assertTrue(assertPage.shouldBe(Condition.visible, Duration.ofSeconds(5)).getText().contains("ანგარიშის დეტალები"));
        return true;
    }


}
