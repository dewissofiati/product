import { OnInit, Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Product } from '../model/product.model';
import { Router, ActivatedRoute } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Kategori } from '../model/kategori.model';
import { ProductService } from '../service/product.service';


@Component({
    selector: 'app-addproduct',
    templateUrl: './addproduct.component.html'
})

export class AddproductComponent implements OnInit {

    addProduct: FormGroup;
    isEdit: boolean;
    categoryId: Kategori;

    constructor(private rute: Router, private route: ActivatedRoute,
        private formBuilder: FormBuilder, private productService: ProductService,
        private toastr: ToastrService) {
        this.addProduct;
    }

    ngOnInit(): void {

        this.addProduct = this.formBuilder.group({
            'productId': ['', [Validators.required, Validators.minLength(1)]],
            'productName': ['', [Validators.required, Validators.minLength(5)]]
        });

        this.route.params.subscribe(hasilrute => {
            if (hasilrute.id) {
                this.isEdit = true;
                this.productService.getProductbyId(hasilrute.id).subscribe(data => {
                    this.applyFormValues(this.addProduct, data);
                });
            }
        });
    }

    saveData(): void {
        let product = new Product();
        product.productId = this.addProduct.controls.productId.value;
        product.productName = this.addProduct.controls.productName.value;
        
    }

    eventFromModal(datax){
        console.log(' ====> ' + datax);
        const data = datax.split('|');
        this.categoryId = data[0];
        this.addProduct.controls.categoryName.setValue(data[1]);
    }

    private applyFormValues(group, formValues) {
        console.log(formValues);
        Object.keys(group.controls).forEach(key => {
            group.get(key).setValue(formValues[key])
        });

    }

}