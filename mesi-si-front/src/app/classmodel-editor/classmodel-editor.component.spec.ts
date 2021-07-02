import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassmodelEditorComponent } from './classmodel-editor.component';

describe('ClassmodelEditorComponent', () => {
  let component: ClassmodelEditorComponent;
  let fixture: ComponentFixture<ClassmodelEditorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClassmodelEditorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassmodelEditorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
