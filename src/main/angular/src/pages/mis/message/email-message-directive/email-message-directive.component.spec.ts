import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailMessageDirectiveComponent } from './email-message-directive.component';

describe('EmailMessageDirectiveComponent', () => {
  let component: EmailMessageDirectiveComponent;
  let fixture: ComponentFixture<EmailMessageDirectiveComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmailMessageDirectiveComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailMessageDirectiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
