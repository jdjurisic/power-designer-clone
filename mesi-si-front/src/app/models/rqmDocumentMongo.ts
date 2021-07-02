import { RqmMongo } from "./rqmMongo";
import { RqmMongoRisk } from "./rqmMongoRisk";

export class RqmDocumentMongo{

    id!:string;
   
    rqms:RqmMongo[];

    
    lastModifiedBy: string;
    previousVersionId:string;
    nextVersionId:string;


    generatedUseCaseIDs: string[];
    
}