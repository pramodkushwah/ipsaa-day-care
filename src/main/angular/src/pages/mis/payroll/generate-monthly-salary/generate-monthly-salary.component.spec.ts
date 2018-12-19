import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateMonthlySalaryComponent } from './generate-monthly-salary.component';

describe('GenerateMonthlySalaryComponent', () => {
  let component: GenerateMonthlySalaryComponent;
  let fixture: ComponentFixture<GenerateMonthlySalaryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenerateMonthlySalaryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateMonthlySalaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
