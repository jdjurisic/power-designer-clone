import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Input } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { Model } from '../models/model';
import { ModelType } from '../models/modelType';
import { Project } from '../models/project';
import { RqmDocumentMongo } from '../models/rqmDocumentMongo';
import { RqmMongo } from '../models/rqmMongo';
import { RqmMongoRisk } from '../models/rqmMongoRisk';
import { RqmMongoType } from '../models/rqmMongoType';
import { Team } from '../models/team';
import { UseCaseMongo } from '../models/usecaseMongo';
import { ModelService } from '../services/model.service';
import { ProjectService } from '../services/project.service';
import { RqmMongoService } from '../services/rqm-mongo.service';
import { Rqm2ucgeneratorService } from '../services/rqm2ucgenerator.service';
import { UseCaseMongoService } from '../services/usecase-mongo.service';

@Component({
  selector: 'app-rqm-table',
  templateUrl: './rqm-table.component.html',
  styleUrls: ['./rqm-table.component.css']
})
export class RqmTableComponent implements OnInit {
  
  @Input() rqmDocument:RqmDocumentMongo;
  @Input() model:Model;
  @Input() currentTeam: Team;
    
    


  constructor(private rqmMongoService:RqmMongoService,
              private modeltService:ModelService,
              private rqm2ucgeneratorService:Rqm2ucgeneratorService,
              private modelService:ModelService,
              private useCaseMongoService: UseCaseMongoService,
              private projectService: ProjectService) { }

    ngOnInit(): void {
      if(this.rqmDocument.rqms!=null){
        this.rqms = this.rqmDocument.rqms;
        // for koji prolazi kroz rqmove i ako nema listu usera da se stavi prazna lista usera...
        console.log("rqms!=null")
      }else{
        this.rqms = []
        this.rqmDocument.rqms=[]
        console.log("rqms==null")
      }
      console.log(this.rqmDocument.rqms[0].type)
    }

    rqmMongoRisks: string[] = ["LOW","MEDIUM", "HIGH"];
    rqmMongoTypes: string[] = ["FUNCTIONAL", "NON_FUNCTIONAL"];
    rqmMongoPriorities: number[] = [1, 2, 3, 4, 5];
    rqmMongoUsers: string[] = ["ADMIN", "USER"]; // Moze lako da se promeni sta je u listi..

    rqmMongoRisk: RqmMongoRisk;
    rqmMongoType: RqmMongoType;
    rqmMongoPriority: number;
    rqmMongoUser: string;

    editField: string;
    rqms:RqmMongo[]=[];
    //rqms:RqmMongo[]=[];


    rqm:RqmMongo=new RqmMongo();

    currentProject: Project
    
    isError: boolean;
    //rqms:RqmMongo[]=[];
/*
    personList: Array<any> = [
      { id: 1, name: 'Aurelia Vega', age: 30, companyName: 'Deepends', country: 'Spain', city: 'Madrid' },
      { id: 2, name: 'Guerra Cortez', age: 45, companyName: 'Insectus', country: 'USA', city: 'San Francisco' },
      { id: 3, name: 'Guadalupe House', age: 26, companyName: 'Isotronic', country: 'Germany', city: 'Frankfurt am Main' },
      { id: 4, name: 'Aurelia Vega', age: 30, companyName: 'Deepends', country: 'Spain', city: 'Madrid' },
      { id: 5, name: 'Elisa Gallagher', age: 31, companyName: 'Portica', country: 'United Kingdom', city: 'London' },
    ];
*/
    updateList(id: number, property: string, event: any) {
      const editField = event.target.textContent;
      this.rqmDocument.rqms[id][property] = editField;
    }
    updateListChild(idChild: number,id: number, property: string, event: any) {
      const editField = event.target.textContent;
      this.rqmDocument.rqms[id].rqms[idChild][property] = editField;
    }

    updateListChildChild(idChildCHild:number, idChild: number,id: number, property: string, event: any) {
      const editField = event.target.textContent;
      this.rqmDocument.rqms[id].rqms[idChild].rqms[idChildCHild][property] = editField;
    }

    remove(id: any) {
      this.rqmDocument.rqms.splice(id, 1);
      this.recalculateIndentations();
    }
    removeChild(idChild: any, idParent:number) {
      this.rqmDocument.rqms[idParent].rqms.splice(idChild, 1);
      this.recalculateIndentations();
    }
    removeChildChild(idChildChild: any,idChild: any, idParent:number) {
      this.rqmDocument.rqms[idParent].rqms[idChild].rqms.splice(idChildChild, 1);
      this.recalculateIndentations();
    }
    add() {
      // Add ne funkcionise kako treba
      // mora da se proveri RQMMONGO(model zahteva) jer se ne uklapaju bek\front\specifikacija

      this.rqm = new RqmMongo()
      // default
      this.rqm.indentation = Number(this.rqmDocument.rqms.length + 1).toString();
      this.rqm.title = "DefTitle" + this.rqmDocument.rqms.length.toString();
      this.rqm.description = "DefDesc" + this.rqmDocument.rqms.length.toString();
      // this.rqm.type = RqmMongoType.FUNCTIONAL
      this.rqm.priority = 0;
      //this.rqm.risk = RqmMongoRisk.LOW;
      //this.rqm.users = []
      this.rqm.rqms = []

      this.rqmDocument.rqms.push(this.rqm);
    }

