import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Team } from '../models/team';

@Injectable({
  providedIn: 'root'
})
export class MainService {

  //private readonly mainURL = "http://localhost:8082/team";
  private readonly mainURL = "http://localhost:8081/broker/team";

  constructor(private httpClient:HttpClient) { }

  
  whichTeam():Observable<Team>{
    return this.httpClient.get<Team>(this.mainURL+"/teamMap")
  }

}
