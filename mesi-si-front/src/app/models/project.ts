import {Model} from "./model"
import { Team } from "./team";
export class Project{

    id!:number;
    name!:string;
    models:Model[];
    team:Team;
}