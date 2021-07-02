import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../models/project';
import { RqmDocumentMongo } from '../models/rqmDocumentMongo';

@Injectable({
  providedIn: 'root'
})
export class RqmMongoService {


  //private rqmMongoURL:string = 'http://localhost:8083/rqm'
  private rqmMongoURL:string = 'http://localhost:8081/broker/rqm'


  constructor(private httpClient:HttpClient) { }


  createRQM(rqmMongo:RqmDocumentMongo):Observable<RqmDocumentMongo>{
    return this.httpClient.post<RqmDocumentMongo>(this.rqmMongoURL+"/save", rqmMongo)
  }

  saveGenerated(rqmMongo:RqmDocumentMongo):Observable<RqmDocumentMongo>{
    return this.httpClient.post<RqmDocumentMongo>(this.rqmMongoURL+"/saveGenerated", rqmMongo)
  }
  getRQM(rqmDocumentMongoId:string):Observable<RqmDocumentMongo>{
    return this.httpClient.get<RqmDocumentMongo>(this.rqmMongoURL+"/get/"+rqmDocumentMongoId);
  }

  getRQMs():Observable<RqmDocumentMongo[]>{
    return this.httpClient.get<RqmDocumentMongo[]>(this.rqmMongoURL+"/all");
  }

  deleteRQM(rqmDocumentMongoId:number):Observable<RqmDocumentMongo>{
    return this.httpClient.delete<RqmDocumentMongo>(this.rqmMongoURL+"/delete"+rqmDocumentMongoId)
  }

}

