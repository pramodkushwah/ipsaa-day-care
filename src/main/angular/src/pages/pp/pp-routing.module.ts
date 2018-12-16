import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PpComponent } from './pp.component';
import { FeeComponent } from './fee/fee.component';
import { AttendanceComponent } from './attendance/attendance.component';
import { ProfileComponent } from './profile/profile.component';
import { SupportComponent } from './support/support.component';
import { ActivitiesComponent } from './activities/activities.component';
import { GalleryComponent } from './gallery/gallery.component';
import { SharingSheetComponent } from '../mis/center/sharing-sheet/sharing-sheet.component';
import { FoodMenuComponent } from '../mis/center/food-menu/food-menu.component';

const routes: Routes = [
  {
    path: '',
    component: PpComponent,
    children: [
      {
        path: '',
        redirectTo: 'profile',
        pathMatch: 'full'
      },
      {
        path: 'profile',
        component: ProfileComponent
      },
      {
        path: 'fee',
        component: FeeComponent
      },
      {
        path: 'attendance',
        component: AttendanceComponent
      },
      {
        path: 'activities',
        component: ActivitiesComponent
      },
      {
        path: 'gallery',
        component: GalleryComponent
      },
      {
        path: 'sharingSheet',
        component: SharingSheetComponent
      },
      {
        path: 'foodmenu',
        component: FoodMenuComponent
      },
      {
        path: 'support',
        component: SupportComponent
      },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PpRoutingModule { }
