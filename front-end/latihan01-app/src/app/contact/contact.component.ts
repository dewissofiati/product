import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormBuilder} from '@angular/forms';


@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
})
export class ContactComponent implements OnInit {
    contactForm: FormGroup;

  constructor(private formBuild:FormBuilder) { 
    //pada saat kelasnya dijalankan, belum tentu ada di browser (pada saat halaman di load)
  }

  ngOnInit() {
    this.contactForm = this.formBuild.group({
      'username'  : new FormControl('', [Validators.required, Validators.minLength(5)]),
      'name'      : new FormControl(''),
      'useremail' : new FormControl('', [Validators.required, Validators.email, Validators.minLength(5)]),
      'usermessage' : new FormControl('', [Validators.required])
    }, {validator: this.checkPasswords});
  }

  onSubmit(){
    console.log(this.contactForm);
  }

  simpan(nama:string): void {
  console.log(this.contactForm.controls.username.value);
  console.log(this.contactForm.controls.name.value);
  console.log(this.contactForm.controls.useremail.value);
  console.log(this.contactForm.controls.usermessage.value);

    }

    checkPasswords(group: FormGroup){
        const username = group.controls.username.value;
        const name = group.controls.name.value;
        return username === name ? true : {notSame:true};
    }
}
