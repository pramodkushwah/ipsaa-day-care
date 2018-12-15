import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IpsaaClubReportComponent } from './ipsaa-club-report.component';

describe('IpsaaClubReportComponent', () => {
  let component: IpsaaClubReportComponent;
  let fixture: ComponentFixture<IpsaaClubReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IpsaaClubReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IpsaaClubReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
