package selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.lang.Thread.sleep;

public class TranslatePage extends BasePage{
    WebDriver driver = null;

    public TranslatePage(WebDriver driver) {
        this.driver = driver;
        this.driver.get("https://ai.sogou.com/product/text_translate/");  //替换为登录地址
    }

    public String translate(String orginLang, String targetLang, String originText){


        WebElement input = driver.findElement(By.xpath("//*[@id=\"content\"]"));
        WebElement btn = driver.findElement(By.xpath("//*[@id=\"gatsby-focus-wrapper\"]/div/main/div/div[3]/div/span/section/div/div[1]/button"));
        WebElement result = driver.findElement(By.xpath("//*[@id=\"gatsby-focus-wrapper\"]/div/main/div/div[3]/div/span/section/div/div[2]/div/div"));
        input.sendKeys("");

        selectOriSelect( orginLang);
        selectTargetSelect( targetLang);

        input.sendKeys(originText);
        doSleep();
        btn.click();
        doSleep();
        String resultText = result.getText();
        return resultText;
    }

    public  void selectTargetSelect(String lang){
        driver.findElement(By.id("targetLanguageCode")).click();
        switch (lang){
            case "中文":
                driver.findElement(By.xpath("//div[3]/div/div/div/ul/li[1]")).click();
                break;
            case "英语":
                driver.findElement(By.xpath("//div[3]/div/div/div/ul/li[2]")).click();
                break;
            case "日语":
                driver.findElement(By.xpath("//div[3]/div/div/div/ul/li[3]")).click();
                break;
        }

    }

    public  void selectOriSelect(String lang){
        driver.findElement(By.id("sourceLanguageCode")).click();
        switch (lang){
            case "自动检测语种":
                driver.findElement(By.xpath("//li[1]")).click();

                break;
            case "中文":
                driver.findElement(By.xpath("//li[2]")).click();

                break;
            case "英语":
                driver.findElement(By.xpath("//li[3]")).click();
                break;
            case "日语":
                driver.findElement(By.xpath("//li[4]")).click();
                break;
        }

    }


}
