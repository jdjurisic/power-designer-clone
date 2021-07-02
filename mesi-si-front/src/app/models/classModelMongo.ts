import { ModelData } from "./modelData";

export class ClassModelMongo{

    id:string;
    //name!:string;

    
    lastModifiedBy: string;
    previousVersionId:string;
    nextVersionId:string;
}
/*
@Id
private String id;

private String linkKeyProperty;
private String linkFromPortIdProperty;
private String linkToPortIdProperty;
private ModelData modelData;

private List<NodeData> nodeDataArray;

private List<LinkData> linkDataArray;


private String previousVersionId;
private String nextVersionId; // ovo mislim da ne treba


*/