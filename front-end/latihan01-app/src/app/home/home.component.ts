import { Component, OnInit } from '@angular/core';
import { KategoriService } from '../service/kategori.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  namaHome: string; //ini deklarasinya
  //disini juga support private, public dll
  constructor(private kategoriService: KategoriService) { 
    //pada saat kelasnya dijalankan, belum tentu ada di browser (pada saat halaman di load)
  }

  ngOnInit() {
    //akan dijalankan pada saat halaman di load, diinisialisasi
    // this.namaHome = ' ini di on init';
    this.namaHome = this.kategoriService.getProjectName();
    const judul= 'ini juga ada';

  }

  simpan(nama:string): void {
    const judulHome = 'ini judul';

    this.namaHome = nama; //untuk menggunakan variabel pd level kelas pakai this, kalau tidak error
    //kalau didalam method gausah pake this
  }

}
