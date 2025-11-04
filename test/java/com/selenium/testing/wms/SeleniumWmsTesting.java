package com.selenium.testing.wms;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WindowType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;

import java.time.Duration;
import java.util.*;

public class SeleniumWmsTesting {

    public static void main(String[] args) throws InterruptedException, IOException {
        System.setProperty("webdriver.chrome.driver", "C:\\Webdrivers\\chromedriver-win64 (3)\\chromedriver-win64\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        // Use your existing Chrome profile (with Tampermonkey already installed)
        options.addArguments("user-data-dir=C:\\Users\\gkkcw\\AppData\\Local\\Google\\Chrome\\User Data\\Default");
        options.addArguments("profile-directory=Default");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 1. Start session
        driver.manage().deleteAllCookies();
        Thread.sleep(3000);
        driver.manage().window().maximize();
        driver.get("https://www.amazon.ca/-/fr/gp/css/homepage.html?ref_=nav_youraccount_btn");
        Thread.sleep(3000);

        /*
        // 2. Sign in once
        driver.findElement(By.xpath("//span[@id='nav-link-accountList-nav-line-1']")).click();
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));
        emailField.sendKeys("omni-mlda-test0+TXTCA1@amazon.com");

        driver.findElement(By.id("continue")).click();
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ap_password")));
        passwordField.sendKeys("1TestTextCa1");
        driver.findElement(By.id("signInSubmit")).click();

*/

        // 3. Navigate to Help
        driver.findElement(By.xpath("//a[contains(@href,'/-/fr/gp/help/customer/display.html?ref_=nav_cs_help')]")).click();

        WebElement helpButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[normalize-space()='Aide pour autre chose']")));
        js.executeScript("arguments[0].scrollIntoView(true);", helpButton);
        helpButton.click();

        WebElement secondButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='page-wrapper hero-banner']//li[11]//div[1]//div[2]")));
        js.executeScript("arguments[0].scrollIntoView(true);", secondButton);
        secondButton.click();

        driver.findElement(By.xpath("//span[contains(text(),'J’ai besoin d’une aide supplémentaire')]")).click();

        // 4. Switch to new chat window
        String originalWindow = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);

                break;
            }
        }
        String chatTab = driver.getWindowHandle();


        WebElement startChatButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(@class,'fs-button')]")));
        startChatButton.click();

        Thread.sleep(10000);


        // 5. Open Excel file instead of Quip
        String excelPath = "C:\\Users\\gkkcw\\Documents\\SeleniumWMStesting.xlsx";
        File excelFile = new File(excelPath);
        if (!excelFile.exists()) throw new FileNotFoundException("Excel file not found: " + excelPath);

        FileInputStream fis = new FileInputStream(excelPath);

        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0); // Use first sheet

        System.out.println("Loaded Excel file: " + excelPath);

        for (Row row : sheet) {
            Cell cell = row.getCell(0); // Column A
            if (cell != null) {
                System.out.println("Found prompt: " + cell.getStringCellValue());
            }
        }

// 6. Loop through prompts in column 0 (A)
        int rowIndex = 1; // Assuming row 0 is header
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) continue;

            Cell promptCell = row.getCell(0);
            if (promptCell == null) continue;

            String prompt = promptCell.getStringCellValue().trim();
            if (prompt.isEmpty()) continue;

            System.out.println("Processing row " + rowIndex + ": " + prompt);

            // Switch to chat tab
            driver.switchTo().window(chatTab);

            Thread.sleep(10000);

            // Send the prompt to chat
            WebElement chatInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("#landing-page-chat-box")));
            chatInput.sendKeys(prompt);
            chatInput.sendKeys(Keys.ENTER);
            System.out.println(" Sent prompt to chat: " + prompt);
            Thread.sleep(8000);

/*
            // 5. Open Quip sheet
        // Save chat tab handle


        driver.switchTo().newWindow(WindowType.TAB); // Open new tab for Quip

        driver.get("https://quip.com/Z7h9AW30fKxt");
        String quipTab = driver.getWindowHandle(); // Save quip tab handle

        driver.findElement(By.xpath("//input[@placeholder='Work email address']")).sendKeys("xxxxxxxxx");
        driver.findElement(By.xpath("//button[@id='email-submit']")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class,'spreadsheet')]"))).click();

        // 6. Get prompts
        List<WebElement> promptCells = driver.findElements(By.xpath(
                "(//div[contains(@class,'spreadsheet-row')])//div[@data-col='0' and @contenteditable='true']"));

        System.out.println("Found " + promptCells.size() + " prompt(s).");
        driver.switchTo().window(chatTab);


        int rowIndex = 1;
        for (WebElement promptCell : promptCells) {
            String prompt = promptCell.getText().trim();
            if (prompt.isEmpty()) break;

            System.out.println(" Processing row " + rowIndex + ": " + prompt);

            WebElement chatInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("chat-input")));
            chatInput.sendKeys(prompt);
            chatInput.sendKeys(Keys.ENTER);
            System.out.println(" Sent prompt to chat: " + prompt);
            Thread.sleep(8000);

            */

            // 7. Process bot response
            List<String> possibleAnswers = Arrays.asList(
                    "Your order is on the way!",
                    "Please provide your order ID.",
                    "Sorry, I couldn’t find your order."
            );

            String matchedAnswer = waitForAnyResponse(driver, wait, possibleAnswers);
            System.out.println("Bot responded with: " + matchedAnswer);

            if (matchedAnswer.contains("order ID")) {
                WebElement reply = driver.findElement(By.id("chat-input"));
                reply.sendKeys("Order12345");
                reply.sendKeys(Keys.ENTER);
            }
            driver.switchTo().newWindow(WindowType.TAB);
            String chatURL = driver.getCurrentUrl();
            System.out.println("Captured chat window URL: " + chatURL);
            driver.switchTo().window(originalWindow);


