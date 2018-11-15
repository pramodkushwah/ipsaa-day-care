import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffMessageComponent } from './staff-message.component';

describe('StaffMessageComponent', () => {
  let component: StaffMessageComponent;
  let fixture: ComponentFixture<StaffMessageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaffMessageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaffMessageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
