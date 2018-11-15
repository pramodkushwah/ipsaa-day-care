import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenerateFeeSlipComponent } from './generate-fee-slip.component';

describe('GenerateFeeSlipComponent', () => {
  let component: GenerateFeeSlipComponent;
  let fixture: ComponentFixture<GenerateFeeSlipComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenerateFeeSlipComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenerateFeeSlipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
