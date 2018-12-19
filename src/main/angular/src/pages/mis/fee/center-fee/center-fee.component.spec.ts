import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CenterFeeComponent } from './center-fee.component';

describe('CenterFeeComponent', () => {
  let component: CenterFeeComponent;
  let fixture: ComponentFixture<CenterFeeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CenterFeeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CenterFeeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
