package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

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
 * 結合テスト ログイン機能②
 * ケース17
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース17 受講生 初回ログイン 正常系")
public class Case17 {

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
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() {
		// TODO ここに追加
		WebElement loginIdElement = webDriver.findElement(By.name("loginId"));
		loginIdElement.clear();
		loginIdElement.sendKeys("StudentAA06");
		assertEquals("StudentAA06", loginIdElement.getAttribute("value"));

		WebElement passwordElement = webDriver.findElement(By.name("password"));
		passwordElement.clear();
		passwordElement.sendKeys("StudentAA06");
		assertEquals("StudentAA06", passwordElement.getAttribute("value"));

		//入力値が正しいか確認
		getEvidence(new Object() {
		}, "checkLoginInput");

		WebElement submitElement = webDriver.findElement(By.className("btn-primary"));
		submitElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("セキュリティ規約 | LMS", webDriver.getTitle());

		//ログインが成功したか確認
		getEvidence(new Object() {
		}, "checkLogin");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() {
		// TODO ここに追加
		scrollBy("1000");

		WebElement agreeCheckElement = webDriver.findElement(By.name("securityFlg"));
		agreeCheckElement.click();

		//同意しますにチェックが入っているか確認
		getEvidence(new Object() {
		}, "checkAgree");

		WebElement agreeBtnElement = webDriver.findElement(By.className("btn-primary"));
		agreeBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("パスワード変更 | LMS", webDriver.getTitle());

		//パスワード変更画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkChengePasswordPage");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 変更パスワードを入力し「変更」ボタン押下")
	void test04() {
		// TODO ここに追加
		scrollBy("120");

		WebElement beforeChengePassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"currentPassword\"]"));
		beforeChengePassElement.clear();
		beforeChengePassElement.sendKeys("StudentAA06");
		assertEquals("StudentAA06", beforeChengePassElement.getAttribute("value"));

		WebElement newPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"password\"]"));
		newPassElement.clear();
		newPassElement.sendKeys("NewPassword123");
		assertEquals("NewPassword123", newPassElement.getAttribute("value"));

		WebElement checkPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"passwordConfirm\"]"));
		checkPassElement.clear();
		checkPassElement.sendKeys("NewPassword123");
		assertEquals("NewPassword123", checkPassElement.getAttribute("value"));

		//入力値が正しいか確認
		getEvidence(new Object() {
		}, "checkChengePassInput");

		//変更ボタンを押下
		WebElement chengePassBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]"));
		chengePassBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));

		WebElement chengePassWindowElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-btn\"]"));
		chengePassWindowElement.click();

		wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));

		assertEquals("コース詳細 | LMS", webDriver.getTitle());

		//コース詳細画面に遷移したか確認
		getEvidence(new Object() {
		}, "checkCourseDetailPage");

	}

}
