import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { StorageService } from '../localstorage/storage';

@Injectable()
export class LoginGaurd implements CanActivate {
  constructor(private router: Router, private storage: StorageService) {}
  canActivate() {
    if (this.storage.getData('ngStorage-token')) {
      this.router.navigate(['/mis']);
      return false;
    } else {
      return true;
    }
  }
}
