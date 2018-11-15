import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MISComponent } from './mis.component';

describe('MISComponent', () => {
  let component: MISComponent;
  let fixture: ComponentFixture<MISComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MISComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MISComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
