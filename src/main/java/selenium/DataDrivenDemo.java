package selenium;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import selenium.pages.TranslatePage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class DataDrivenDemo {
    ExcelReader reader = null;
    ExcelWriter writer = null;
    String user_dir = System.getProperty("user.dir");
    String excelFilePath = user_dir + "/src/main/resources/副本翻译用例数据驱动.xls";
    List<List<String>> rows = new ArrayList<>();
    WebDriver driver = null;

    @BeforeClass
    public void before() {
        reader = ExcelUtil.getReader(FileUtil.file(excelFilePath), "sheet1");

        String user_dir = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", user_dir + "/src/main/resources/v92/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterClass
    public void after() {
        driver.quit();
        writer.write(rows);
        writer.close();
    }

    @DataProvider(name = "data")
    public Object[][] dataProvider() {
        List<List<Object>> read = reader.read();
        Object[][] data = new Object[read.size() - 1][4];
        //第一行是标题,跳过
        for (int i = 1; i < read.size(); i++) {
            List<Object> line = read.get(i);
            //读取前4列
            for (int j = 0; j < 4; j++) {
                if (line.get(j) == null || "".equals(line.get(j))) {
                    break;
                }
                data[i - 1][j] = line.get(j);
            }
        }
        reader.close();
        writer = ExcelUtil.getWriter(FileUtil.file(excelFilePath), "sheet1");
        writer.passCurrentRow();//跳过第一行标题
        return data;
    }

    @Test(dataProvider = "data")
    public void test(String originLanguage, String targetLanguage, String originText, String transText) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        TranslatePage translatePage = new TranslatePage(driver);
        String resultText = translatePage.translate(originLanguage, targetLanguage, originText);
        List<String> row = null;
        if (transText.equals(resultText)) {
            row = CollUtil.newArrayList(originLanguage, targetLanguage, originText, transText, resultText, "正确");
        } else {
            row = CollUtil.newArrayList(originLanguage, targetLanguage, originText, transText, resultText, "错误");
        }
        //记录用例执行结果
        rows.add(row);

    }


}
