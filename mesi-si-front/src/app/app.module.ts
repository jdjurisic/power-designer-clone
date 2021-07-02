import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { AuthGuard } from './auth/auth.guard';
import { LoginService } from './services/login-service';
import { AuthInterceptor } from './auth/auth-interceptor';
import { CommonModule } from '@angular/common';
import { TopcomponentComponent } from './topcomponent/topcomponent.component';
import { MainComponent } from './main/main.component';
import { UserComponent } from './user/user.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TreeComponent } from './tree/tree.component';
import {DemoMaterialModule} from './tree/material-module';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
import { RqmTableComponent } from './rqm-table/rqm-table.component';
import { UsecaseEditorComponent } from './usecase-editor/usecase-editor.component';
import { InspectorComponent } from './inspector/inspector.component';
import { GojsAngularModule } from 'gojs-angular';
import { ClassmodelEditorComponent } from './classmodel-editor/classmodel-editor.component';
import { ClassinspectorComponent } from './classinspector/classinspector.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    TopcomponentComponent,
    MainComponent,
    UserComponent,
    TreeComponent,
    RqmTableComponent,
    UsecaseEditorComponent,
    InspectorComponent,
    ClassmodelEditorComponent,
    ClassinspectorComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    CommonModule,
    FormsModule,
    BrowserAnimationsModule,
    DemoMaterialModule,
    MatNativeDateModule,
    ReactiveFormsModule,
    GojsAngularModule

  ],
  providers: [
    AuthGuard, LoginService, 
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi:true},
    {provide: MAT_FORM_FIELD_DEFAULT_OPTIONS, useValue: { appearance: 'fill' } },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
