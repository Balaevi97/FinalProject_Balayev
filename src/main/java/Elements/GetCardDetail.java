package Elements;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class GetCardDetail {

    public SelenideElement pages = $(byXpath("//div[@class='navigation']//p"));
    public SelenideElement nextProduct = $(byId("selectNextProduct"));

    public ElementsCollection getAllElement = $$(byXpath("//div[@class='brief-info']//p"));
    public SelenideElement accountNumber = $(byXpath("//div[@class='wrapper']//p[@class='block-header-caps-16' and contains(text(),'GE')]"));


}
