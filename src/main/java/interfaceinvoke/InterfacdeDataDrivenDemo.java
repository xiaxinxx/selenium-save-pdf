package interfaceinvoke;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import selenium.pages.TranslatePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InterfacdeDataDrivenDemo {
    ExcelReader reader = null;
    ExcelWriter writer = null;
    String user_dir = System.getProperty("user.dir");
    String excelFilePath = user_dir + "/src/main/resources/副本翻译用例数据驱动.xls";
    List<List<String>> rows = new ArrayList<>();

    @BeforeClass
    public void before() {
        reader = ExcelUtil.getReader(FileUtil.file(excelFilePath), "sheet1");
    }

    @AfterClass
    public void after() {
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
        reader.close(); //读取结束

        //初始化excel写操作
        writer = ExcelUtil.getWriter(FileUtil.file(excelFilePath), "sheet1");
        writer.passCurrentRow();//跳过第一行标题
//        writer.passRows(3); //如果前几行是标题或说明，可以跳过多行才开始写
        return data;
    }

    @Test(dataProvider = "data")
    public void test(String originLanguage, String targetLanguage, String originText, String transText) {
        String url = "url";
        //处理 body
        Map<String,Object> req = new HashMap<>();
        Map<String,String> lang = new HashMap<>();
        lang.put("source_language_code",originLanguage);
        lang.put("target_language_code",targetLanguage);
        req.put("config",lang);
        req.put("text",originText);

        String body = JSONUtil.parseObj(req).toString();

        String response = HttpRequest.post(url)
                .header("Appid","15Dy8oGpZg25DldDipPFqyM5HtQ")
                .header("Authorization","Bearer eyJhbGciOiJkaXIiLCJjdHkiOiJKV1QiLCJlbmMiOiJBMTI4R0NNIiwidHlwIjoiSldUIiwiemlwIjoiREVGIn0..5i2VteID0mzsvyEj.Ty4ufjLYYuXd8vIFI5t898rRs7OCWaT0YEJ3mBODxNMPV2N_ergRLhbZYXb_gPWWZ132Q_WiwmTcz9AdjqYMQ1qLZWM4dkPV6stANBUkkIHIB7ZRn8Pops51-pQRkHKFWM5MBMGMshqh1FzREBWF0RYDK11rmVVviv8eR0qsjLnTUE_CC7qZGQa06YJzjdApqaqM73XaIh3OzME130H8loM7kStw_NIMQhhiqvJA-b_h00vvQR7SUFqq8eLxZPI-VzOmZ4ThDW3gjXG0BQMG3sW7n07mMJgK-afN92fWozMLVZHvSpNtBGJxuaQo6lQHiJJTm0qyhno5vto8PunrrlYDG66-cqQpxYRkixhE2gYyeYruiN4ah3OxXa1lGnj06jRQ8mzSW7P8EMt2GjUiMr_jZFYS33cRZkrlHFqluvzUT3W0gSuqa-0R2Pj5M5nSUXXdz517V_bM7-GgDmQJjUxdm8DQSJ53rRz0HsJrRVsk8LKVeuFEctMlahotTRgC0-FLXQ.uUOInbtYnEeGsOsDG9Qnhw")
                .form(body)
                .execute().body();

        JSONObject jsonArray = JSONUtil.parseObj(response);
        String translated_text = String.valueOf(jsonArray.get("translated_text"));

        List<String> row;
        if (transText.equals(translated_text)) {
            row = CollUtil.newArrayList(originLanguage, targetLanguage, originText, transText, translated_text, "正确");
        } else {
            row = CollUtil.newArrayList(originLanguage, targetLanguage, originText, transText, translated_text, "错误");
        }
        //记录用例执行结果
        rows.add(row);

    }

}
