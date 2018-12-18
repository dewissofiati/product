import { OnInit, Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Kategori } from '../model/kategori.model';
import { KategoriService } from '../service/kategori.service';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


@Component({
    selector: 'app-addkategori',
    templateUrl: './addkategori.component.html'
})

export class AddkategoriComponent implements OnInit {

    tambahKategori: FormGroup;
    isEdit: boolean;
    isDelete: boolean;
    selectedFiles: FileList;
    // di deklarasikannya bisa didalam konstruktor kayak si builer atau di luar kayak si form group
    constructor(private rute: Router, private route: ActivatedRoute,
        private formBuilder: FormBuilder, private kategoriService: KategoriService,
        private toastr: ToastrService) {
        this.tambahKategori;
        //router digunakan untuk ambil id nya
    }

    ngOnInit(): void {

        this.tambahKategori = this.formBuilder.group({
            'categoryId': ['', [Validators.required, Validators.minLength(1)]],
            'categoryName': ['', [Validators.required, Validators.minLength(5)]],
            'description': ['', []],
            'imgLocation':['',[]]
        });

        //untuk mengambil id nya
        this.route.params.subscribe(hasilrute => {
            // const id = hasilrute.id; //id ini dari app routing
            if (hasilrute.id) {
                this.isEdit = true; //isEdit digunakan untuk dihalaman html nya biar pake halaman html yang sama
                this.kategoriService.getKategoribyId(hasilrute.id).subscribe(data => {
                    this.applyFormValues(this.tambahKategori, data); //nge apply ke value yang ada dibelakang
                });
            }
        });

        // this.route.params.subscribe(hasilrute =>{
        //     if(hasilrute.id){
        //         this.isDelete = true;
        //         this.dataService.getKategoribyId(hasilrute.id).subscribe(data =>{
        //             this.applyFormValues(this.tambahKategori,data);
        //         })
        //     }
        // })

    }

    simpanData(): void {
        let kategori = new Kategori();
        kategori.categoryId = this.tambahKategori.controls.categoryId.value;
        kategori.categoryName = this.tambahKategori.controls.categoryName.value;
        kategori.description = this.tambahKategori.controls.description.value;
        kategori.imgLocation = this.tambahKategori.controls.imgLocation.value;

        //subscribe ini bawaan dari observerable
        //solving untuk datanya masuk ke database
        // this.dataService.getSimpanKategori(kategori, this.isEdit, this.isDelete,kategori.categoryId).subscribe(
        //     res => {
        //         console.log(res);
        //         this.toastr.success(res.pesan, 'Info');
        //     },
        //     err => {
        //         console.log(err);
        //         this.toastr.error(err.toLocaleString(),'Error');
        //     },
        //     () => {

        //     }
        // );

        this.kategoriService.getSimpanKategori(kategori, this.isEdit).subscribe(
            res=>{
                this.upload(kategori.categoryId);
                this.toastr.success('Simpan Berhasil', 'Info');
                this.rute.navigateByUrl('/welcome');

            }
        )
        

    }

    selectFile(event: any){
        this.selectedFiles = event.target.files;
    }

    upload(idkey){
        if(this.selectedFiles){
            this.kategoriService.pushFileToStorage(this.selectedFiles[0], idkey).subscribe(res=>{
                console.log('Done');

            },
            err=>{
                console.log(err);
                this.toastr.error(err, 'Error')
            },
            ()=>{
                console.log('Upload Complete');
                // this.rute.navigateByUrl('/welcome');
            }
            );
        }
        this.selectedFiles = undefined;
    }

    private applyFormValues(group, formValues) {
        console.log(formValues);
        Object.keys(group.controls).forEach(key => {
            group.get(key).setValue(formValues[key])
        });

    }

}

// <app-addkategori><app-addkategori>