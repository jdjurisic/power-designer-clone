import { Component, EventEmitter, Input, Output } from '@angular/core';
import * as go from 'gojs';

@Component({
  selector: 'app-classinspector',
  templateUrl: './classinspector.component.html',
  styleUrls: ['./classinspector.component.css']
})
export class ClassinspectorComponent {

  public _selectedNode: go.Node;
  public data = {
    key: null,    
    name: null,
    properties:null,
    methods:null,
  };

  public newProperty = { name: null, type: null, visibility: null };
  public newMethod = { name: null, type:null, parameters: [], visibility: null};
  public newParameter = { name: null, type: null };

  public visibilityTypes:string[] = ["public", "private", "protected", "package"];

  @Input()
  public model: go.Model;

  @Output()
  public onFormChange: EventEmitter<any> = new EventEmitter<any>();

  @Input()
  get selectedNode() { return this._selectedNode; }
  set selectedNode(node: go.Node) {
    if (node) {
      this._selectedNode = node;
      this.data.key = this._selectedNode.data.key;
      this.data.name = this._selectedNode.data.name;
      this.data.properties = this._selectedNode.data.properties;
      this.data.methods = this._selectedNode.data.methods;

    } else {
      this._selectedNode = null;
      this.data.key = null;
      this.data.name = null;
      this.data.properties = null;
      this.data.methods = null;
    }
  }



  constructor() { }

  public onCommitForm() {
    this.onFormChange.emit(this.data);
  }

  addProperty(property:any){
    this.data.properties.push(property);
    this.newProperty = { name: null, type: null, visibility: null };
  }

  addMethod(method:any){
    console.log(method);
    this.data.methods.push(method);
    this.newMethod = { name: null,type: null, parameters: [], visibility: null};
  }

  addParameter(param:any){
    console.log(param);
    this.data.methods[this.data.methods.length-1].parameters.push(param);
    this.newParameter = { name: null, type: null };
  }

  removeProperty(prop:any){
    //console.log(prop);
    this.data.properties.splice(this.data.properties.findIndex(item => item.__gohashid == prop),1);
  }

  removeMethod(prop:any){
    //console.log(prop);
    this.data.methods.splice(this.data.methods.findIndex(item => item.__gohashid == prop),1);
  }

}