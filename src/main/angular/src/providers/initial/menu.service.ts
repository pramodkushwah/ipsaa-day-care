import 'rxjs/add/operator/toPromise';
import { Injectable } from '@angular/core';
import { Api } from '../api/api';
import { StorageService } from '../localstorage/storage';

@Injectable()
export class MenuService {
  menu: any;
  constructor(public api: Api, public storage: StorageService) {}

  getMenus() {
    return this.api.get('api/user/menus');
  }

  getUserProfile() {
    return this.api.get('api/user/me/');
  }
}
