package selenium.pages;

import static java.lang.Thread.sleep;

public class BasePage {

    public void doSleep() {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
