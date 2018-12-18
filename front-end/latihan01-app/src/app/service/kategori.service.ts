import {Injectable} from '@angular/core';
import {HttpClient, HttpParams, HttpRequest} from '@angular/common/http';
import { Observable } from 'rxjs';
// import { DataTablesResponse } from '../home/datatablesresponse.models';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Kategori } from '../model/kategori.model';
import { Product } from '../model/product.model';
//injectable ini supaya bisa di daftarkan di provider di app module
@Injectable()
export class KategoriService{
    constructor(private httpClient: HttpClient){

    }
    getProjectName(){
        return 'Toko Minuman Online';
    }

    //menampilkan list kategori
    getListKategori(parameter: Map<string, any>, datatablesParameters: any): Observable<DataTablesResponse> {
        let params = new HttpParams();
        params = params.append('start', datatablesParameters.start);
        params = params.append('length', datatablesParameters.length);
        params = params.append('draw', datatablesParameters.draw);
        params = params.append('order[0][column]', datatablesParameters.order[0]['column']);
        params = params.append('order[0][dir]', datatablesParameters.order[0]['dir']);
        parameter.forEach(function (value, key) {
            params = params.append(key, value);
        });
        return this.httpClient.post(environment.baseUrl + 'listkategori', parameter, { params: params }
        ).pipe(map(data => <DataTablesResponse>data));
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
    
    //menyimpan kategori
    getSimpanKategori(kategori: Kategori, isEdit: boolean): Observable<any>{
        console.log(kategori);
        let url = 'simpankategori';
        if(isEdit=true){
            url = 'updatekategori';
        }//else if(isDelete=true){
        //     url = 'deletekategori/' + id;
        // }
        return this.httpClient.post(environment.baseUrl+url,kategori);
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

    //mengambil id kategori untuk update
    getKategoribyId(id: number): Observable<Kategori>{
        return this.httpClient.get(environment.baseUrl+'getkategoribyid/' +  id)
        .pipe(map(data => <Kategori>data));
        
    }

    //mengambil id product untuk update
    getProductbyId(id: number): Observable<Product>{
        return this.httpClient.get(environment.baseUrl+'getproductbyid/' +  id)
        .pipe(map(data => <Product>data));
        
    }

    //upload image kategori
    pushFileToStorage(file: File, id:string):Observable<Object>{
        const formdata: FormData = new FormData();
        formdata.append('file', file);
        const req = new HttpRequest('POST', environment.baseUrl + 'upload/post/' + id, formdata,{
            reportProgress:true,
            responseType:'text'
        });
        return this.httpClient.request(req);
    }

    

}