import { TestBed } from '@angular/core/testing';

import { RqmMongoService } from './rqm-mongo.service';

describe('RqmMongoService', () => {
  let service: RqmMongoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RqmMongoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
