import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClassinspectorComponent } from './classinspector.component';

describe('ClassinspectorComponent', () => {
  let component: ClassinspectorComponent;
  let fixture: ComponentFixture<ClassinspectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClassinspectorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClassinspectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
