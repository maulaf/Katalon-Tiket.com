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



class stepsKeretaAPI {

	@Given("pengguna berada di halaman utama aplikasi tiket.com")
	def homepage() {
		WebUI.openBrowser(GlobalVariable.url)
		WebUI.maximizeWindow()
		TestObject logo = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//img[@alt='Tiket.com logo']")
		WebUI.verifyElementVisible(logo)
		WebUI.verifyTextPresent("Hai kamu, mau ke mana?", false)
	}

	@When("pengguna memilih menu Kereta Api")
	def menuKA() {
		
		WebUI.click(findTestObject('Object Repository/Kereta API/menu_Kereta API'))		
		WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/input_Departure City'), 0)		
		WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/input_Arrival City'), 0)		
		WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/input_Departure City'), 0)
		WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/btn_Cari Kereta API'), 0)
	}
	
	@And("pengguna memasukkan kota asal (.*)")
	def inputDepartureCity(String departureCity) {
		WebUI.setText(findTestObject('Object Repository/Kereta API/input_Departure City'), departureCity)
		WebUI.verifyElementAttributeValue(findTestObject('Object Repository/Kereta API/input_Departure City'), 'value', departureCity, 0)
	}

	@Then("I verify the (.*) in step")
	def I_verify_the_status_in_step(String status) {
		println status
	}
}