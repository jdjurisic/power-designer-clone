import { collectExternalReferences } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login-service';
import { Router } from '@angular/router';
import { ResponseAlo } from '../models/response';
import { Team } from '../models/team';
import { MainService } from '../services/main.service';

@Component({
  selector: 'app-topcomponent',
  //template: ` <nav class="navbar navbar-light" style="background-color: #e3f2fd;">{{username}}  {{type}}</nav>`,
  templateUrl: './topcomponent.component.html',
  styleUrls: ['./topcomponent.component.css']
})
export class TopcomponentComponent  implements OnInit{

  isLoggedIn$?: Observable<boolean>;
  username!:string;
  type!:string;
  team!:Team;

  
  routes = [
    {linkName: 'Main Page', url: '/main'},
    {linkName: 'User Page', url: '/user'}
  ]
  
  constructor(private service:LoginService, private router: Router,private mainService:MainService) {
    this.isLoggedIn$ = this.service.isLoggedIn1;
    if(this.isLoggedIn$){
      this.username = window.localStorage.getItem("userName")!;
      this.type = window.localStorage.getItem("isAdmin")!;
      //this.whichTeam();

   }
  }
  ngOnInit(){ 
   
  }

 whichTeam(){
   this.mainService.whichTeam().subscribe(data => {
     this.team = data;
   })

 }

  onLogout() {
   this.isLoggedIn$ = this.service.isLoggedIn1;
   this.username ="";
   this.type ="";
   
   this.service.logout()
   this.router.navigate(["/login"]);
 }

}
