import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TopcomponentComponent } from './topcomponent.component';

describe('TopcomponentComponent', () => {
  let component: TopcomponentComponent;
  let fixture: ComponentFixture<TopcomponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TopcomponentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TopcomponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
