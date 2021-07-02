import { TestBed } from '@angular/core/testing';

import { TeamserviceService } from './team.service';

describe('TeamserviceService', () => {
  let service: TeamserviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TeamserviceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
