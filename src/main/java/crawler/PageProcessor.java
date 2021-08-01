package crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class PageProcessor {

    public static void main(String[] args) {
        String user_dir = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", user_dir + "/src/main/resources/v92/chromedriver");

        ChromeOptions options = new ChromeOptions();
        Map<String, String> profile = new HashMap<>();
        profile.put("savefile.default_directory", user_dir + "/pdf");
        profile.put("printing.print_preview_sticky_settings.appState", "{\n" +
                "    \"recentDestinations\": [\n" +
                "        {\n" +
                "            \"id\": \"Save as PDF\",\n" +
                "            \"origin\": \"local\",\n" +
                "            \"account\": \"\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"selectedDestinationId\": \"Save as PDF\",\n" +
                "    \"version\": 2\n" +
                "}");
        options.setExperimentalOption("prefs", profile);
//        options.addArguments("--headless");
        options.addArguments("--kiosk");
        options.addArguments("--kiosk-printing");

        WebDriver driver = new ChromeDriver(options);

        //登录
        driver.get("https://www.baidu.com/");  //替换为登录地址

        //文本输入的例如
        WebElement element_text = driver.findElement(By.xpath("//*[@id=\"kw\"]"));
        element_text.sendKeys("苍井空");

        //按钮点击的例子
        WebElement element_btn = driver.findElement(By.xpath("//*[@id=\"su\"]"));
        element_btn.click();

        doSleep();

        String s = "http://tensent.com/AppStore/Wiki/Pages/WikiArticles.html?id=";

        //开始存储页面
        for (int i = 1; i <= 36000; i++) {
            String realURL = s + i +"";
            print(driver, realURL);
        }

        driver.quit();
    }

    public static void print(WebDriver driver, String url) {
        driver.get(url);
        //加逻辑判断是否为无效页面
        WebElement element_title = driver.findElement(By.id("article-title"));
        if (element_title.getText().contains("文章不存在")) {
            return;
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.print();");

        doSleep();
    }

    public static void doSleep() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
