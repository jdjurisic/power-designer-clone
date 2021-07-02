import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClassModelMongo } from '../models/classModelMongo';

@Injectable({
  providedIn: 'root'
})
export class M2cgeneratorService {

  
  private classGeneratorURL:string = 'http://localhost:8088/cm2code/'


  constructor(private httpClient:HttpClient) { }


  // generateCodeFromModel(classModelMongo:ClassModelMongo):Observable<ClassModelMongo>{
  //   return this.httpClient.post<ClassModelMongo>(this.classGeneratorURL, classModelMongo)
  // }

  generateCodeFromModel(classModelMongo:ClassModelMongo):Observable<ClassModelMongo>{
    return this.httpClient.post<ClassModelMongo>(this.classGeneratorURL+"/transform/", classModelMongo);
  }
}
