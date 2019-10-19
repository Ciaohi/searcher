import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

/**
 * Created by ciaohi on 2
 * 019/10/18 16:58
 */

public class LagouSearcher {
    public static void main(String[] args){
        //设置webdriver路径
        System.setProperty("webdriver.chrome.driver",LagouSearcher.class.getClassLoader().getResource("chromedriver.exe").getPath());
        //创建webdriver
        WebDriver webDriver=new ChromeDriver();
        //跳转页面
        webDriver.get("https://www.lagou.com/jobs/list_java?labelWords=&fromSearch=true&suginput=");
        //通过xpath 选中元素
        clickOptions(webDriver, "工作经验", "3年及以下");
        clickOptions(webDriver, "学历要求", "本科");
        clickOptions(webDriver, "融资阶段", "不限");
        clickOptions(webDriver, "公司规模", "不限");
        clickOptions(webDriver, "行业领域", "移动互联网");


        //通过分页解析页面元素
        extractJobsByPagination(webDriver);

    }
    private static void extractJobsByPagination(WebDriver webDriver) {
        List<WebElement> jobElements = webDriver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElements) {
            WebElement moneyElement = jobElement.findElement(By.className("position")).findElement(By.className("money"));
            String companyName=jobElement.findElement(By.className("company_name")).getText();
            System.out.println(companyName+":"+moneyElement.getText());
        }
        //点击下一页
        WebElement nextPageBtn = webDriver.findElement(By.className("pager_next"));
        JavascriptExecutor executor = (JavascriptExecutor)webDriver;
        //如果还有下一页的话点击下一页
        if(!nextPageBtn.getAttribute("class").contains("pager_next_disabled")){
            executor.executeScript("arguments[0].click();", nextPageBtn);
            System.out.println("解析下一页");
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //递归执行
            extractJobsByPagination(webDriver);
        }

    }

    //点击事件点击对应标签筛选信息
    private static void clickOptions(WebDriver webDriver, String choseTitle, String optionTitle) {
        WebElement chosenElement = webDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + choseTitle + "')]"));
        WebElement optionElement = chosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTitle + "')]"));
        optionElement.click();
    }
}
