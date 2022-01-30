import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GraphicLoginComponent } from './graphic-login.component';

describe('GraphicLoginComponent', () => {
  let component: GraphicLoginComponent;
  let fixture: ComponentFixture<GraphicLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GraphicLoginComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GraphicLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
