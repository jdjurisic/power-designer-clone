import { ModelType } from "./modelType";

export class Model{

    id!:number;
    name!:string;
    mongoKey!:string;
    modelType:ModelType;
    
}