import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CenterRoutingModule } from './/center-routing.module';
import { SharingSheetComponent } from './sharing-sheet/sharing-sheet.component';
import { HolidaysComponent } from './holidays/holidays.component';
import { FoodMenuComponent } from './food-menu/food-menu.component';
import { GroupActivityComponent } from './group-activity/group-activity.component';
import { GalleryComponent } from './gallery/gallery.component';
import { HygieneComponent } from './hygiene/hygiene.component';

@NgModule({
  imports: [
    CommonModule,
    CenterRoutingModule
  ],
  declarations: [SharingSheetComponent, HolidaysComponent, FoodMenuComponent, GroupActivityComponent, GalleryComponent, HygieneComponent]
})
export class CenterModule { }
