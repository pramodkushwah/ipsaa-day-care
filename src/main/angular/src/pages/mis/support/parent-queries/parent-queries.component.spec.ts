import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ParentQueriesComponent } from './parent-queries.component';

describe('ParentQueriesComponent', () => {
  let component: ParentQueriesComponent;
  let fixture: ComponentFixture<ParentQueriesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ParentQueriesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParentQueriesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
