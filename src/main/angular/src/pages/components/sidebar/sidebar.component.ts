import { Component, OnInit } from '@angular/core';
import { MenuService } from '../../../providers/initial/menu.service';
import { User } from '../../../providers/user/user';

declare const $: any;

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

  menu: any = {};
  self: any = {};
  profileImageURI: string;

  constructor(private menuService: MenuService, private userService: User) {
    menuService.getMenus().subscribe(
      (response: any) => {
        this.menu = response;
      },
      err => {
        console.error('ERROR', err);
      }
    );
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
      },
      err => {
        console.error('ERROR', err);
      }
    );
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
    this.userService.logout();
  }
}
