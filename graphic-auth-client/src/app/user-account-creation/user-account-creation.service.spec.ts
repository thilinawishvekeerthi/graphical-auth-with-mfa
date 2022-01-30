import { TestBed } from '@angular/core/testing';

import { UserAccountCreationService } from './user-account-creation.service';

describe('UserAccountCreationService', () => {
  let service: UserAccountCreationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserAccountCreationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
