import { Component, OnChanges, OnInit } from '@angular/core';
import { Project } from '../models/project';
import { Model } from '../models/model';
import { RqmDocumentMongo } from '../models/rqmDocumentMongo';
import { RqmMongo } from '../models/rqmMongo';
import { Team } from '../models/team';
import { User } from '../models/user';
import { ProjectService } from '../services/project.service';
import { ModelService } from '../services/model.service';
import { RqmMongoService } from '../services/rqm-mongo.service';
import { TeamService } from '../services/team.service';
import { UserService } from '../services/user.service';
import { ModelType } from '../models/modelType';
import { UseCaseMongoService } from '../services/usecase-mongo.service';
import { UseCaseMongo } from '../models/usecaseMongo';
import { ClassModelMongoService } from '../services/classmodel-mongo.service';
import { ClassModelMongo } from '../models/classModelMongo';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  constructor(private teamService:TeamService,
              private userService:UserService,
              private projectService:ProjectService,
              private rqmMongoService:RqmMongoService,
              private modelService:ModelService,
              private useCaseService:UseCaseMongoService,
              private classService:ClassModelMongoService) { }

  currentUsername!:string;
  currentTeam!:Team;
  
  
  model:Model=new Model();
  models:Model[]=[];
  selectedModel:Model;
  selectedModelId:number;


  project:Project=new Project();
  projects:Project[]=[];
  selectedProject!:Project;
  selectedProjectId:number;
  
  rqmDocument:RqmDocumentMongo=new RqmDocumentMongo();
  rqmDocuments:RqmDocumentMongo[]=[];
  selectedRqmDocument:RqmDocumentMongo;

  rqm:RqmMongo=new RqmMongo();
  rqms:RqmMongo[]=[];
  selectedRqm:RqmMongo; // mozda moze bez ovoga

  // useCaseDocument:UseCaseDocumentMongo=new UseCaseDocumentMongo();
  // useCaseDocuments:UseCaseDocumentMongo[]=[];
  // selectedUseCaseDocument:UseCaseDocumentMongo;

  useCaseDocument:UseCaseMongo=new UseCaseMongo();
  useCaseDocuments:UseCaseMongo[]=[];
  selectedUseCaseDocument:UseCaseMongo;

  classDocument:ClassModelMongo = new ClassModelMongo();
  

  showTree:boolean;
  showProjectChooser:boolean;
  showModelChooser:boolean;
  showRqmModelTable:boolean;
  showUseCaseModelTable:boolean;
  showClassModelTable:boolean;

  getModelFromTree(model:Model){
    console.log("Ovo je moj veliki mongo key: " + model.mongoKey);
    console.log("Ovo je model koji smo dobili: " + model.id + '-' + model.name + '-' + model.modelType);
    this.selectModelFromTree(model.id);
    console.log(this.rqmDocument, this.model)
  } 




  ngOnInit(): void {
    // Oni su po default false?
    this.showTree = false
    this.showProjectChooser = true;
    this.showModelChooser = false;
    this.showRqmModelTable = false;

    this.currentUsername = localStorage.getItem("userName");
    this.userService.getUserByUsername(this.currentUsername).subscribe(data => {
      if(data.team==null){
        this.showTree=false
      }else{
        this.showTree = true
        this.currentTeam=data.team
        this.projects = data.team.projects
      }
    })
  }
  selectProject(){
    this.projectService.getProject(this.selectedProjectId).subscribe(data => {
      this.selectedProject = data
      console.log(this.selectedProject)
      this.models = this.selectedProject.models
      this.showModelChooser = true;
    })
  }
  selectModel(){
    this.modelService.getModel(this.selectedModelId).subscribe(data => {
        this.selectedModel = data
        console.log("SelectModel: " + this.selectedModel)
        this.model = this.selectedModel
        
        this.getRQM(this.model.mongoKey)

    })
  } // mozda moze da se zakomentarise...

  selectModelFromTree(reqId:number){
    this.modelService.getModel(reqId).subscribe(data => {
        this.selectedModel = data
        console.log("ModelFromTree: " + this.model)
        this.model = this.selectedModel

          console.log("TIP IZABRANOG MODELA JE:"+ this.model.modelType);
        if(this.model.modelType.toString() == "REQUIREMENT"){
          console.log("SelectmodelFromTree-Requirement-01");
          this.getRQM(this.model.mongoKey);
        }

        if(this.model.modelType.toString() == "USE_CASE"){
          this.getUseCase(this.model.mongoKey);
        }

        if(this.model.modelType.toString() == "CLASS"){
          console.log("SelectmodelFromTree-Class");
          this.getClass(this.model.mongoKey);
 
        }
    })
  }
  getProjects(){
    this.projectService.getProjects().subscribe(data => {
      this.projects=data;
    })
  }
  getModels(){ //ovo mi ne treba(mozda)
    this.modelService.getModels().subscribe(data => {
      this.models=data;
    })
  }
 
  getRQM(rqmDocumentMongoId:string){
    
    this.showUseCaseModelTable = false;
    this.rqmMongoService.getRQM(rqmDocumentMongoId).subscribe(data => {
      this.rqmDocument = data;
      console.log(this.rqmDocument)

      this.showUseCaseModelTable = false;
      this.showRqmModelTable = false;
      this.showClassModelTable = false;
      this.showRqmModelTable = true;

      
      console.log(this.showRqmModelTable)
      
      console.log(this.rqmDocument, this.model)
    })
  }

  saveRQM(rqmDocument:RqmDocumentMongo){
    this.rqmMongoService.createRQM(rqmDocument).subscribe(data => {
      this.rqmDocument = data;
      console.log(data)
    })
  }

  getUseCase(rqmDocumentMongoId:string){
    this.useCaseService.getUseCase(rqmDocumentMongoId).subscribe(data => {
      this.useCaseDocument = data;
      console.log("poyy mejn iz usecase-a")
      console.log(this.useCaseDocument)

      this.showRqmModelTable = false;
      this.showUseCaseModelTable = false;
      this.showClassModelTable = false;
      this.showUseCaseModelTable = true;

    
      console.log(this.showUseCaseModelTable)
      
      console.log(this.useCaseDocument, this.model)
    })

    // console.log("getUseCase pozvan");
    // this.useCaseDocument = new UseCaseMongo();
    // this.showRqmModelTable = false;
    // this.showUseCaseModelTable = false;
    // this.showUseCaseModelTable = true;

  }

  getClass(rqmDocumentMongoId:string){
    this.classService.getClassModel(rqmDocumentMongoId).subscribe(data => {
      this.classDocument = data;
      console.log("poyy mejn iz klasnog-a")
      console.log(this.classDocument)

      this.showClassModelTable = false;
      this.showRqmModelTable = false;
      this.showUseCaseModelTable = false;
      this.showClassModelTable = true;
      
      console.log(this.showClassModelTable)
      
      console.log(this.classDocument, this.model)
    })

    // console.log("getUseCase pozvan");
    // this.useCaseDocument = new UseCaseMongo();
    // this.showRqmModelTable = false;
    // this.showUseCaseModelTable = false;
    // this.showUseCaseModelTable = true;

  }


}


