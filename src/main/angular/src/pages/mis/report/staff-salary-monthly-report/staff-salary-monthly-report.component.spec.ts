import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffSalaryMonthlyReportComponent } from './staff-salary-monthly-report.component';

describe('StaffSalaryMonthlyReportComponent', () => {
  let component: StaffSalaryMonthlyReportComponent;
  let fixture: ComponentFixture<StaffSalaryMonthlyReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffSalaryMonthlyReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffSalaryMonthlyReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
