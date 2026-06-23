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
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
		loginIdElement.sendKeys("StudentAA04");
		assertEquals("StudentAA04", loginIdElement.getAttribute("value"));

		WebElement passwordElement = webDriver.findElement(By.name("password"));
		passwordElement.clear();
		passwordElement.sendKeys("StudentAA04");
		assertEquals("StudentAA04", passwordElement.getAttribute("value"));

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
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() {
		// TODO ここに追加
		scrollBy("1000");

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

		assertEquals("パスワード変更 | LMS", webDriver.getTitle());

		scrollBy("120");

		//各種エラーメッセージを取得
		WebElement beforeChengePassErrElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[1]/div/ul/li/span"));
		WebElement newPassErrElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"));
		WebElement checkPassErrElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[3]/div/ul/li/span"));

		assertEquals("現在のパスワードは必須です。", beforeChengePassErrElement.getText());

		//エラーメッセージの順序が固定ではないため、それぞれのパターンで分別
		String needPassFront = null;
		String needPassRear = null;
		if (newPassErrElement.getText().equals(
				"パスワードは必須です。\n"
						+ "「パスワード」には半角英数字のみ使用可能です。"
						+ "また、半角英大文字、半角英小文字、"
						+ "数字を含めた8～20文字を入力してください。")) {
			needPassFront = newPassErrElement.getText();
			assertEquals(
					"パスワードは必須です。\n"
							+ "「パスワード」には半角英数字のみ使用可能です。"
							+ "また、半角英大文字、半角英小文字、"
							+ "数字を含めた8～20文字を入力してください。",
					needPassFront);
		} else {
			needPassRear = newPassErrElement.getText();
			assertEquals(
					"「パスワード」には半角英数字のみ使用可能です。"
							+ "また、半角英大文字、半角英小文字、"
							+ "数字を含めた8～20文字を入力してください。\n"
							+ "パスワードは必須です。",
					needPassRear);
		}

		assertEquals("確認パスワードは必須です。", checkPassErrElement.getText());

		//各種エラーメッセージが表示されているか確認
		getEvidence(new Object() {
		}, "checkEmptyErrMsg");

	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() {
		// TODO ここに追加
		WebElement beforeChengePassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"currentPassword\"]"));
		beforeChengePassElement.clear();
		beforeChengePassElement.sendKeys("StudentAA04");
		assertEquals("StudentAA04", beforeChengePassElement.getAttribute("value"));

		WebElement newPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"password\"]"));
		newPassElement.clear();
		newPassElement.sendKeys("NewPasswordNewPassword123");
		assertEquals("NewPasswordNewPassword123", newPassElement.getAttribute("value"));

		WebElement checkPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"passwordConfirm\"]"));
		checkPassElement.clear();
		checkPassElement.sendKeys("NewPasswordNewPassword123");
		assertEquals("NewPasswordNewPassword123", checkPassElement.getAttribute("value"));

		//入力値が正しいか確認
		getEvidence(new Object() {
		}, "checkPassOver20Input");

		//変更ボタンを押しエラーメッセージを表示
		scrollBy("120");

		WebElement chengePassBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]"));
		chengePassBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));

		WebElement chengePassWindowElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-btn\"]"));
		chengePassWindowElement.click();

		wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));

		WebElement newPassErrElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"));
		assertEquals("パスワードの長さが最大値(20)を超えています。",
				newPassErrElement.getText());

		//エラーメッセージを確認
		getEvidence(new Object() {
		}, "checkPassOver20ErrMsg");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() {
		// TODO ここに追加 
		scrollBy("120");

		WebElement beforeChengePassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"currentPassword\"]"));
		beforeChengePassElement.clear();
		beforeChengePassElement.sendKeys("StudentAA04");
		assertEquals("StudentAA04", beforeChengePassElement.getAttribute("value"));

		WebElement newPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"password\"]"));
		newPassElement.clear();
		newPassElement.sendKeys("NewPassword");
		assertEquals("NewPassword", newPassElement.getAttribute("value"));

		WebElement checkPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"passwordConfirm\"]"));
		checkPassElement.clear();
		checkPassElement.sendKeys("NewPassword");
		assertEquals("NewPassword", checkPassElement.getAttribute("value"));

		//入力値が正しいか確認
		getEvidence(new Object() {
		}, "checkPassPolicyInput");

		//変更ボタンを押しエラーメッセージを表示
		WebElement chengePassBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]"));
		chengePassBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));

		WebElement chengePassWindowElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-btn\"]"));
		chengePassWindowElement.click();

		wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));

		scrollBy("120");

		WebElement newPassErrElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"));
		assertEquals("「パスワード」には半角英数字のみ使用可能です。"
				+ "また、半角英大文字、半角英小文字、"
				+ "数字を含めた8～20文字を入力してください。",
				newPassErrElement.getText());

		//エラーメッセージを確認
		getEvidence(new Object() {
		}, "checkPassPolicyErrMsg");

	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() {
		// TODO ここに追加
		WebElement beforeChengePassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"currentPassword\"]"));
		beforeChengePassElement.clear();
		beforeChengePassElement.sendKeys("StudentAA04");
		assertEquals("StudentAA04", beforeChengePassElement.getAttribute("value"));

		WebElement newPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"password\"]"));
		newPassElement.clear();
		newPassElement.sendKeys("NewPassword123");
		assertEquals("NewPassword123", newPassElement.getAttribute("value"));

		WebElement checkPassElement = webDriver.findElement(By.xpath(
				"//*[@id=\"passwordConfirm\"]"));
		checkPassElement.clear();
		checkPassElement.sendKeys("DifferentPass123");
		assertEquals("DifferentPass123", checkPassElement.getAttribute("value"));

		//入力値が正しいか確認
		getEvidence(new Object() {
		}, "checkDifferentPassInput");

		//変更ボタンを押しエラーメッセージを表示
		WebElement chengePassBtnElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[4]/div/button[2]"));
		chengePassBtnElement.click();

		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("upd-btn")));

		WebElement chengePassWindowElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-btn\"]"));
		chengePassWindowElement.click();

		wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("error")));

		scrollBy("120");

		WebElement newPassErrElement = webDriver.findElement(By.xpath(
				"//*[@id=\"upd-form\"]/div[1]/fieldset/div[2]/div/ul/li/span"));
		assertEquals("パスワードと確認パスワードが一致しません。",
				newPassErrElement.getText());

		//エラーメッセージを確認
		getEvidence(new Object() {
		}, "checkDifferentPassErrMsg");

	}

}
