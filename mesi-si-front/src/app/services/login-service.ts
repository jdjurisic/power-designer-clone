import { HttpClient, HttpParams } from '@angular/common/http'
import { Injectable, OnDestroy } from '@angular/core'
import { LoginRequest } from '../models/loginrequest'
import { map } from 'rxjs/operators'
import {ResponseAlo} from '../models/response'
import { BehaviorSubject, Observable } from 'rxjs';


@Injectable({
    providedIn: 'root'
  })
export class LoginService implements OnDestroy {

  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  get isLoggedIn1() {
    return this.loggedIn.asObservable();
  }
  //private readonly loginUrl = 'http://localhost:8082/auth/login'
  private readonly loginUrl = 'http://localhost:8081/broker/auth/login'
  
  constructor(private http: HttpClient) { }
  
    ngOnDestroy(): void {
      this.logout()
    }

    logout(){
      this.loggedIn.next(false);
      localStorage.removeItem("jwt")
      localStorage.removeItem("isAdmin")
      localStorage.removeItem("userName")
      //return this.http.get('http://localhost:8080/auth/logout');
    }
  
    login(loginrequest:LoginRequest) {
      localStorage.setItem("userName", loginrequest.username)
      let httpParams = new HttpParams()
      httpParams.append("username", loginrequest.username)
      httpParams.append('password', loginrequest.password)
      return this.http.post<ResponseAlo>(this.loginUrl, loginrequest
        ).pipe(map( (response => {
          this.loggedIn.next(true);
          window.localStorage.setItem("jwt", response.jwt);
          window.localStorage.setItem("isAdmin", response.isAdmin);
         
      })))
    }
    isLoggedIn(): boolean {
      return this.getJwtToken() != null;
    }

    getJwtToken() {
      return window.localStorage.retrieve('jwt');
    }
    
  }
  