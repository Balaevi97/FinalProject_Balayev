package Setup;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PageResolution extends SeleniumSetUp {
    public static void changeResolution (int percent) throws AWTException {
        driver.manage().window().maximize();
        Robot robot = new Robot();
        int count = Math.abs(percent / 10);

        if (percent > 50) {
            for (int i = 0; i < count; i++) {

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_ADD);
                robot.keyRelease(KeyEvent.VK_ADD);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
        } else {
            for (int i = 0; i < count; i++) {

                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_SUBTRACT);
                robot.keyRelease(KeyEvent.VK_SUBTRACT);
                robot.keyRelease(KeyEvent.VK_CONTROL);
            }
        }
    }
}
