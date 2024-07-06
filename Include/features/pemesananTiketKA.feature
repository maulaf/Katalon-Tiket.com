@tag
Feature: Pemesanan Tiket Kereta API

  @cariTiket
  Scenario Outline: Mencari Kereta API
    Given pengguna berada di halaman utama aplikasi tiket.com
    When pengguna memilih menu Kereta Api
    And pengguna memasukkan kota asal <departureCity>
    And pengguna memasukkan kota tujuan <arrivalCity>
    And pengguna memilih tanggal keberangkatan <departDate>
    And pengguna menekan tombol "Cari"
    Then sistem menampilkan daftar kereta api yang tersedia dari <departureCIty> ke <arrivalCity> pada tanggal <departDate>

    Examples: 
      | departureCity  | arrivalCity | departDate  |
      | jakarta				 |   bandung	 | 20241206		 |