import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../../providers/user/user';
import { AlertService } from '../../providers/alert/alert.service';
import { StorageService } from '../../providers/localstorage/storage';
declare const $: any;
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user = {
    email: '',
    password: ''
  };
  token: string;
  password1: string;
  password2: string;
  forgetForm: any;
  forgetCLickEmailError: boolean;
  logging = false; // to diable the login btn while request is in progress
  forgetEmail: string;
  pwdMisMatch = false;

  constructor(
    private userService: User,
    private router: Router,
    private alertService: AlertService,
    private storage: StorageService
  ) { }

  ngOnInit() { }

  verifyUser() {
    this.logging = true;
    this.userService.login(this.user).subscribe(
      (response: any) => {
        this.logging = false;
        this.onSuccess();
      },
      (error: any) => {
        this.logging = false;
      }
    );
  }

  onSuccess() {
    const user: any = this.userService.getUser();

    // const pvlges = this.storage.getData('ngStorage-token');
    // if (typeof pvlges !== 'undefined') {
    //   this.router.navigate(['mis']);

    // } else {
    //   this.router.navigate(['pp']);

    // }

    if (user.domain === '/pp/') {
      this.router.navigate(['pp']);
    } else {
      this.router.navigate(['mis']);
    }
  }

  onError(error: any) {
    this.alertService.errorAlert(error.message);
  }


  userEmailChange() {
    this.forgetCLickEmailError = false;
    this.forgetEmail = this.user.email;
  }
  onForgotPswd() {
    // TODO


    if (this.forgetEmail) {

      this.userService.getTokenForForgetPassword({ email: this.forgetEmail })
        .subscribe((res: any) => {
          $('#forgetPassword').modal('show');
        });


    } else {
      this.forgetCLickEmailError = true;
    }
    // console.log('forgetpassword');

  }



  newPassword() {

    if (this.password1 === this.password2) {
      console.log(this.token + '  ' + this.password1);

      this.userService.getCreateNewPassword({ password: this.password1, token: this.token.toString() })
        .subscribe((res: any) => {
          this.alertService.successAlert('Password Change Successfuly \n Login With New Password');
          $('#forgetPassword').modal('hide');
        }, (err) => {
          this.pwdMisMatch = false;

        });
    } else {
      this.pwdMisMatch = true;
    }

  }
}
