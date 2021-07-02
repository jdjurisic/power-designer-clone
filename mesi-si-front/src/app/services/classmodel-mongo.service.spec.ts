import { TestBed } from '@angular/core/testing';

import { ClassModelMongoService } from './classmodel-mongo.service';

describe('ClassmodelMongoService', () => {
  let service: ClassModelMongoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClassModelMongoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
