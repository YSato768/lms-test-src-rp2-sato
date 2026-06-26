package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms/");
		assertEquals("ログイン | LMS", webDriver.getTitle());

		//ログイン画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkLoginPage");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms/");

		WebElement loginIdElement = webDriver.findElement(By.name("loginId"));
		loginIdElement.clear();
		loginIdElement.sendKeys("StudentAA01");
		assertEquals("StudentAA01", loginIdElement.getAttribute("value"));

		WebElement passwordElement = webDriver.findElement(By.name("password"));
		passwordElement.clear();
		passwordElement.sendKeys("StudentAA01");
		assertEquals("StudentAA01", passwordElement.getAttribute("value"));

		//入力値が正しいか確認
		getEvidence(new Object() {
		}, "checkInput");

		WebElement submitElement = webDriver.findElement(By.className("btn-primary"));
		submitElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		getEvidence(new Object() {
		}, "checkCourseDetailPage");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		//trタグごとにコースをリストで全取得
		List<WebElement> panelListElement = webDriver.findElements(By.className(
				"panel-primary"));

		List<WebElement> detailListElements = new ArrayList<WebElement>();

		for (WebElement panelElement : panelListElement) {
			detailListElements.addAll(panelElement.findElements(By.xpath(
					".//div[2]/table/tbody/tr")));
		}

		//レポート未提出のコースを取得
		WebElement targetDetailElement = null;
		for (WebElement detailElement : detailListElements) {

			if (detailElement.findElement(By.xpath(
					".//td[3]/span")).getText().equals("未提出")) {
				targetDetailElement = detailElement;
				break;
			}
		}

		Actions actions = new Actions(webDriver);
		actions.scrollToElement(targetDetailElement).perform();
		scrollBy("100");
		//未提出の詳細ボタンをクリック
		targetDetailElement.findElement(By.className("btn-default")).click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		WebElement sendReportTextElement = webDriver.findElement(By.xpath(
				"//*[@id=\"sectionDetail\"]/table/tbody/tr[2]/td/form/input[5]"));

		assertEquals("日報【デモ】を提出する", sendReportTextElement.getAttribute("value"));
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		//セクション詳細画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkSectionDetailPage");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// TODO ここに追加
		WebElement sendReportBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"sectionDetail\"]/table/tbody/tr[2]/td/form/input[5]"));
		sendReportBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		//レポート登録画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkSendReportPage");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// TODO ここに追加
		List<WebElement> formListElement = webDriver.findElements(By.id("content_0"));

		for (WebElement formElement : formListElement) {
			Actions actions = new Actions(webDriver);
			actions.scrollToElement(formElement).perform();
			scrollBy("100");

			formElement.clear();
			formElement.sendKeys("今日もがんばりました。");
		}

		getEvidence(new Object() {
		}, "checkSendReportInput");

		WebElement sendBtnElement = webDriver.findElement(By.className("btn-primary"));

		Actions actions = new Actions(webDriver);
		actions.scrollToElement(sendBtnElement).perform();
		scrollBy("100");

		sendBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		WebElement sendReportTextElement = webDriver.findElement(By.xpath(
				"//*[@id=\"sectionDetail\"]/table/tbody/tr[2]/td/form/input[6]"));

		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		assertEquals("提出済み日報【デモ】を確認する", sendReportTextElement.getAttribute("value"));

		//レポート登録が反映されているか確認
		getEvidence(new Object() {
		}, "checkSendReportSuccess");

	}

}
