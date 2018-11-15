import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentApprovalsComponent } from './student-approvals.component';

describe('StudentApprovalsComponent', () => {
  let component: StudentApprovalsComponent;
  let fixture: ComponentFixture<StudentApprovalsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StudentApprovalsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StudentApprovalsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
