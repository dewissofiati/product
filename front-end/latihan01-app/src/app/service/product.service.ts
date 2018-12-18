import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpRequest} from '@angular/common/http';
import { Observable } from 'rxjs';
// import { DataTablesResponse } from '../home/datatablesresponse.models';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Product } from '../model/product.model';
//injectable ini supaya bisa di daftarkan di provider di app module
@Injectable()
export class ProductService{
    constructor(private httpClient: HttpClient){

    }
    //menampilkan list product
    getListProduct(parameter: Map<string, any>, datatablesParameters: any): Observable<DataTablesResponse> {
        let params = new HttpParams();
        params = params.append('start', datatablesParameters.start);
        params = params.append('length', datatablesParameters.length);
        params = params.append('draw', datatablesParameters.draw);
        params = params.append('order[0][column]', datatablesParameters.order[0]['column']);
        params = params.append('order[0][dir]', datatablesParameters.order[0]['dir']);
        parameter.forEach(function (value, key) {
            params = params.append(key, value);
        });
        return this.httpClient.post(environment.baseUrl + 'listproduct', parameter, { params: params }
        ).pipe(map(data => <DataTablesResponse>data));
    }
    
    //menyimpan product
    saveProduct(product: Product, isEdit: boolean): Observable<any>{
        console.log(product);
        let url = 'saveproduct';
        if(isEdit=true){
            url = 'updateproduct';
        }//else if(isDelete=true){
        //     url = 'deletekategori/' + id;
        // }
        return this.httpClient.post(environment.baseUrl+url,product);
    }

    //mengambil id product untuk update
    getProductbyId(id: number): Observable<Product>{
        return this.httpClient.get(environment.baseUrl+'getproductbyid/' +  id)
        .pipe(map(data => <Product>data));
        
    } 
}