import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClassModelMongo } from '../models/classModelMongo';

@Injectable({
  providedIn: 'root'
})
export class ClassModelMongoService {

  
  //private classModelMongoURL:string = 'http://localhost:8084/class'
  private classModelMongoURL:string = 'http://localhost:8081/broker/class'


  constructor(private httpClient:HttpClient) { }


  createClassModel(classModelMongo:ClassModelMongo):Observable<ClassModelMongo>{
    return this.httpClient.post<ClassModelMongo>(this.classModelMongoURL+"/save", classModelMongo)
  }

  getClassModel(classModelDocumentMongoId:string):Observable<ClassModelMongo>{
    return this.httpClient.get<ClassModelMongo>(this.classModelMongoURL+"/get/"+classModelDocumentMongoId);
  }

  getClassModels():Observable<ClassModelMongo[]>{
    return this.httpClient.get<ClassModelMongo[]>(this.classModelMongoURL+"/all");
  }

  deleteClassModel(classModelDocumentMongoId:number):Observable<ClassModelMongo>{
    return this.httpClient.delete<ClassModelMongo>(this.classModelMongoURL+"/delete"+classModelDocumentMongoId)
  }
}
