import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FeeReceiptInfoComponent } from './fee-receipt-info.component';

describe('FeeReceiptInfoComponent', () => {
  let component: FeeReceiptInfoComponent;
  let fixture: ComponentFixture<FeeReceiptInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FeeReceiptInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FeeReceiptInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
