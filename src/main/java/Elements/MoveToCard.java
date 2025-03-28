package Elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MoveToCard {

    public SelenideElement products = $(byXpath("//p [text()='პროდუქტები']"));
    public ElementsCollection loadPage = $$(byXpath("//div[@class='header trigger']/p[contains(@class,'block-header-caps-20')]"));
    public ElementsCollection moveToProduct = $$(byId("navToAccDetails"));
    public SelenideElement assertPage = $(byXpath("//p[@class='header block-header-caps-20 trigger']"));

}
