import { Component, OnInit} from '@angular/core';
import {FlatTreeControl} from '@angular/cdk/tree';
import {MatTreeFlatDataSource, MatTreeFlattener} from '@angular/material/tree';
import { Project } from '../models/project';
import { Input } from '@angular/core';
import { Output, EventEmitter } from '@angular/core';
import { Model } from '../models/model';
import { ModelType } from '../models/modelType';

 interface TreeNode {
  name: string;
  id:number;
  models?: TreeNode[];
  mongoKey?:string;
  modelType?:ModelType;
}


/** Flat node with expandable and level information */
interface ExampleFlatNode {
  expandable: boolean;
  name: string;
  level: number;
}

/**
 * @title Tree with flat nodes
 */

@Component({
  selector: 'app-tree',
  templateUrl: './tree.component.html',
  styleUrls: ['./tree.component.css']
})


export class TreeComponent implements OnInit {

  @Input() projects!: Project[];


  @Output() sendModel = new EventEmitter<Model>();

  addNewItem(model:Model){
    this.sendModel.emit(model);
  }


  selectedRQM:string ="";
  
  private _transformer = (node: TreeNode, level: number) => {
    return {
      expandable: !!node.models && node.models.length > 0,
      name: node.name,
      level: level,
      mongoKey:node.mongoKey,
      id: node.id,
      modelType:node.modelType
    };
  }

  treeControl = new FlatTreeControl<ExampleFlatNode>(
      node => node.level, node => node.expandable);

  treeFlattener = new MatTreeFlattener(
      this._transformer, node => node.level, node => node.expandable, node => node.models);

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  constructor() {

  }

  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;

  ngOnInit(): void {
    this.dataSource.data = this.projects;
  }

  onSave(event?: MouseEvent) {
    console.log("Izabran je "+ this.selectedRQM);
  }
  onSaveR(mongoKey:string, event?: MouseEvent) {
    console.log("Izabran je "+ mongoKey);
  }
}


