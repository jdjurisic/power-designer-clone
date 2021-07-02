import { Component, OnInit } from '@angular/core';
import { FormBuilder, NgForm } from '@angular/forms';
import { Project } from '../models/project';
import { Model } from '../models/model';
import { RqmDocumentMongo } from '../models/rqmDocumentMongo';
import { Team } from '../models/team';
import { User } from '../models/user';
import { UserType } from '../models/userType';
import { ProjectService } from '../services/project.service';
import { ModelService } from '../services/model.service';
import { RqmMongoService } from '../services/rqm-mongo.service';
import { TeamService } from '../services/team.service';
import { UserService } from '../services/user.service';
import { ModelType } from '../models/modelType';
import { UseCaseMongoService } from '../services/usecase-mongo.service';
import { ClassModelMongoService } from '../services/classmodel-mongo.service';
import { ClassModelMongo } from '../models/classModelMongo';
import { UseCaseMongo } from '../models/usecaseMongo';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  users:User[]=[];
  isAdmin:boolean=false;
  isUser:boolean=false;
  type:string=window.localStorage.getItem("isAdmin")!;

  user:User=new User();
  types:string[]=[];
  selectedType:string="";

  userU:User=new User();
  selectedTypeU:string="";

  updateB:boolean=false;

  team:Team=new Team();
  teams:Team[]=[];
  selectedTeam!:Team;
  selectedTeamID!:number;
  
  
  project:Project=new Project();
  projects:Project[]=[];
  selectedProject!:Project;
  selectedProjectId!:number;
  selectedProjectForModelId!:number;
  selectedTeamId!:number;

  
  model:Model=new Model();
  models:Model[]=[];
  selectedModel!:Model;
  selectedModelId!:number;
  selectedModelType:ModelType;
  modelTypes:ModelType[] = [ModelType.REQUIREMENT,ModelType.USE_CASE,ModelType.CLASS];

  TypeLabel = new Map<number, string>([
    [ModelType.REQUIREMENT, 'Requirement'],
    [ModelType.USE_CASE, 'Use Case'],
    [ModelType.CLASS, 'Class Model']
  ]);


  newUser:boolean = false;
  newTeam:boolean = false;
  newProject:boolean = false;
  newModel:boolean = false;

  createRqmDocument: RqmDocumentMongo
  createUseCaseDocument: UseCaseMongo
  createClassModelDocument: ClassModelMongo;


  constructor(private userService:UserService, 
              private projectService:ProjectService, 
              private teamService:TeamService, 
              private modelService:ModelService,
              private formBuilder:FormBuilder,
              private rqmMongoService:RqmMongoService,
              private useCaseMongoService:UseCaseMongoService,
              private classModelMongoService:ClassModelMongoService) { }

  ngOnInit(): void {
    this.isUserAdmin()
    this.getUsers();
    this.getTypes();
    this.getTeams();
    this.getProjects();
    this.getModels();
  }
  isUserAdmin() {
    if(this.type=="ADMIN")
      this.isAdmin=true;
    else
      this.isUser=true;
  }
 

  addUserToTeam(id:number){
    if(this.selectedTeamID != null){
      this.userService.addUserToTeam(id, this.selectedTeamID).subscribe(data => {
        this.getUsers();
      })
    }
  }
   
  addProjectToTeam(id:number){
    if(this.selectedTeamId != null){
      this.userService.addProjectToTeam(id, this.selectedTeamId).subscribe(data => {
        this.getProjects();
      })
    }
  }

  addModelToProject(id:number){
    if(this.selectedProjectForModelId != null){
      this.projectService.addModelToProject(id, this.selectedProjectForModelId).subscribe(data => {
        this.getModels();
      })
    }
  }
  addModelToProjectV2(idModel:number, idProject:number ){
    if(this.selectedProjectForModelId != null){
      this.projectService.addModelToProject(idModel, idProject).subscribe(data => {
        this.getModels();
      })
    }
  }
  getModels() {
    this.modelService.getModels().subscribe(data => {
      console.log(data);
      this.models = data;
    })
  }
  getTeams(){
    this.userService.getTeams().subscribe(data => {
      console.log(data);
      this.teams = data;
    })
  }
  getProjects() {
    this.projectService.getProjects().subscribe(data => {
      console.log(data);
      this.projects = data;
    })
  }
  getUsers(){
    this.userService.getUsers().subscribe(data => {
      this.users=data;
    })
  }
  updateUser1(id:number){
    this.userService.getUser(id).subscribe(data => {
      this.userU = data;
    })
    this.showUpdateUserForm()
  }

  updateUser(){
    if(this.selectedTypeU == "ADMIN")
      this.userU.userType = UserType.ADMIN;
    else
      this.userU.userType = UserType.USER;
    this.userService.updateUser(this.userU, this.userU.id).subscribe(data => {
      this.userU.username="";
      this.selectedTypeU="";
      this.userU.password="";
      this.updateB=false;
      this.getUsers();
    })

  }
  deleteUser(id:number){
    this.userService.deleteUser(id).subscribe(data => {
      this.getUsers();
    })
  }

  getTypes(){
    this.userService.getTypes().subscribe(data => {
      this.types = data;
    })
  }

  createUser(date:NgForm){
    console.log(this.selectedType);
    if(this.selectedType =="ADMIN"){
      this.user.userType = UserType.ADMIN;
    } 
    else {
      this.user.userType = UserType.USER;
    }
    console.log(this.user);
    this.userService.createUser(this.user).subscribe(data => {
      console.log(data);
      this.user.username="";
      this.selectedType="";
      this.user.password="";
      this.getUsers();
    },
    error=>  console.error(console.error())
    );
  }
  createProject(date:NgForm){
    console.log("Selektovan tim id" + this.selectedTeamId);
    this.teamService.getTeam(this.selectedTeamId).subscribe(data => {
      this.project.team = data;
      this.projectService.createProject(this.project).subscribe(data => {
        console.log(data);
        this.project.name="";
        this.project.team=null;
        this.getProjects();
      },
        error=>  console.error(console.error())
      );
    })
  }

  createModel(date:NgForm){
    console.log("Selektovan project id " + this.selectedProjectForModelId);
    
    if(this.selectedModelType == 0)
      this.createRQM();
      //console.log("Pravim RQM.")
    if(this.selectedModelType == 1)
      this.createUseCase();
    //console.log("Pravim UseCase.")
    if(this.selectedModelType == 2)
      this.createClassModel();
      //console.log("Pravim Class model.")
      //console.log("Pravim ClassModel.")
    
    
  }
  createRQM() {
    console.log("Selektovan project id " + this.selectedProjectForModelId);
    this.createRqmDocument = new RqmDocumentMongo
    this.createRqmDocument.rqms = []   
    this.rqmMongoService.createRQM(this.createRqmDocument).subscribe(data => {
    this.model.mongoKey = data.id
    this.model.modelType = this.selectedModelType;
    this.modelService.createModel(this.model).subscribe(data => {
        console.log(data);
        this.model.name="";
        this.model.mongoKey="";
        this.model.modelType=null;
        this.addModelToProjectV2(data.id, this.selectedProjectForModelId);
      },
        error=>  console.error(console.error())
      );
    });
  }
  createUseCase() {
    console.log("Selektovan project id " + this.selectedProjectForModelId);
    this.createUseCaseDocument = new UseCaseMongo();
   // this.createUseCaseDocument.useCases = []   
    this.useCaseMongoService.createUseCase(this.createUseCaseDocument).subscribe(data => {
    this.model.mongoKey = data.id
    this.model.modelType = this.selectedModelType;
    this.modelService.createModel(this.model).subscribe(data => {
      
        console.log(data);
        this.model.name="";
        this.model.mongoKey="";
        this.model.modelType=null;
        this.addModelToProjectV2(data.id, this.selectedProjectForModelId);
      },
        error=>  console.error(console.error())
      );
    });
  }
  createClassModel() {
    console.log("Selektovan project id " + this.selectedProjectForModelId);
    this.createClassModelDocument = new ClassModelMongo
    this.classModelMongoService.createClassModel(this.createClassModelDocument).subscribe(data => {
    this.model.mongoKey = data.id
    this.model.modelType = this.selectedModelType;
    this.modelService.createModel(this.model).subscribe(data => {
        console.log(data);
        this.model.name="";
        this.model.mongoKey="";
        this.model.modelType=null;
        this.addModelToProjectV2(data.id, this.selectedProjectForModelId);
      },
        error=>  console.error(console.error())
      );
    });
  }
 
  createTeam(date:NgForm){
    this.teamService.createTeam(this.team).subscribe(data => {
      console.log(data);
      this.team.name="";
      this.getTeams();
    },
      error=>  console.error(console.error())
    );
  }

  showNewUserForm(){
    this.newUser = true;
    this.newTeam = false;
    this.newProject = false;
    this.newModel = false;
    this.updateB=false;
  }
  showNewTeamForm(){
    this.newUser = false;
    this.newTeam = true;
    this.newProject = false;
    this.newModel = false;
    this.updateB=false;
  }
  showNewProjectForm(){
    this.newUser = false;
    this.newTeam = false;
    this.newProject = true;
    this.newModel = false;
    this.updateB=false;
  }
  showNewModelForm(){
    this.newUser = false;
    this.newTeam = false;
    this.newProject = false;
    this.newModel = true;
    this.updateB=false;
  }
  showUpdateUserForm(){
    this.newUser = false;
    this.newTeam = false;
    this.newProject = false;
    this.newModel = false;
    this.updateB=true;
  }
}


