package jp.co.sss.lms.ct.f05_exam;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.Date;
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
 * 結合テスト 試験実施機能
 * ケース13
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース13 受講生 試験の実施 結果0点")
public class Case13 {

	/** テスト07およびテスト08 試験実施日時 */
	static Date date;

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
	@DisplayName("テスト03 「試験有」の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// TODO ここに追加
		//trタグごとにコースをリストで全取得
		List<WebElement> datailListElement = webDriver.findElements(By.xpath(
				"//*[@id=\"main\"]/div/div[2]/div[2]/table/tbody/tr"));

		//試験有のコースを取得
		WebElement targetDetailElement = null;
		for (WebElement detailElement : datailListElement) {

			if (detailElement.findElement(By.xpath(
					".//td[4]/span")).getText().equals("試験有")) {
				targetDetailElement = detailElement;
				break;
			}

		}

		//試験有の詳細ボタンをクリック
		targetDetailElement.findElement(By.className("btn-default")).click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		//セクション詳細画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkSectionDetailPage");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「本日の試験」エリアの「詳細」ボタンを押下し試験開始画面に遷移")
	void test04() {
		// TODO ここに追加
		WebElement detailBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"sectionDetail\"]/table[1]/tbody/tr[2]/td[2]/form/input[1]"));
		detailBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("試験【ITリテラシー①】 | LMS", webDriver.getTitle());
		//試験開始画面が表示されたか確認
		getEvidence(new Object() {
		}, "checkStartExamPage");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「試験を開始する」ボタンを押下し試験問題画面に遷移")
	void test05() {
		// TODO ここに追加
		WebElement startExamBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"main\"]/div/form/input[4]"));

		startExamBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		WebElement radioBtnElement = webDriver.findElement(By.id("answer-0-0"));

		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		assertTrue(radioBtnElement.isDisplayed());

		//試験開始画面が表示されたか確認
		getEvidence(new Object() {
		}, "checkExamPage");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 未回答の状態で「確認画面へ進む」ボタンを押下し試験回答確認画面に遷移")
	void test06() {
		// TODO ここに追加
		scrollBy("10000");

		//回答時間が短すぎてnullになってしまうのを防ぐため5秒待機
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebElement finishExamElement = webDriver.findElement(By.xpath(
				"//*[@id=\"examQuestionForm\"]/div[13]/fieldset/input"));
		finishExamElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		//試験回答確認画面が表示されたか確認
		WebElement answerElement = webDriver.findElement(By.xpath(
				"//*[@id=\"examBeing\"]/div[1]/div[2]/h6"));

		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		assertEquals("あなたの回答", answerElement.getText());

		//試験開始画面が表示されたか確認
		getEvidence(new Object() {
		}, "checkExamAnswerCheckPage");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 「回答を送信する」ボタンを押下し試験結果画面に遷移")
	void test07() throws InterruptedException {
		// TODO ここに追加
		scrollBy("10000");

		//回答時間が短すぎてnullになってしまうのを防ぐため5秒待機
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		WebElement sendAnswerElement = webDriver.findElement(By.id("sendButton"));
		sendAnswerElement.click();

		webDriver.switchTo().alert().accept();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		WebElement answerElement = webDriver.findElement(By.tagName("h3"));
		assertEquals("ITリテラシー① | LMS", webDriver.getTitle());
		assertEquals("【不正解】", answerElement.getText());

		//試験結果画面が表示されたか確認
		getEvidence(new Object() {
		}, "checkExamAnswerPage");

	}

	@Test
	@Order(8)
	@DisplayName("テスト08 「戻る」ボタンを押下し試験開始画面に遷移後当該試験の結果が反映される")
	void test08() {
		// TODO ここに追加
		scrollBy("10000");

		WebElement backBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"examBeing\"]/div[13]/fieldset/form/input[1]"));
		backBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		scrollBy("100");

		List<WebElement> scoreNumElements = webDriver.findElements(By.xpath(
				"//*[@id=\"main\"]/div/table[2]/tbody/tr"));
		WebElement lastScoreElement = scoreNumElements.get(scoreNumElements.size() - 1);

		Actions actions = new Actions(webDriver);
		actions.scrollToElement(lastScoreElement).perform();
		scrollBy("100");

		assertEquals("試験【ITリテラシー①】 | LMS", webDriver.getTitle());
		assertEquals("0.0点", lastScoreElement.findElement(By.xpath(
				".//td[2]")).getText());

		//試験点数が表示されたか確認
		getEvidence(new Object() {
		}, "checkExamScore");

	}

}