    addInner(rqmId: number) {
      this.rqm = new RqmMongo()
      // default
      this.rqm.title = "InnerDefTit"
      this.rqm.description = "InnerDefDes"
      // this.rqm.type = RqmMongoType.FUNCTIONAL
      // this.rqm.priority = 0;
      // this.rqm.risk = RqmMongoRisk.LOW;
      // this.rqm.users = []
      this.rqm.rqms = []
      if(this.rqmDocument.rqms[rqmId].rqms==null){
        this.rqmDocument.rqms[rqmId].rqms=[]
      }
      this.rqm.indentation = this.rqmDocument.rqms[rqmId].indentation +"."+Number(this.rqmDocument.rqms[rqmId].rqms.length + 1).toString();
      this.rqmDocument.rqms[rqmId].rqms.push(this.rqm);
    }

    addInnerChild(childRqmId: number,rqmId: number) {
      this.rqm = new RqmMongo()
      // default
      this.rqm.indentation = "1.1.1"
      this.rqm.title = "InInDefTit"
      this.rqm.description = "InInDefDes"
      // this.rqm.type = RqmMongoType.FUNCTIONAL
      // this.rqm.priority = 0;
      // this.rqm.risk = RqmMongoRisk.LOW;
      // this.rqm.users = []
      this.rqm.rqms = []

      if(this.rqmDocument.rqms[rqmId].rqms[childRqmId].rqms==null){
        this.rqmDocument.rqms[rqmId].rqms[childRqmId].rqms=[]
      }
      console.log("ChildId + rqmId: " + childRqmId,rqmId)
      this.rqmDocument.rqms[rqmId].rqms[childRqmId].rqms.push(this.rqm);
    }
    
    changeValue(id: number, property: string, event: any) {
      this.editField = event.target.textContent;
    }

    // U saveRQM treba da se ubaci funkcija koja ispisuje na ekranu forEach(this.rqmDocument.generatedUseCaseIDs) nije up to date."
    
    ispis:String="Use cases" //with ids: ";
    show:boolean;

    nekaFunkcija(rqmDocument:RqmDocumentMongo){
        console.log(rqmDocument.generatedUseCaseIDs)
          for(let generatedId in rqmDocument.generatedUseCaseIDs){
              console.log("Pronasao je: " + generatedId)
              //this.ispis.concat(rqmDocument.generatedUseCaseIDs[], "\n")
              //this.ispis+=generatedId + " \n";
              this.ispis+="GeneratedUseCase" + generatedId+", ";
          }
          this.ispis+="are not up to date"
          this.show = true;
        }

    saveRQM(){
      this.rqmMongoService.createRQM(this.rqmDocument).subscribe(data => {
        this.rqmDocument = data;
        this.model.mongoKey = data.id
        this.rqmDocument.lastModifiedBy = window.localStorage.getItem("userName");
        // this.rqmDocument.previousVersionId = this.rqmDocument.previousVersionId;
        // this.rqmDocument.nextVersionId = this.rqmDocument.nextVersionId;
        this.modeltService.updateModel(this.model.id, this.model)
        console.log(data)
        this.isError = false;
        this.nekaFunkcija(this.rqmDocument);
      },
      error => {
        //              da izbaci warning da je doslo do konflikta
        
        if(error.status == 409){
          this.rqmDocument = error.error;
          this.isError = true;
          //this.recalculateIndentations();
        }
      });
    }

    useCaseMongo: UseCaseMongo;
    generatedModel: Model

    useCaseIDGenerator = 0
    generateUseCase(){
      this.rqm2ucgeneratorService.generateUseCaseFromRQM(this.rqmDocument).subscribe(data=>{
          this.generatedModel = new Model

          this.useCaseMongo = data;

          this.useCaseMongoService.saveGeneratedFile(this.useCaseMongo).subscribe(data => {
            console.log("SaveGeneratedFileData:" + data.id);

            // Ovde moze da se podesi lokacija
            //data.nodeDataArray[0].loc
            this.generatedModel.mongoKey = data.id
            this.generatedModel.name = "GeneratedUseCase"+this.useCaseIDGenerator
            this.useCaseIDGenerator++;
            this.generatedModel.modelType = ModelType.USE_CASE
            if(this.rqmDocument.generatedUseCaseIDs==null)
              this.rqmDocument.generatedUseCaseIDs = []
            this.rqmDocument.generatedUseCaseIDs.push(this.generatedModel.mongoKey)

            this.modelService.createModel(this.generatedModel).subscribe(data => {
                console.log(data);
                this.generatedModel.name="";
                this.generatedModel.mongoKey="";
                this.generatedModel.modelType=null;
                this.currentProject = this.currentTeam.projects[0]
                console.log("CurrentProjectID: - " + this.currentProject.id + ' -')
                this.addModelToProjectV2(data.id, 3); // 3 = test_project

                //Moram da sacuvam RQM sa novim generatedUseCaseIDs

                this.rqmMongoService.saveGenerated(this.rqmDocument).subscribe(data => {
                  this.rqmDocument = data;
                })
              
              })});
        })
    
    }
    addModelToProjectV2(idModel:number, idProject:number ){
        this.projectService.addModelToProject(idModel, idProject).subscribe(data => {

        })
      
    }
    recalculateIndentations(){
      var i, j;
      for(i = 0; i < this.rqmDocument.rqms.length; i++){
        this.rqmDocument.rqms[i].indentation = Number(i + 1).toString();
        for(j = 0; j < this.rqmDocument.rqms[i].rqms.length; j++){
          this.rqmDocument.rqms[i].rqms[j].indentation = Number(i + 1).toString() + "." + Number(j+1).toString();
        }
      }
    }
    
}
