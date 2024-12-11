package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dev.failsafe.internal.util.Assert;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    

    public static boolean click(WebElement element, WebDriver driver ){
        if(element.isDisplayed()){
            try{
                JavascriptExecutor js = (JavascriptExecutor)driver;
                js.executeScript("arguments[0].scrollIntoView(true)", element);
                element.click();
                Thread.sleep(2000);
                return true;
            }catch(Exception e){
                return false;
            }
        }
        return false;
    }
    // public static Boolean validatePercentage(WebDriver driver,By locator,double range){
    //     Boolean success;
    //     WebDriverWait wait = new WebDriverWait((WebDriver) driver,Duration.ofSeconds(10));
    //     wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    //     List<WebElement> teams= driver.findElements(locator);
    //     HashMap<String,String> teamSet = new HashMap<>();
    //     for(WebElement team : teams){
    //         String winpercent = team.findElement(By.xpath("//td[contains(@class,'pct text')]")).getText();

    //     }

    // }
    public static void scrape(String year, WebDriver driver){
        try{
            WebElement yearLink = driver.findElement(By.id(year));
            String yearLinkText= yearLink.getText();

            click(yearLink,driver);
            Thread.sleep(5000);
            

            // yearLink.click();
            WebDriverWait wait = new WebDriverWait((WebDriver) driver,Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@class='film']")));

            ArrayList<HashMap<String,String>> movieList = new ArrayList<>();

            List<WebElement> filmRows = driver.findElements(By.xpath("//tr[@class='film']"));

            int count = 1;
            // for(WebElement filmRow : filmRows){
            // for(int i = 0; i<5; i++){
                for(int i = 0; i<Math.min(filmRows.size(),5); i++){
                    // for(WebElement filmRow : filmRows){
                        WebElement filmRow=filmRows.get(i);
                
                    String filmTitle = filmRow.findElement(By.xpath(".//td[contains(@class,'title')]")).getText();
                    String nomination = filmRow.findElement(By.xpath(".//td[contains(@class,'nominations')]")).getText();
                    String awards= filmRow.findElement(By.xpath(".//td[contains(@class,'awards')]")).getText();
                    boolean isWinnerFlag = count==1;
                    String isWinner= String.valueOf(isWinnerFlag);

                    long epoch = System.currentTimeMillis()/1000;
                    String epochTime = String.valueOf(epoch);

                    HashMap<String,String> movieMap= new HashMap<>();
                    movieMap.put("epochTime", epochTime);
                    movieMap.put("yearLinkText", yearLinkText);
                    movieMap.put("title", filmTitle);
                    movieMap.put("nomination", nomination);
                    movieMap.put("awards", awards);
                    movieMap.put("isWinner", isWinner);

                    movieList.add(movieMap);
                    
                // }
                
                
            }
            System.out.println("Five movies of "+yearLinkText+"  are .........");
            for(HashMap<String,String> movieData: movieList){
                // System.out.println("These are movies list");
                System.out.println("Epoch time: " + movieData.get("epochTime")+ ", Year: "+movieData.get("yearLinkText")
                     +", Film title: "+movieData.get("title")+", Nomination: "+movieData.get("nomination")
                     +", Awards: "+movieData.get("awards")+", Best Picture: "+movieData.get("isWinner"));

            }
        
            System.out.println("These are the five movies");

            ObjectMapper objectMapper=new ObjectMapper();
            try{
                String userDir = System.getProperty("user.dir");
                File jsonFile= new File(userDir+"/src/test/resources/"+ year+"-oscar-winner-data.json");
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, movieList);
                System.out.println("JS0N data written to:"+jsonFile.getAbsolutePath());
                org.testng.Assert.assertTrue(jsonFile.length() !=0);
            }catch(IOException ioe){
                ioe.printStackTrace();
            }

        }catch(Exception e){
            System.out.println("Web Scrap for movie is failed..");
            e.printStackTrace();
        }
    }
    
}
//tr[@class='team']
//for(int i = 0; i<5; i++){