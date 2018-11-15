import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateFeeReceiptComponent } from './generate-fee-receipt.component';

describe('GenerateFeeReceiptComponent', () => {
  let component: GenerateFeeReceiptComponent;
  let fixture: ComponentFixture<GenerateFeeReceiptComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenerateFeeReceiptComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateFeeReceiptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
