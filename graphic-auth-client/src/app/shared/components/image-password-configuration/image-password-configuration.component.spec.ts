import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImagePasswordConfigurationComponent } from './image-password-configuration.component';

describe('ImagePasswordConfigurationComponent', () => {
  let component: ImagePasswordConfigurationComponent;
  let fixture: ComponentFixture<ImagePasswordConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ImagePasswordConfigurationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImagePasswordConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
