import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsecaseEditorComponent } from './usecase-editor.component';

describe('UsecaseEditorComponent', () => {
  let component: UsecaseEditorComponent;
  let fixture: ComponentFixture<UsecaseEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UsecaseEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UsecaseEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
