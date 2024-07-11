package keretaAPI
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
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
import com.kms.katalon.core.webui.keyword.internal.WebUIAbstractKeyword

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

import java.time.format.DateTimeFormatter
import java.time.LocalDate

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Keys


class stepsSeacrhTiket {

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

		String xpath = "(//div[@class='station-name'])"
		TestObject object = new TestObject().addProperty('xpath', ConditionType.EQUALS, xpath)

		List<TestObject> element = WebUI.findWebElements(object, 30)

		int count = element.size()

		List<String> originStationName = []

		for (int i = 1; i <= count; i++) {
			TestObject objectStation = new TestObject().addProperty('xpath', ConditionType.EQUALS, "$xpath[$i]")
			String stationName = WebUI.getText(objectStation)
			originStationName.add(stationName)
		}

		GlobalVariable.originStationName = originStationName
		println("originStationName = $GlobalVariable.originStationName")

		WebUI.click(new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[@class='station-city'])[1]"))
	}

	@And("pengguna memasukkan kota tujuan (.*)")
	def inputArrivalCity(String arrivalCity) {
		WebUI.setText(findTestObject('Object Repository/Kereta API/input_Arrival City'), arrivalCity)
		WebUI.verifyElementAttributeValue(findTestObject('Object Repository/Kereta API/input_Arrival City'), 'value', arrivalCity, 0)

		String xpath = "(//div[@class='station-name'])"
		TestObject object = new TestObject().addProperty('xpath', ConditionType.EQUALS, xpath)

		List<TestObject> element = WebUI.findWebElements(object, 30)

		int count = element.size()

		List<String> destinationStationName = []

		for (int i = 1; i <= count; i++) {
			TestObject objectStation = new TestObject().addProperty('xpath', ConditionType.EQUALS, "$xpath[$i]")
			String stationName = WebUI.getText(objectStation)
			destinationStationName.add(stationName)
		}

		GlobalVariable.destinationStationName = destinationStationName
		println("destinationStationName = $GlobalVariable.destinationStationName")

		WebUI.click(new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[@class='station-city'])[1]"))
	}

	@And("pengguna memilih tanggal keberangkatan (.*)")
	def inputDepartDate(def departDate) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new java.util.Locale("id"))

		LocalDate dateDepart = LocalDate.parse(departDate, inputFormatter)
		String formattedDate = dateDepart.format(outputFormatter)
		println('Formatted departure date: ' + formattedDate)

		if (dateDepart.isBefore(LocalDate.now())) {
			throw new Exception("Tanggal keberangkatan tidak valid, tidak dapat melakukan pemesanan.")
		}

		String new_xpath = "//td[contains(@aria-label,'$formattedDate')]"
		println("new_xpath = $new_xpath")

		TestObject dynamicObject = new TestObject('dynamicObject').addProperty('xpath', ConditionType.EQUALS, new_xpath)

		boolean departDatePresent = WebUI.verifyElementVisible(dynamicObject, FailureHandling.OPTIONAL)

		while (!(departDatePresent)) {
			TestObject nextMonth = new TestObject().addProperty('xpath', ConditionType.EQUALS, '//span[@class=\'widget-date-prev-next-logo\']/i[@class=\'tix tix-chevron-right\']')
			WebUI.click(nextMonth)
			departDatePresent = WebUI.verifyElementVisible(dynamicObject, FailureHandling.OPTIONAL)
		}

		WebUI.click(dynamicObject)
	}

	@And("pengguna memilih penumpang : (\\d+) dewasa, (\\d+) bayi")
	def inputPenumpang(int totalAdult, int totalInfant) {

		int totalPassenger = totalAdult + totalInfant

		if (totalPassenger > 8) {
			throw new Exception("Anda hanya dapat memesan maksimal 8 penumpang.");
		}

		if (totalAdult < 1) {
			throw new Exception("Minimal memesan 1 penumpang dewasa.");
		}

		if (totalAdult > 4 || totalInfant > 4) {
			throw new Exception("Maksimal 4 penumpang dewasa atau bayi.");
		}

		WebUI.click(findTestObject('Object Repository/Kereta API/input_Penumpang'))

		for (int i = 1; i < totalAdult; i++) {
			if ((totalAdult != 1)) {
				WebUI.click(findTestObject('Object Repository/Kereta API/increment_Adult'))
			}
		}

		for (int i = 0; i < totalInfant; i++) {
			if (totalInfant != 0) {
				WebUI.click(findTestObject('Object Repository/Kereta API/increment_Infant'))
			}
		}

		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Selesai'))

		GlobalVariable.totalAdult = totalAdult
		GlobalVariable.totalInfant = totalInfant
	}

	@And("pengguna menekan tombol Cari")
	def clickCari() {
		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Cari Kereta API'))
		TestObject lewati = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//button[.='Lewati']")
		boolean isPresent = WebUI.verifyElementVisible(lewati, FailureHandling.OPTIONAL)

		if (isPresent) {
			WebUI.click(lewati)
		}
	}

	@Then("sistem menampilkan daftar kereta api yang tersedia dari (.*) ke (.*) pada tanggal (.*)")
	def verifyCariTiket(String deapartureCity, String arrivalCity, def departDate ) {

		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMM yy", new java.util.Locale("id"))

		LocalDate dateDepart = LocalDate.parse(departDate, inputFormatter)
		GlobalVariable.departDate = dateDepart.format(outputFormatter)
		println('Formatted departure date: ' + GlobalVariable.departDate)

		String getDate = WebUI.getText(new TestObject().addProperty("class", ConditionType.EQUALS, "ChangeSearch_departure_date__d7iOq Text_text__DSnue Text_size_b2__y3Q2E Text_weight_bold__m4BAY"))
		assert getDate.contains(GlobalVariable.departDate)

		boolean isOriginTextPresent = false

		// Loop through each station name in the list
		for (String originStationName : GlobalVariable.originStationName) {
			// Verify if the station name text is present on the page
			if (WebUI.verifyTextPresent(originStationName, false, FailureHandling.OPTIONAL)) {
				isOriginTextPresent = true
				println("Text '$originStationName' is present on the page.")
			}
		}

		if (!isOriginTextPresent) {
			println("None of the origin are present on the page.")
			throw new AssertionError("Verification failed: None of the origin are present on the page.")
		} else {
			println("At least one of the specified origin is present on the page.")
		}

		boolean isDestinationTextPresent = false

		// Loop through each station name in the list
		for (String destinationStationName : GlobalVariable.destinationStationName) {
			// Verify if the station name text is present on the page
			if (WebUI.verifyTextPresent(destinationStationName, false, FailureHandling.OPTIONAL)) {
				isDestinationTextPresent = true
				println("Text '$destinationStationName' is present on the page.")
			}
		}

		if (!isDestinationTextPresent) {
			println("None of the destination are present on the page.")
			throw new AssertionError("Verification failed: None of the destination are present on the page.")
		} else {
			println("At least one of the specified destination is present on the page.")
		}

		verifyRoutePrice()


	}

	def verifyRoutePrice() {
		String xpath_price = "//span[@class='Text_text__DSnue Text_variant_alert__7jMF3 Text_size_b2__y3Q2E Text_weight_bold__m4BAY']"
		String gettext_price = WebUI.getText(new TestObject().addProperty("xpath", ConditionType.EQUALS, xpath_price))
		String priceText = gettext_price.split(" ")[1]
		int price = Integer.parseInt(priceText.replaceAll("[^\\d]", ""))
		println("price = $price")

		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Lihat Detail'))

		WebUI.click(findTestObject('Kereta API/btn_Lihat Detail (Rute)'))

		String xpath_route = "//header[contains(@class, 'Modal_modal_header')]/h2"
		String gettext_route = WebUI.getText(new TestObject().addProperty("xpath", ConditionType.EQUALS, xpath_route))
		GlobalVariable.originCity = gettext_route.split(" ")[0]
		GlobalVariable.destinationCity = gettext_route.split(" ")[2]

		TestObject departTime_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//p[contains(@class, 'TripRoute_time_origin')]")
		GlobalVariable.departTime = WebUI.getText(departTime_object)

		TestObject arrivalTime_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//p[contains(@class, 'TripRoute_time_destination')]")
		GlobalVariable.arrivalTime = WebUI.getText(arrivalTime_object)

		TestObject trainName_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[contains(@class, 'TrainInformation_wagon')]/span)[1]")
		GlobalVariable.trainName = WebUI.getText(trainName_object)

		TestObject trainClass_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[contains(@class, 'TrainInformation_wagon')]/span)[2]")
		GlobalVariable.trainClass = WebUI.getText(trainClass_object)

		TestObject originStation_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[contains(@class, 'TripRoute_name')])[1]/p")
		GlobalVariable.originStation = WebUI.getText(originStation_object)

		TestObject destinationStation_object = new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[contains(@class, 'TripRoute_name')])[2]/p")
		GlobalVariable.destinationStation = WebUI.getText(destinationStation_object)

		WebUI.click(findTestObject('Kereta API/btn_Lihat Detail (Harga)'))

		String getText_total = WebUI.getText(new TestObject().addProperty("xpath", ConditionType.EQUALS, "//span[@data-testid='product_details_total_price_footer']"))
		String totalText = getText_total.split(" ")[1]
		GlobalVariable.totalPrice = Integer.parseInt(totalText.replaceAll("[^\\d]", ""))
		println("totalPrice = $GlobalVariable.totalPrice")

		assert GlobalVariable.totalPrice == ( price * GlobalVariable.totalAdult)

		WebUI.click(findTestObject('Object Repository/Kereta API/btn_Close (X)'))
	}

}