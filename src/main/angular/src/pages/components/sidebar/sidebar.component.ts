import { Component, OnInit} from '@angular/core';
import { MenuService } from '../../../providers/initial/menu.service';
import { StorageService } from '../../../providers/localstorage/storage';
import { Router } from '@angular/router';

declare const $: any;
declare let document;
declare interface RouteInfo {
  path: string;
  title: string;
  icon: string;
  class: string;
}

export const ROUTES: RouteInfo[] = [
  { path: 'dashboard', title: 'Dashboard', icon: 'dashboard', class: '' }
];

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
  menuItems: any[];
  loaded: any;
  menu: any = {};
  self: any = {};
  profileImageURI: string;

  constructor(private menuService: MenuService,
    private storage: StorageService,
    private router: Router) {
    menuService.getMenus().subscribe(
      (response: any) => {
        response.items.sort((a: any , b: any) => {
          a.submenu.sort((ai: any , bi: any) => {
            return ai.seq - bi.seq ;
          });
          return a.seq - b.seq ;
        });
        this.menu = response;
      });
    menuService.getUserProfile().subscribe(
      (response: any) => {
        this.self = response;
        if (this.self.profileImage) {
          this.profileImageURI =
            'http://ipsaaprod.s3-website.ap-south-1.amazonaws.com/' +
            this.self.profileImage;
        } else {
          this.profileImageURI = '/assets/img/faces/default_profile_pic.png';
        }
      });
  }

  ngOnInit() {
    this.menuItems = ROUTES.filter(menuItem => menuItem);
  }
  isMobileMenu() {
    if ($(window).width() > 991) {
      return false;
    }
    return true;
  }

  onLogout() {
    this.storage.clearData();
    this.router.navigate(['/login']);

    }
}
