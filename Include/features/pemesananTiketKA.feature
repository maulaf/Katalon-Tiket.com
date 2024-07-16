@bookingTicketKA
Feature: Pemesanan Tiket Kereta API

  @cariKereta
  Scenario Outline: Mencari Kereta API
    Given pengguna berada di halaman utama aplikasi tiket.com
    When pengguna memilih menu Kereta Api
    And pengguna memasukkan kota asal <departureCity>
    And pengguna memasukkan kota tujuan <arrivalCity>
    And pengguna memilih tanggal keberangkatan <departDate>
    And pengguna memilih penumpang : <totalAdult> dewasa, <totalInfant> bayi
    And pengguna menekan tombol Cari
    Then sistem menampilkan daftar kereta api yang tersedia dari <departureCity> ke <arrivalCity> pada tanggal <departDate>

    Examples: 
      | departureCity  | arrivalCity | departDate  | totalAdult | totalInfant |
      | jakarta				 |   bandung	 | 19-07-2024	 |		1				|			1 			|
      
   
   @filterKereta
  	Scenario Outline: Memfilter tipe kelas Kereta API
  	Given pengguna berada di halaman hasil pencarian kereta api
  	When pengguna memilih kelas <classKA> dari opsi filter
  	And pengguna menekan tombol simpan
  	Then sistem menampilkan halaman detail kereta api kelas <classKA> yang dipilih
  	
  	 Examples: 
      | classKA  | 
      | EKO	 		 |
      
      
    @inputDataPenumpang
    Scenario: Mengisi Data Penumpang Saat Memesan Tiket
    Given pengguna memilih kereta dan berada di halaman pengisian data penumpang
    When pengguna mengisi detail penumpang
    And pengguna menekan tombol Lanjutkan
    Then sistem menampilkan halaman Kursi & Perlindungan
  
    