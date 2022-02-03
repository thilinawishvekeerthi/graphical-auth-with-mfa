import { TestBed } from '@angular/core/testing';

import { GraphicLoginService } from './graphic-login.service';

describe('GraphicLoginService', () => {
  let service: GraphicLoginService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GraphicLoginService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
