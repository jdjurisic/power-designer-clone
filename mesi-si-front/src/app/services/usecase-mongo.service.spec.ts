import { TestBed } from '@angular/core/testing';

import { UsecaseMongoService } from './usecase-mongo.service';

describe('UsecaseMongoService', () => {
  let service: UsecaseMongoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsecaseMongoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
