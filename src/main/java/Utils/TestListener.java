package Utils;

import io.qameta.allure.Attachment;
import org.testng.ITestListener;

public class TestListener implements ITestListener {

    @Attachment
    public String messageText (String reportValue) {
        return reportValue;
    }
}
