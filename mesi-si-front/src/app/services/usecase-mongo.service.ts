import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UseCaseMongo } from '../models/usecaseMongo';

@Injectable({
  providedIn: 'root'
})
export class UseCaseMongoService {

  private useCaseMongoURL:string = 'http://localhost:8085/usecase'


  constructor(private httpClient:HttpClient) { }


  createUseCase(useCaseMongo:UseCaseMongo):Observable<UseCaseMongo>{
    console.log("use kaze kreacija");
    console.log("use case za slanje: "+useCaseMongo);

    var headers = new HttpHeaders({
      'Content-Type': 'application/json'
  });

    return this.httpClient.post<UseCaseMongo>(this.useCaseMongoURL+"/save", useCaseMongo,{headers});
  }

  saveGeneratedFile(useCaseMongo: UseCaseMongo):Observable<UseCaseMongo>{
    var headers = new HttpHeaders({
      'Content-Type': 'application/json'
  });
    return this.httpClient.post<UseCaseMongo>(this.useCaseMongoURL+"/saveGeneratedFile", useCaseMongo,{headers});
  }
  getUseCase(useCaseDocumentMongoId:string):Observable<UseCaseMongo>{
    return this.httpClient.get<UseCaseMongo>(this.useCaseMongoURL+"/get/"+useCaseDocumentMongoId);
  }

  getUseCases():Observable<UseCaseMongo[]>{
    return this.httpClient.get<UseCaseMongo[]>(this.useCaseMongoURL+"/all");
  }

  deleteUseCase(useCaseDocumentMongoId:number):Observable<UseCaseMongo>{
    return this.httpClient.delete<UseCaseMongo>(this.useCaseMongoURL+"/delete"+useCaseDocumentMongoId)
  }

}
