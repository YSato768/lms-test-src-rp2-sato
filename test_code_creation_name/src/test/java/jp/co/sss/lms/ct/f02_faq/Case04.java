package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト よくある質問機能
 * ケース04
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース04 よくある質問画面への遷移")
public class Case04 {

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

}
