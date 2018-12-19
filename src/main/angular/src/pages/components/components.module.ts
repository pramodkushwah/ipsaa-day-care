import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { FooterComponent } from './footer/footer.component';
import { NavbarComponent } from './navbar/navbar.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { MenuService } from '../../providers/initial/menu.service';
import { TableComponent } from './table/table.component';

@NgModule({
  imports: [CommonModule, RouterModule],
  declarations: [FooterComponent, NavbarComponent, SidebarComponent, TableComponent],
  exports: [FooterComponent, NavbarComponent, SidebarComponent, TableComponent],
  providers: [MenuService]
})
export class ComponentsModule {}
