import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ParentQueriesComponent } from './parent-queries/parent-queries.component';
import { SupportRoutingModule } from './/support-routing.module';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    SupportRoutingModule
  ],
  declarations: [ParentQueriesComponent]
})
export class SupportModule { }
