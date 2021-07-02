import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Team } from '../models/team';

@Injectable({
  providedIn: 'root'
})
export class TeamService {
  //private teamURL:string = 'http://localhost:8082/team'
  private teamURL:string = 'http://localhost:8081/broker/team'

  constructor(private httpClient:HttpClient) { }

  createTeam(team:Team):Observable<Team>{
    return this.httpClient.post<Team>(this.teamURL+"/save", team)
  }
  getTeam(teamId:number):Observable<Team>{
    return this.httpClient.get<Team>(this.teamURL+"/get/"+teamId);
  }
  
  getTeams():Observable<Team[]>{
    return this.httpClient.get<Team[]>(this.teamURL+"/all");
  }

  addUserToTeam(idU:number, idT:number){
    return this.httpClient.post(this.teamURL+"/addUserToTeam/"+idU+"/"+idT, null);
  }
  
  addProjectToTeam(projectId:number, teamId:number){
    return this.httpClient.post(this.teamURL+"/addProjectToTeam/"+projectId+"/"+teamId, null);
  }
  
}
