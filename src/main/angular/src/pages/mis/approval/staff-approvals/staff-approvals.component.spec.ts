import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffApprovalsComponent } from './staff-approvals.component';

describe('StaffApprovalsComponent', () => {
  let component: StaffApprovalsComponent;
  let fixture: ComponentFixture<StaffApprovalsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffApprovalsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffApprovalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
