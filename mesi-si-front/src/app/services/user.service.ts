import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Team } from '../models/team';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  //private userURL:string = 'http://localhost:8082/user'
  private userURL:string = 'http://localhost:8081/broker/user'
  //private teamURL:string = 'http://localhost:8082/team'
  private teamURL:string = 'http://localhost:8081/broker/team'
  //private projectURL:string = 'http://localhost:8082/project'
  private projectURL:string = 'http://localhost:8081/broker/project'

  constructor(private httpClient:HttpClient) { }

  getUsers():Observable<User[]>{
    return this.httpClient.get<User[]>(this.userURL+"/all");


  }
  addUserToTeam(idU:number, idT:number){
    return this.httpClient.post(this.teamURL+"/addUserToTeam/"+idU+"/"+idT, null);
  }
  addProjectToTeam(projectId:number, teamId:number){
    return this.httpClient.post(this.teamURL+"/addProjectToTeam/"+projectId+"/"+teamId, null);
  }

  deleteUser(id:number) {
    return this.httpClient.delete(this.userURL+"/delete/"+id);
  }

  getTeams():Observable<Team[]>{
    return this.httpClient.get<Team[]>(this.teamURL+"/all");
  }

  getTypes():Observable<any[]> {
    return this.httpClient.get<any[]>(this.userURL+"/types");
  }
  createUser(user:User):Observable<User> {
    return this.httpClient.post<User>(this.userURL+"/save", user);
  }

  updateUser(user:User,id:number):Observable<User>{
    return this.httpClient.put<User>(this.userURL+"/update/"+id, user);
  }

  getUser(id:number):Observable<User>{
    return this.httpClient.get<User>(this.userURL+"/get/"+id);
  }

  getUserByUsername(id:string):Observable<User>{
    return this.httpClient.get<User>(this.userURL+"/getByUsername/"+id);
  }

  
}
