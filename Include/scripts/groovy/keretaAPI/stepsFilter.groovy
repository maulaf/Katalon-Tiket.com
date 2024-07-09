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



class stepsFilter {

	@Given("pengguna berada di halaman hasil pencarian kereta api")
	def hasilCariTiket() {
		WebUI.callTestCase(findTestCase('Mencari Kereta Api'), [:], FailureHandling.STOP_ON_FAILURE)
	}

	@When("pengguna memilih kelas (.*) dari opsi filter")
	def pilihKelas(String classKA) {
		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Filter dan Urutkan'))
		WebUI.click(findTestObject('Object Repository/Kereta API/text_Tipe Kelas'))

		TestObject checkbox = findTestObject('Object Repository/Kereta API/checkbox_Tipe Kelas', [('classKA') : classKA])
		WebElement element = WebUiCommonHelper.findWebElement(checkbox, 30)
		WebUI.executeJavaScript("arguments[0].click();", Arrays.asList(element))
	}

	@And("pengguna menekan tombol simpan")
	def clickSimpan() {
		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Simpan'))
	}

	@Then("sistem menampilkan halaman detail kereta api kelas (.*) yang dipilih")
	def hasilFilterKelass(String classKA) {
		String xpath = "(//div[@class='Text_text__DSnue Text_variant_lowEmphasis__VihAq Text_size_b2__y3Q2E')"

		TestObject object = new TestObject().addProperty("xpath", ConditionType.EQUALS, xpath)

		List<TestObject> element = WebUI.findWebElements(object, 30)

		int count = element.size()

		for(int i=1; i<=count; i++) {
			String new_xpath = xpath + "[$i]"
			println(new_xpath)

			String getText = WebUI.getText(new TestObject().addProperty("xpath", ConditionType.EQUALS, new_xpath))

			if(classKA == "EKO") {
				assert getText.contains("Ekonomi")
			}else if((classKA == "EKS")) {
				assert getText.contains("Eksekutif")
			}
		}
	}
}