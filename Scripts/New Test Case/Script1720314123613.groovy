import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

departureCity = 'jakarta'
arrivalCity = 'bandung'
departDateString = '20240718'

WebUI.openBrowser(GlobalVariable.url)
WebUI.maximizeWindow()
TestObject logo = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//img[@alt='Tiket.com logo']")
WebUI.verifyElementVisible(logo)
WebUI.verifyTextPresent("Hai kamu, mau ke mana?", false)

WebUI.click(findTestObject('Object Repository/Kereta API/menu_Kereta API'))
WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/input_Departure City'), 0)
WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/input_Arrival City'), 0)
WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/input_Departure City'), 0)
WebUI.verifyElementPresent(findTestObject('Object Repository/Kereta API/btn_Cari Kereta API'), 0)

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


WebUI.setText(findTestObject('Object Repository/Kereta API/input_Arrival City'), arrivalCity)
WebUI.verifyElementAttributeValue(findTestObject('Object Repository/Kereta API/input_Arrival City'), 'value', arrivalCity, 0)
WebUI.click(new TestObject().addProperty("xpath", ConditionType.EQUALS, "(//div[@class='station-city'])[1]"))


DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", new java.util.Locale("id"))

LocalDate dateDepart = LocalDate.parse(departDateString, inputFormatter)
String formattedDate = dateDepart.format(outputFormatter)
println('Formatted departure date: ' + formattedDate)

if (dateDepart.isBefore(LocalDate.now())) {
	throw new Exception("Tanggal keberangkatan tidak valid, tidak dapat melakukan pemesanan.")
}

new_xpath = "//td[contains(@aria-label,'$formattedDate')]"
println("new_xpath = $new_xpath")

dynamicObject = new TestObject('dynamicObject').addProperty('xpath', ConditionType.EQUALS, new_xpath)

boolean departDatePresent = WebUI.verifyElementVisible(dynamicObject, FailureHandling.OPTIONAL)

while (!(departDatePresent)) {
    TestObject nextMonth = new TestObject().addProperty('xpath', ConditionType.EQUALS, '//span[@class=\'widget-date-prev-next-logo\']/i[@class=\'tix tix-chevron-right\']')
    WebUI.click(nextMonth)
    departDatePresent = WebUI.verifyElementVisible(dynamicObject, FailureHandling.OPTIONAL)
}

WebUI.click(dynamicObject)

totalAdult = 2
totalInfant = 2

totalPassenger = totalAdult + totalInfant
if (totalPassenger > 8) {
	throw new Exception("You can only book max total 8 passenger.");
}

if (totalAdult < 1) {
	throw new Exception("Minimal book 1 adult.");
}

if (totalAdult > 4 || totalAdult > 4) {
	throw new Exception("Maximal 4 passenger.");
}

WebUI.click(findTestObject('Object Repository/Kereta API/input_Penumpang'))

for (int i = 1; i < (totalAdult); i++) {
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

