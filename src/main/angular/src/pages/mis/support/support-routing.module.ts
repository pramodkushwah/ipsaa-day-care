import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ParentQueriesComponent } from './parent-queries/parent-queries.component';

const supportRoutes: Routes = [
  {
    path: 'support',
    component: ParentQueriesComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(supportRoutes)],
  exports: [RouterModule],
})
export class SupportRoutingModule { }
