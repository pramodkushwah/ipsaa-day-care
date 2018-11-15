import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SharingSheetComponent } from './sharing-sheet/sharing-sheet.component';
import { HolidaysComponent } from './holidays/holidays.component';
import { FoodMenuComponent } from './food-menu/food-menu.component';
import { HygieneComponent } from './hygiene/hygiene.component';
import { GroupActivityComponent } from './group-activity/group-activity.component';
import { GalleryComponent } from './gallery/gallery.component';

const centerRoutes: Routes = [
  {
    path: 'sharingsheet',
    component: SharingSheetComponent
  },
  {
    path: 'holiday',
    component: HolidaysComponent
  },
  {
    path: 'foodmenu',
    component: FoodMenuComponent
  },
  {
    path: 'hygiene',
    component: HygieneComponent
  },
  {
    path: 'activity',
    component: GroupActivityComponent
  },
  {
    path: 'gallery',
    component: GalleryComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(centerRoutes)],
  exports: [RouterModule]
})
export class CenterRoutingModule { }
