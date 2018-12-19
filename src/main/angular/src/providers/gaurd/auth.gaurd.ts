import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { StorageService } from '../localstorage/storage';
import { User } from '../user/user';

@Injectable()
export class AuthGaurd implements CanActivate {
  constructor(private router: Router,
    private userService: User,
    private storage: StorageService) { }
  canActivate() {
    if (this.storage.getData('ngStorage-token')) {
      const user: any = this.userService.getUser();
      if (user.domain === '/mis/') {
        return true;
      }
      this.router.navigate(['/login']);
      return false;
    }
    this.router.navigate(['/login']);
    return false;

  }

}
