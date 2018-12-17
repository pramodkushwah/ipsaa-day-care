import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PpRoutingModule } from './pp-routing.module';
import { PpComponent } from './pp.component';
import { AttendanceComponent } from './attendance/attendance.component';
import { FeeComponent } from './fee/fee.component';
import { ProfileComponent } from './profile/profile.component';
import { SupportComponent } from './support/support.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { GalleryComponent } from './gallery/gallery.component';
import { ActivitiesComponent } from './activities/activities.component';
import { FoodMenuComponent } from './food-menu/food-menu.component';
import { SharingSheetComponent } from './sharing-sheet/sharing-sheet.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PpRoutingModule
  ],
  declarations: [PpComponent, AttendanceComponent, FeeComponent,
     ProfileComponent, SupportComponent, GalleryComponent, ActivitiesComponent,
     SharingSheetComponent,  FoodMenuComponent  ]
})
export class PpModule { }
