import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { ProductService } from '../service/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit{ 

dtOptions: any = {};
formSearch: FormGroup;

@ViewChild(DataTableDirective)
dtElement: DataTableDirective;
dtTrigger: Subject<any> = new Subject();

  constructor(private productService: ProductService, private formBuild: FormBuilder){}

  ngOnInit(): void {
    this.formSearch = new FormGroup({
      'nama' : new FormControl()
    });

    const that = this;
    this.dtOptions = {
      ajax: (dataTablesParameters: any, callback)=>{
        const parameter = new Map<string, any>();
        parameter.set('nama', this.formSearch.controls.nama.value);
        that.productService.getListProduct(parameter, dataTablesParameters).subscribe(resp =>{
          callback({
            recordsTotal: resp.recordsTotal,
            recordsFiltered: resp.recordsFiltered,
            data: resp.data,
            draw: resp.draw
          });
        });
      },
      serverSide: true,
      processing: true,
      filter:false,
      columns: [{
        title: 'ID',
        data: 'productId',
        orderable: false
      }, {
        title: 'Product Name',
        data: 'productName'
      },{
        title: 'Category Name',
        data:'categoryName'
        
      },{
        title: 'Action',
        orderable: false, //supaya tidak bisa di sort
        render: function(data, type, row){
          return  `<a href="editproduct/${row.productId}" type="button" class="btn btn-warning btn-xs edit" data-element-id="${row.productId}">
          <i class="glyphicon glyphicon-edit"></i>Edit</a>&nbsp&nbsp
          <a href="editproduct/${row.productId}" type="button" class="btn btn-danger btn-xs remove" data-element-id="${row.productId}">
          <i class="glyphicon glyphicon-remove"></i>Delete</a>&nbsp&nbsp
          `;
        }
      }],
      'rowCallback': function (row, data, dataIndex) {
        const idx = ((this.api().page()) * this.api().page.len()) + dataIndex + 1;
        $('td:eq(0)', row).html('<b>' + idx + '</b>');
      }
    };
  }

  search(): void{
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api)=>{
      dtInstance.draw();
    });
  }
}

