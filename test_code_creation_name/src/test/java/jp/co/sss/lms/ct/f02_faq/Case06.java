package jp.co.sss.lms.ct.f02_faq;

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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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

		//ログインが成功したか確認
		getEvidence(new Object() {
		}, "checkLogin");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// TODO ここに追加
		WebElement functionElement = webDriver.findElement(By.className("dropdown-toggle"));
		functionElement.click();

		WebElement menuElement = webDriver.findElement(By.xpath(
				"//*[@id='nav-content']/ul[1]/li[4]/ul/li[4]/a"));
		menuElement.click();

		assertEquals("ヘルプ | LMS", webDriver.getTitle());

		//ヘルプ画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkHelpPage");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// TODO ここに追加
		//別タブ以降前のページ情報を取得
		String Handle = webDriver.getWindowHandle();

		WebElement helpElement = webDriver.findElement(By.linkText("よくある質問"));
		helpElement.click();

		//ページ情報リストを取得し、異なるページ情報を取得
		String newHandle = null;
		for (String id : webDriver.getWindowHandles()) {
			if (!id.equals(Handle)) {
				newHandle = id;
			}
		}
		webDriver.switchTo().window(newHandle);

		assertEquals("よくある質問 | LMS", webDriver.getTitle());

		//よくある質問画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkFAQ");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		// TODO ここに追加
		WebElement searchedByCategoryElement = webDriver.findElement(By.linkText("【研修関係】"));
		searchedByCategoryElement.click();

		//よくある質問リストを2件取得し終えるまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(1));
		By listPath = By.xpath(
				"//*[@id=\"question-h[${status.index}]\"]/dt/span[2]");
		wait.until(ExpectedConditions.numberOfElementsToBe(listPath, 2));

		//よくある質問リストを取得
		List<WebElement> searchedByCategoryList = new ArrayList<WebElement>();
		searchedByCategoryList = webDriver.findElements(By.xpath(
				"//*[@id=\"question-h[${status.index}]\"]/dt/span[2]"));
		assertEquals(2, searchedByCategoryList.size());

		//取得した質問リストを該当するテキストに分別
		String howToCancel = null;
		String howToApply = null;
		if (searchedByCategoryList.get(0).getText().equals("キャンセル料・途中退校について")) {
			howToCancel = searchedByCategoryList.get(0).getText();
			howToApply = searchedByCategoryList.get(1).getText();
		} else {
			howToApply = searchedByCategoryList.get(0).getText();
			howToCancel = searchedByCategoryList.get(1).getText();
		}

		assertEquals("キャンセル料・途中退校について", howToCancel);
		assertEquals("研修の申し込みはどのようにすれば良いですか？", howToApply);

		scrollBy("1000");

		//検索結果の確認
		getEvidence(new Object() {
		}, "checkFaqTextByCategory");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// TODO ここに追加
		//回答表示ボタンをリストで取得
		List<WebElement> showAnswerButtunList = webDriver.findElements(By.className("mb10"));

		for (WebElement showAnswerButtunElement : showAnswerButtunList) {
			showAnswerButtunElement.click();
		}
		scrollBy("1000");

		//回答をリストで取得
		List<WebElement> answerElementList = webDriver.findElements(By.xpath(
				"//*[@id=\"answer-h[${status.index}]\"]/span[2]"));
		//回答リストから該当する回答に分別
		String answerOfCancel = null;
		String answerOfApply = null;
		for (WebElement answerElement : answerElementList) {
			if (answerElement.getText().equals(
					"受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、"
							+ "事情をお伺いした上で、協議という形を取らせて頂きます。 "
							+ "弊社営業担当までご相談下さい。")) {
				answerOfCancel = answerElement.getText();
			} else {
				answerOfApply = answerElement.getText();
			}
		}

		assertEquals("受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、"
				+ "事情をお伺いした上で、協議という形を取らせて頂きます。"
				+ " 弊社営業担当までご相談下さい。", answerOfCancel);
		assertEquals("営業担当がいる場合は、営業担当までご連絡ください。 "
				+ "申し込み方法についてご案内させていただきます。 "
				+ "なお、弊社営業営業がいない場合は、"
				+ "東京ITスクール運営事務局までご連絡いただけると幸いです。", answerOfApply);

		//回答表示の確認
		getEvidence(new Object() {
		}, "checkFaqAnswer");

	}

}
