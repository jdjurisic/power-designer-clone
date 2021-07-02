import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router,ActivatedRoute } from '@angular/router';
import { LoginRequest } from 'src/app/models/loginrequest';
import { LoginService } from 'src/app/services/login-service';
import { throwError } from 'rxjs';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;
  loginRequestPayload: LoginRequest;
  registerSuccessMessage!: string;
  isError!: boolean;

  constructor(private authService: LoginService, private activatedRoute: ActivatedRoute,
    private router: Router) {
    this.loginRequestPayload = {
      username: '',
      password: ''
    };

    this.loginForm = new FormGroup({
      username: new FormControl('', Validators.required),
      password: new FormControl('', Validators.required)
    });
  }

  ngOnInit(): void {
    this.authService.logout();
  }

  login() {
    this.loginRequestPayload.username = this.loginForm.get('username')!.value ;
    this.loginRequestPayload.password = this.loginForm.get('password')!.value;
      this.authService.login(this.loginRequestPayload).subscribe(data => {
      if(!this.authService.isLoggedIn){
        //this.router.navigate(['/login']);
        console.log(localStorage.getItem("jwt"));
        this.authService.logout();
        this.router.navigate(['/login']);

      }
      else {
        this.isError = false;
       this.router.navigate(['/main']);
      }


    }, error => {
      this.isError = true;
      throwError(error);
    });
  }

}
