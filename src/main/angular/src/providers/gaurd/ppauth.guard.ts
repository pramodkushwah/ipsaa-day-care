import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { StorageService } from '../localstorage/storage';
import { User } from '../user/user';

@Injectable({
  providedIn: 'root'
})
export class PpauthGuard implements CanActivate {
  constructor(private router: Router,
    private userService: User,
    private storage: StorageService) { }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    if (this.storage.getData('ngStorage-token')) {
      const user: any = this.userService.getUser();
      if (user.domain === '/pp/') {
        return true;
      }
      this.router.navigate(['/login']);
      return false;
    }
    this.router.navigate(['/login']);
    return false;

  }


}
