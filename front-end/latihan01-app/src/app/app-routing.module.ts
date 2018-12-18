
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { KategoriComponent } from './kategori/kategori.component';
import { ContactComponent } from './contact/contact.component';
import { ProductComponent } from './product/product.component';
import { AddkategoriComponent } from './kategori/addkategori.component';
import { AddproductComponent } from './product/addproduct.component';
import { KategorimodalComponent } from './kategori/kategorimodal.component';



const routes: Routes = [
  {path: '', pathMatch:'full', redirectTo:'/'},
  {path:'home', component: HomeComponent},
  {path:'kategori', component: KategoriComponent},
  {path:'contact', component:ContactComponent},
  {path:'addkategori', component: AddkategoriComponent},
  {path: 'editkategori/:id', component: AddkategoriComponent, pathMatch:'full'}, //untuk action dari editnya
  {path:'deletekategori', component: AddkategoriComponent, pathMatch:'full'},
  {path:'product', component: ProductComponent},
  {path:'addproduct', component: AddproductComponent},
  {path: 'editproduct/:id', component: AddproductComponent, pathMatch:'full'}
];
//pathMatch full kalau alamatnya ga lengkap gak akan ke path nya
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule {
  static components = [HomeComponent, KategoriComponent, ContactComponent, ProductComponent, AddkategoriComponent, AddproductComponent, KategorimodalComponent];
 }
