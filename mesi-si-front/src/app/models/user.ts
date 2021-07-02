import { Team } from "./team";
import { UserType } from "./userType";

export class User {
    id!:number;
    username!:string;
    password!:string;
    userType!:UserType;
    team!:Team;
}