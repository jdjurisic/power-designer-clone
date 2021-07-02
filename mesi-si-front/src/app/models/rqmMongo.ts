import { RqmMongoRisk } from "./rqmMongoRisk";
import { RqmMongoType } from "./rqmMongoType";

export class RqmMongo{

    id:number;
    indentation!:string;
    title!:string;
    description!:string;
    type!:RqmMongoType;
    priority!:number;
    risk!:RqmMongoRisk;
    rqms:RqmMongo[];
    users!:String;//[]; //[]
    
}

