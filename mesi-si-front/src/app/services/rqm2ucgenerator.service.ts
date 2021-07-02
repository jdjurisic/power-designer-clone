import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClassModelMongo } from '../models/classModelMongo';
import { RqmDocumentMongo } from '../models/rqmDocumentMongo';
import { UseCaseMongo } from '../models/usecaseMongo';

@Injectable({
  providedIn: 'root'
})
export class Rqm2ucgeneratorService {

  private usecaseGeneratorURL:string = 'http://localhost:8087/rqm2uc/'


  constructor(private httpClient:HttpClient) { }

  
  generateUseCaseFromRQM(rqmDocumentMongo:RqmDocumentMongo):Observable<UseCaseMongo>{
    return this.httpClient.post<UseCaseMongo>(this.usecaseGeneratorURL+"transform/", rqmDocumentMongo);
  }
}
