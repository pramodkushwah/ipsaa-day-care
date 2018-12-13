import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { User } from '../../providers/user/user';
import { AlertService } from '../../providers/alert/alert.service';
import { StorageService } from '../../providers/localstorage/storage';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  user = {
    email: '',
    password: 'riddhi0211'
  };
  logging = false; // to diable the login btn while request is in progress

  constructor(
    private userService: User,
    private router: Router,
    private alertService: AlertService,
    private storage: StorageService
  ) {}

  ngOnInit() {}

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

  onForgotPswd() {
    // TODO
  }
}