/*

            // 8. Return to Quip tab
            driver.switchTo().window(quipTab);





            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'spreadsheet')]")));

            // Paste chat URL into column 1 of current row
            WebElement resultCell = driver.findElement(By.xpath(
                    "(//div[contains(@class,'spreadsheet-row')])[" + rowIndex + "]//div[@data-col='1']"));
            resultCell.click();
            resultCell.sendKeys(chatURL);

            System.out.println("✅ Chat URL pasted into Quip row " + rowIndex);

            rowIndex++;

            */


            // Write URL to Excel (column 1, i.e., column B)
            Cell linkCell = row.createCell(1);
            linkCell.setCellValue(chatURL);

            rowIndex++;
        }

// ✅ Save and close Excel
        fis.close();
        FileOutputStream fos = new FileOutputStream(excelPath);
        workbook.write(fos);
        fos.close();
        workbook.close();

        driver.quit();
    }


    /**
     * Waits for any one of the known bot responses to appear.
     */
    private static String waitForAnyResponse(WebDriver driver, WebDriverWait wait, List<String> possibleAnswers) {
        for (String answer : possibleAnswers) {
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//*[contains(text(),'" + answer + "')]")));
                return answer;
            } catch (TimeoutException ignored) {
            }
        }
        throw new RuntimeException("❌ No known bot response appeared within timeout!");
    }

}
/*

/*

// ===============================================
// 6. Loop through prompts from Excel and send to chat
// ===============================================

// Loop through all rows starting from 1 (assuming row 0 is header)
for (int i = 1; i <= sheet.getLastRowNum(); i++) {
    Row row = sheet.getRow(i);
    if (row == null) continue;

    Cell promptCell = row.getCell(0);
    if (promptCell == null) continue;

    String prompt = promptCell.getStringCellValue().trim();
    if (prompt.isEmpty()) continue;

    System.out.println("🟦 Processing row " + i + ": " + prompt);

    // Switch back to chat tab
    driver.switchTo().window(chatTab);

    // ======================================================
    // STEP 1 — Locate the chat iframe
    // ======================================================
    // Tip: Inspect the Amazon chat page, find which iframe contains the input box.
    // Common patterns include:
    //   <iframe id="chat-widget" src="https://...">
    // or multiple iframes, where only one holds the textarea.
    //
    // To automatically detect:
    List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
    System.out.println("Found " + iframes.size() + " iframe(s) on chat page.");

    boolean foundChatFrame = false;

    for (WebElement frame : iframes) {
        // Switch into this frame temporarily
        driver.switchTo().frame(frame);

        // Check if this frame has the chat input box
        List<WebElement> chatInputs = driver.findElements(By.xpath(
            "//textarea | //input[@type='text' or contains(@placeholder,'message')]"));

        if (!chatInputs.isEmpty()) {
            System.out.println("✅ Found chat frame!");
            foundChatFrame = true;

            // STEP 2 — Send the prompt inside the chat
            WebElement chatInput = chatInputs.get(0);
            chatInput.sendKeys(prompt);
            chatInput.sendKeys(Keys.ENTER);
            System.out.println("💬 Sent prompt: " + prompt);

            // Optional: wait for bot response
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'order') or contains(text(),'Sorry') or contains(text(),'Your')]")));
            } catch (TimeoutException e) {
                System.out.println("⚠️ No visible response detected for this prompt.");
            }

            // STEP 3 — Exit the frame when done
            driver.switchTo().defaultContent();
            break;
        }

        // If not found, return to main document and try next iframe
        driver.switchTo().defaultContent();
    }

    if (!foundChatFrame) {
        System.out.println("❌ Could not locate any chat iframe for this prompt. Skipping...");
        continue;
    }

    // ======================================================
    // STEP 4 — Capture chat page URL (if needed)
    // ======================================================
    // Open a new tab briefly, get the current chat session URL, and write it to Excel
    String newTab = driver.switchTo().newWindow(WindowType.TAB).getWindowHandle();
    String chatURL = driver.getCurrentUrl();
    driver.close(); // close the new tab
    driver.switchTo().window(chatTab); // return to chat tab

    // Write URL to Excel (column B)
    Cell linkCell = row.createCell(1);
    linkCell.setCellValue(chatURL);

    System.out.println("🔗 Saved chat URL for row " + i + ": " + chatURL);
}

 */