import { TestBed } from '@angular/core/testing';

import { Rqm2ucgeneratorService } from './rqm2ucgenerator.service';

describe('Rqm2ucgeneratorService', () => {
  let service: Rqm2ucgeneratorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Rqm2ucgeneratorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
