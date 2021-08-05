package crawler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

import static java.lang.Thread.sleep;

public class DataDrivenDemo {
    ExcelReader reader = null;

    @BeforeClass
    public void before(){
        String user_dir = System.getProperty("user.dir");
        String excelFilePath = user_dir +"/src/main/resources/副本翻译用例数据驱动.xls";
        reader =ExcelUtil.getReader(FileUtil.file(excelFilePath),"sheet1");
    }

    @DataProvider(name = "data")
    public Object[][] dataProvider(){
        List<List<Object>> read = reader.read();
        Object[][] data = new Object[read.size()][4];
        //第一行是标题
        for (int i = 1; i < read.size(); i++) {
            List<Object> line = read.get(i);
            //读取前4列
            for (int j = 0; j < 4; j++) {
                if(line.get(j) == null){
                    break;
                }
                data[i-1][j] = line.get(j);
            }
        }
        return data;
    }

    @Test(dataProvider = "data")
    public  void test(String originLanguage,String targetLanguage,String originText,String transText) {

        System.out.println(originLanguage+"-"+targetLanguage+"-"+originText+"-"+transText);
        //String user_dir = System.getProperty("user.dir");
        //System.setProperty("webdriver.chrome.driver", user_dir + "/src/main/resources/v92/chromedriver");
        //WebDriver driver = new ChromeDriver();
        //
        ////登录
        //driver.get("https://ai.sogou.com/product/text_translate/");  //替换为登录地址
        //
        //WebElement language1 = driver.findElement(By.xpath("//*[@id=\"sourceLanguageCode\"]/div/div"));
        //WebElement language2 = driver.findElement(By.xpath("//*[@id=\"targetLanguageCode\"]/div/div"));
        //WebElement input = driver.findElement(By.xpath("//*[@id=\"content\"]"));
        //WebElement result = driver.findElement(By.xpath("//*[@id=\"gatsby-focus-wrapper\"]/div/main/div/div[3]/div/span/section/div/div[2]/div/div"));
        //
        //input.click();
        //language1.click();
        ////language1.sendKeys("英语");
        //input.sendKeys("英语");
        //
        //doSleep();
        //driver.quit();
    }



    public static void doSleep() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
