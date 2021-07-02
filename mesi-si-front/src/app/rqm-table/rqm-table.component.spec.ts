import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RqmTableComponent } from './rqm-table.component';

describe('RqmTableComponent', () => {
  let component: RqmTableComponent;
  let fixture: ComponentFixture<RqmTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RqmTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RqmTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
