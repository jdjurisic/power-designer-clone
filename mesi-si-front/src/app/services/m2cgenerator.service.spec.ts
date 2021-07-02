import { TestBed } from '@angular/core/testing';

import { M2cgeneratorService } from './m2cgenerator.service';

describe('M2cgeneratorService', () => {
  let service: M2cgeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(M2cgeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
