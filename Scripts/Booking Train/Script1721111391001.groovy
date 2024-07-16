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

CucumberKW.runFeatureFileWithTags('Include/features/pemesananTiketKA.feature', '@cariKereta')

CucumberKW.runFeatureFileWithTags('Include/features/pemesananTiketKA.feature', '@inputDataPenumpang')

tiketHabis = "Maaf ya, tiket kereta ini habis terjual"

btn_CariKeretaLain = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//button[.='Lihat Kereta Lain']")

if (WebUI.verifyTextPresent(tiketHabis, false, FailureHandling.OPTIONAL) == true) {
    WebUI.click(btn_CariKeretaLain)
} else {
    WebUI.click(findTestObject('Object Repository/Kereta API/btn_Lanjut Bayar'))
    WebUI.click(findTestObject('Object Repository/Kereta API/btn_Confirm Lanjut Bayar'))
	
	countdown = new TestObject().addProperty("xpath", ConditionType.EQUALS, "//ul[contains(@class, 'Countdown')]")
	WebUI.verifyElementVisible(countdown)
	
	int biayaPemesanan = 3500
	int biayaJasa = 5000
	int asuransi = 8000
	
	println(GlobalVariable.totalPrice)
	
	gettext_finalPrice = WebUI.getText(new TestObject().addProperty("xpath", ConditionType.EQUALS, "//span[contains(@class, 'PriceSummary_final_price')]"))
	def finalPrice = Integer.parseInt(gettext_finalPrice.replaceAll("[^\\d]", ""))
	
	assert finalPrice == GlobalVariable.totalPrice + biayaPemesanan + biayaJasa + asuransi
}