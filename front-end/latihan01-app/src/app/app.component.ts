import { Component } from '@angular/core';

// anotasi untuk memberi tahu html dan css yang dipakai
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
// ini struktur asli typescript
export class AppComponent {
  title = 'Tes Title';
  nama = 'ini nama';
  halo = 'Angular';
}

// ini akan menjadi template untuk halaman lain
