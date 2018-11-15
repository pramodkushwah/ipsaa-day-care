import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentFeeInfoComponent } from './student-fee-info.component';

describe('StudentFeeInfoComponent', () => {
  let component: StudentFeeInfoComponent;
  let fixture: ComponentFixture<StudentFeeInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentFeeInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentFeeInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
