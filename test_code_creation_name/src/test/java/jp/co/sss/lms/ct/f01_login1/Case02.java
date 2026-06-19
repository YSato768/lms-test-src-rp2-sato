package jp.co.sss.lms.ct.f01_login1;

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
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

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
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// TODO ここに追加
		goTo("http://localhost:8080/lms/");

		WebElement loginIdElement = webDriver.findElement(By.name("loginId"));
		loginIdElement.clear();
		loginIdElement.sendKeys("abc12345");
		assertEquals("abc12345", loginIdElement.getAttribute("value"));

		WebElement passwordElement = webDriver.findElement(By.name("password"));
		passwordElement.clear();
		passwordElement.sendKeys("password123");
		assertEquals("password123", passwordElement.getAttribute("value"));

		//入力内容が正しいかの取得
		getEvidence(new Object() {
		}, "checkInput");

		WebElement submitElement = webDriver.findElement(By.className("btn-primary"));
		submitElement.click();
		assertEquals("ログイン | LMS", webDriver.getTitle());

		WebElement errElement = webDriver.findElement(By.className("help-inline"));
		assertEquals("* ログインに失敗しました。", errElement.getText());

		getEvidence(new Object() {
		}, "checkMsg");
	}

}
