import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { StorageService } from '../localstorage/storage';
import { User } from '../user/user';

@Injectable()
export class LoginGaurd implements CanActivate {
  constructor(private router: Router,
    private userService: User,
    private storage: StorageService) { }
  canActivate() {


    if (this.storage.getData('ngStorage-token')) {
      const user: any = this.userService.getUser();

      if (user.domain === '/pp/') {
        this.router.navigate(['pp']);
        return false;
      } else {
        this.router.navigate(['mis']);
        return false;
      }
    } else {
      return true;
    }

    // if (this.storage.getData('ngStorage-token')) {
    //   if (this.storage.getData('ngStorage-privileges').length > 1) {
    //     this.router.navigate(['/mis']);
    //     return false;
    //   } else {
    //     this.router.navigate(['/pp']);
    //     return false;
    //   }
    // } else {
    //   return true;
    // }
  }
}
