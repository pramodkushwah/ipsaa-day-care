import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryReportComponent } from './inquiry-report.component';

describe('InquiryReportComponent', () => {
  let component: InquiryReportComponent;
  let fixture: ComponentFixture<InquiryReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [InquiryReportComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InquiryReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
