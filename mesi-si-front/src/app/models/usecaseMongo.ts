import { LinkData } from "./linkData";
import { ModelData } from "./modelData";
import { NodeData } from "./nodeData";

export class UseCaseMongo{

    id:string;
    // name!:string;
    // actors:Actor[];
    // useCases!:UseCase[];
    // links!:Link[];
    class:"GraphLinksModel";
    linkKeyProperty: "key";
    linkFromPortIdProperty: "fromPort";
    linkToPortIdProperty: "toPort";
    modelData:ModelData;
    nodeDataArray:NodeData[];
    linkDataArray:LinkData[];
    

    lastModifiedBy: string;
    previousVersionId:string;
    nextVersionId:string;


    //{
    // class:"GraphLinksModel",
    // linkKeyProperty: "key",
    // linkFromPortIdProperty: "fromPort",
    // linkToPortIdProperty: "toPort",
    // modelData: {
    //   prop: "value",
    //   position:string;
    // },
        // nodeDataArray:[
    //           {
    //             key: "Actor",
    //             text: "Peirsa",
    //             color: "indianred",
    //             category: "actor",
    //             loc: "231 110.60000610351562"
    //           },
    //           {
    //             key: "Use Case2",
    //             text: "Use Case",
    //             color: "lightsalmon",
    //             category: "usecase",
    //             loc: "159.3299475381541 267.76108703613284"
    //           }
    //         ],
    // linkDataArray:[ {
    //              category: "include",
    //             from: "Actor",
    //             to: "Use Case2",
    //             fromPort: "b",
    //             toPort: "t",
    //             key: -1
    //            }]

}
