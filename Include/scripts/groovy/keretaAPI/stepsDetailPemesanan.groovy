package keretaAPI
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.apache.commons.lang.RandomStringUtils
import org.openqa.selenium.By

import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.common.WebUiCommonHelper
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.exception.WebElementNotFoundException

import cucumber.api.java.en.And
import cucumber.api.java.en.Given
import cucumber.api.java.en.Then
import cucumber.api.java.en.When

import com.github.javafaker.Faker as Faker


class stepsDetailPemesanan {
	@Given("pengguna memilih kereta dan berada di halaman pengisian data penumpang")
	def pageDetailPemesanan() {

		TestObject firstCard = new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[@class='ScheduleList_main_card_content__6U8EA'])[1]")
		WebUI.click(firstCard)

		TestObject steps = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//div[contains(@class,'Step_circle_active')]/following-sibling::span")
		WebUI.verifyElementText(steps, "Detail Pemesanan")
	}

	@When("pengguna mengisi detail penumpang")
	def inputDetailPenumpang() {

		dataPemesan()

		String xpath = "(//div[@class='PassengersDetailSection_card_inner__zxNy9'])"
		TestObject object = new TestObject().addProperty("xpath", ConditionType.EQUALS, xpath)
		List<TestObject> element = WebUI.findWebElements(object, 30)

		int count = element.size()
		
		for (int i=1; i<=count; i++) {
			int a = i + 1
			String new_xpath = xpath + "[$a]";
			println(new_xpath)
			TestObject new_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, new_xpath)
			WebUI.scrollToElement(new_object, 0)
			WebUI.click(new_object)
			if (a == count) {
				break;
			}
		}
		
		for (int i=1; i<=count; i++) {
			dataPenumpang(i)
		}
	}
	
	@And("pengguna menekan tombol Lanjutkan")
	def clickLanjutkan() {
		WebUI.scrollToElement(findTestObject('Object Repository/Kereta API/btn_Lanjutkan'), 0)
		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Lanjutkan'))
		WebUI.waitForElementPresent(findTestObject('Object Repository/Kereta API/btn_Lanjut Bayar'), 0)
	}

	@Then("sistem menampilkan halaman Kursi & Perlindungan")
	def pageKursiPerlindungan() {
		WebUI.verifyTextPresent("Detail Kursi", false)
		TestObject steps = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//div[contains(@class,'Step_circle_active')]/following-sibling::span")
		WebUI.verifyElementText(steps, "Kursi & Perlindungan")
	}

	def dataPemesan(){
		Faker faker = new Faker()

		def gender = faker.demographic().sex()
		def prefix = ''
		if(gender == 'Male') {
			prefix = '"mr"'
		}else if(gender == 'Female') {
			prefix = '"mrs"'
		}

		def fullName = faker.name().fullName()
		def hp = RandomStringUtils.randomNumeric(8)
		def email = fullName.replaceAll('[^a-zA-Z0-9]', '') + "@mail.com"


		String radio_xpath = "(//span[.='$prefix'])[1]"

		TestObject radio_Title = findTestObject('Object Repository/Kereta API/radio_Title', [('value') : prefix])
		WebElement element = WebUiCommonHelper.findWebElement(radio_Title, 30)
		WebUI.executeJavaScript("arguments[0].click();", Arrays.asList(element))


		WebUI.setText(findTestObject('Object Repository/Kereta API/input_fullName'), fullName)
		WebUI.setText(findTestObject('Object Repository/Kereta API/input_nomor-ponsel'), hp)
		WebUI.setText(findTestObject('Object Repository/Kereta API/input_emailAddress'), email)
	}

	def dataPenumpang(int i){
		Faker faker = new Faker()

		def gender = faker.demographic().sex()
		def prefix = ''
		if(gender == 'Male') {
			prefix = 'Tuan'
		}else if(gender == 'Female') {
			prefix = 'Nona'
		}

		def firstName = faker.name().firstName().replaceAll('[^a-zA-Z0-9]', '')
		def lastName = faker.name().lastName().replaceAll('[^a-zA-Z0-9]', '')
		def fullName = firstName + " " + lastName
		def nik = RandomStringUtils.randomNumeric(16)

		String radio_xpath = "(//span[.='$prefix'])[$i+1]"

		TestObject radio_Title = new TestObject().addProperty("xpath", ConditionType.EQUALS, radio_xpath)
		WebElement element = WebUiCommonHelper.findWebElement(radio_Title, 30)
		WebUI.executeJavaScript("arguments[0].click();", Arrays.asList(element))

		String fullName_xpath = "(//input[contains(@id,'nama-lengkap-sesuai')])[$i+1]"
		TestObject fullName_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, fullName_xpath)
		WebUI.setText(fullName_object, fullName)

		String nik_xpath = "(//input[@id='nomor-nik'])[$i]"
		TestObject nik_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, nik_xpath)
		WebUI.setText(nik_object, nik)
	}
}