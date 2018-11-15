import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { StorageService } from '../localstorage/storage';

@Injectable()
export class AuthGaurd implements CanActivate {
  constructor(private router: Router, private storage: StorageService) {}
  canActivate() {
    if (this.storage.getData('ngStorage-token')) {
      return true;
    }
    this.router.navigate(['/login']);
    return false;
  }
}
