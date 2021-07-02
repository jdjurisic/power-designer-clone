import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Model } from '../models/model';

@Injectable({
  providedIn: 'root'
})
export class ModelService {

 
  //private requirementURL:string = 'http://localhost:8082/requirement'
  private modelURL:string = 'http://localhost:8081/broker/model'


  constructor(private httpClient:HttpClient) { }


  createModel(model:Model):Observable<Model>{
    return this.httpClient.post<Model>(this.modelURL+"/save", model)
  }
  
  updateModel(modelId:number, model:Model):Observable<Model>{
    return this.httpClient.put<Model>(this.modelURL+"/update/"+modelId, model)
  }

  getModel(modelId:number):Observable<Model>{
    return this.httpClient.get<Model>(this.modelURL+"/get/"+modelId);
  }

  getModels():Observable<Model[]>{
    return this.httpClient.get<Model[]>(this.modelURL+"/all");
  }

}