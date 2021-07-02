import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Project } from '../models/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  
  //private projectURL:string = 'http://localhost:8082/project'
  private projectURL:string = 'http://localhost:8081/broker/project'


  constructor(private httpClient:HttpClient) { }


  createProject(project:Project):Observable<Project>{
    return this.httpClient.post<Project>(this.projectURL+"/save", project)
  }

  getProject(projectId:number):Observable<Project>{
    return this.httpClient.get<Project>(this.projectURL+"/get/"+projectId);
  }

  getProjects():Observable<Project[]>{
    return this.httpClient.get<Project[]>(this.projectURL+"/all");
  }
 
  addModelToProject(modelId:number, projectId:number){
    return this.httpClient
      .post(this.projectURL+"/addModelToProject/"+modelId+"/"+projectId, null)
  }

}

