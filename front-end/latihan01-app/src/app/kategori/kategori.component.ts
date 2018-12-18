import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { KategoriService } from '../service/kategori.service';
import { FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { DataTableDirective } from 'angular-datatables';
import { Subject } from 'rxjs';
import { NgxSmartModalService } from 'ngx-smart-modal';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-kategori',
  templateUrl: './kategori.component.html',
})
export class KategoriComponent implements OnInit, AfterViewInit {
satu: number;
dua: number;    

dtOptions: any = {};
formCari: FormGroup;

@ViewChild(DataTableDirective)
dtElement: DataTableDirective;
dtTrigger: Subject<any> = new Subject();

  constructor(private kategoriService: KategoriService, private formBuild: FormBuilder,
    private ngxSmartModalService: NgxSmartModalService){}


  ngAfterViewInit(){
    this.ngxSmartModalService.getModal('imageModal').onOpen.subscribe((event:Event)=>{
      console.log("ini lagi dibuka");
    });

    this.ngxSmartModalService.getModal('imageModal').onCloseFinished.subscribe((event:Event)=>{
      this.ngxSmartModalService.resetModalData('imageModal');
    });
  }

  ngOnInit(): void {
    this.formCari = new FormGroup({
      'nama' : new FormControl()
    });

    const that = this;
    this.dtOptions = {
      ajax: (dataTablesParameters: any, callback)=>{
        const parameter = new Map<string, any>();
        parameter.set('nama', this.formCari.controls.nama.value);
        that.kategoriService.getListKategori(parameter, dataTablesParameters).subscribe(resp =>{
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
        data: 'categoryId',
        orderable: false //supaya tidak dapat di order by
      }, {
        title: 'Category Name',
        data: 'categoryName'
      }, {
        title: 'Description',
        data: 'description'
      },{
        title: 'Action',
        orderable: false, //supaya tidak bisa di sort
        render: function(data, type, row){
          return  `<a href="editkategori/${row.categoryId}" type="button" class="btn btn-warning btn-xs edit" data-element-id="${row.categoryId}">
          <i class="glyphicon glyphicon-edit"></i> Edit</a>&nbsp&nbsp
          <a href="deletekategori/${row.categoryId}" type="button" class="btn btn-danger btn-xs remove" data-element-id="${row.categoryId}">
          <i class="glyphicon glyphicon-remove"></i> Delete</a>&nbsp&nbsp
          <button class="btn btn-info btn-sm image" data-element-id="${row.categoryId}">
          <i class="glyphicon glyphicon-zoom-in"></i> View</button>
          `;
        }
      }],
      'rowCallback': function (row, data, dataIndex) {
        const idx = ((this.api().page()) * this.api().page.len()) + dataIndex + 1;
        $('td:eq(0)', row).html('<b>' + idx + '</b>');
      }
    };
    //dom pada angular dihindari untuk dipakai
    //solusi untuk bisa ada action di edit dan delete yang ada di render
    document.querySelector('body').addEventListener('click', (event) => {
      const target = <Element>event.target;
      if (target.tagName.toLowerCase() === 'button' && $(target).hasClass('image')) {
        
        this.ngxSmartModalService.setModalData(environment.baseUrl+'api/image/' + target.getAttribute('data-element-id'),
        'imageModal');
        this.ngxSmartModalService.open('imageModal');
      // if (target.tagName.toLowerCase() === 'button' && $(target).hasClass('remove')) {
      //   console.log('ini delete' + target.getAttribute('data-element-id'));

      }
    });
  }

  cari(): void{
    this.dtElement.dtInstance.then((dtInstance: DataTables.Api)=>{
      dtInstance.draw();
    });
  }

  tambah(satu:number, dua:number):number{
    return satu+dua;
  }

}

