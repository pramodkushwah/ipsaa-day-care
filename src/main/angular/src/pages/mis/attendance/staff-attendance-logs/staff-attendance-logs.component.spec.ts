import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffAttendanceLogsComponent } from './staff-attendance-logs.component';

describe('StaffAttendanceLogsComponent', () => {
  let component: StaffAttendanceLogsComponent;
  let fixture: ComponentFixture<StaffAttendanceLogsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffAttendanceLogsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffAttendanceLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
