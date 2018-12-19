import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { IpsaaclubComponent } from './ipsaaclub.component';

describe('IpsaaclubComponent', () => {
  let component: IpsaaclubComponent;
  let fixture: ComponentFixture<IpsaaclubComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ IpsaaclubComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(IpsaaclubComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
