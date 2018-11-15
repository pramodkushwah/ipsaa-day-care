import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffLeavesComponent } from './staff-leaves.component';

describe('StaffLeavesComponent', () => {
  let component: StaffLeavesComponent;
  let fixture: ComponentFixture<StaffLeavesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffLeavesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffLeavesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
