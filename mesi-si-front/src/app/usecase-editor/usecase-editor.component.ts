import { ChangeDetectorRef, Component, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import * as go from 'gojs';
import { DataSyncService, DiagramComponent, PaletteComponent } from 'gojs-angular';
import * as _ from 'lodash';
import { Model } from '../models/model';
import { UseCaseMongo } from '../models/usecaseMongo';
import { UseCaseMongoService } from '../services/usecase-mongo.service';

@Component({
  selector: 'app-usecase-editor',
  templateUrl: './usecase-editor.component.html',
  styleUrls: ['./usecase-editor.component.css'],
  encapsulation: ViewEncapsulation.ShadowDom

})
export class UsecaseEditorComponent implements OnInit {

  @ViewChild('myDiagram', { static: true }) public myDiagramComponent: DiagramComponent;
  @ViewChild('myPalette', { static: true }) public myPaletteComponent: PaletteComponent;

  @Input() useCaseDocument:UseCaseMongo;
  @Input() model:Model;

  public typeLink:string = 'generalization';
  bekap:any = {};

  public noviUseCase:UseCaseMongo = new UseCaseMongo();

  ngOnInit(): void {
  }

// initialize diagram / templates
public initDiagram(): go.Diagram {
 
  const $ = go.GraphObject.make;
  const dia = $(go.Diagram, {
    'undoManager.isEnabled': true,
    model: $(go.GraphLinksModel,
      {
        linkToPortIdProperty: 'toPort',
        linkFromPortIdProperty: 'fromPort',
        linkKeyProperty: 'key' // IMPORTANT! must be defined for merges and data sync when using GraphLinksModel
      }
    )
  });

  dia.commandHandler.archetypeGroupData = { key: 'Group', isGroup: true };

  const makePort = function(id: string, spot: go.Spot) {
    return $(go.Shape, 'Circle',
      {
        fill: 'null', strokeWidth: 0, desiredSize: new go.Size(8, 8),
        portId: id, alignment: spot,
        fromLinkable: true, toLinkable: true
      }
    );
  }
  // aktor pokusaj
  var alpha2 = Math.PI / 4;
  var KAPPA = ((4 * (1 - Math.cos(alpha2))) / (3 * Math.sin(alpha2))); 
  go.Shape.defineFigureGenerator("Actor", function(shape, w, h) {
    var geo = new go.Geometry();
    var radiusw = .2;
    var radiush = .1;
    var offsetw = KAPPA * radiusw;
    var offseth = KAPPA * radiush;
    var centerx = .5;
    var centery = .1;
    var fig = new go.PathFigure(centerx * w, (centery + radiush) * h, true);
    geo.add(fig);
  
    // Head
    fig.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - radiusw) * w, centery * h, (centerx - offsetw) * w, (centery + radiush) * h,
    (centerx - radiusw) * w, (centery + offseth) * h));
    fig.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - radiush) * h, (centerx - radiusw) * w, (centery - offseth) * h,
    (centerx - offsetw) * w, (centery - radiush) * h));
    fig.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + radiusw) * w, centery * h, (centerx + offsetw) * w, (centery - radiush) * h,
    (centerx + radiusw) * w, (centery - offseth) * h));
    fig.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery + radiush) * h, (centerx + radiusw) * w, (centery + offseth) * h,
    (centerx + offsetw) * w, (centery + radiush) * h));
    var r = .05;
    var cpOffset = KAPPA * r;
    centerx = .05;
    centery = .25;
    var fig2 = new go.PathFigure(.5 * w, .2 * h, true);
    geo.add(fig2);
    // Body
    fig2.add(new go.PathSegment(go.PathSegment.Line, .95 * w, .2 * h));
    centerx = .95;
    centery = .25;
    // Right arm
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + r) * w, centery * h, (centerx + cpOffset) * w, (centery - r) * h,
    (centerx + r) * w, (centery - cpOffset) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, w, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .85 * w, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .85 * w, .35 * h));
    r = .025;
    cpOffset = KAPPA * r;
    centerx = .825;
    centery = .35;
    // Right under arm
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
    (centerx + cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
    (centerx - r) * w, (centery - cpOffset) * h));
    // Right side/leg
    fig2.add(new go.PathSegment(go.PathSegment.Line, .8 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .55 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .55 * w, .7 * h));
    // Right in between
    r = .05;
    cpOffset = KAPPA * r;
    centerx = .5;
    centery = .7;
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
    (centerx + cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
    (centerx - r) * w, (centery - cpOffset) * h));
    // Left side/leg
    fig2.add(new go.PathSegment(go.PathSegment.Line, .45 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .2 * w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .2 * w, .35 * h));
    r = .025;
    cpOffset = KAPPA * r;
    centerx = .175;
    centery = .35;
    // Left under arm
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
    (centerx + cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
    (centerx - r) * w, (centery - cpOffset) * h));
    // Left arm
    fig2.add(new go.PathSegment(go.PathSegment.Line, .15 * w, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .6 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .25 * h));
    r = .05;
    cpOffset = KAPPA * r;
    centerx = .05;
    centery = .25;
    // Left shoulder
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx - r) * w, (centery - cpOffset) * h,
    (centerx - cpOffset) * w, (centery - r) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .5 * w, .2 * h));
    geo.spot1 = new go.Spot(.2, .2);
    geo.spot2 = new go.Spot(.8, .65);
    return geo;
  });
  
  // define the Node template
  var usecasetemplate =
    $(go.Node, 'Spot',
      { locationSpot: go.Spot.Center },
      new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
      {
        contextMenu:
          $('ContextMenu',
            $('ContextMenuButton',
              $(go.TextBlock, 'Group'),
              { click: function(e, obj) { e.diagram.commandHandler.groupSelection(); } },
              new go.Binding('visible', '', function(o) {
                return o.diagram.selection.count > 1;
              }).ofObject())
          )
      },
      $(go.Panel, 'Auto',
        $(go.Shape, 'Ellipse', { stroke: null },
          new go.Binding('fill', 'color')
        ),
        $(go.TextBlock, { margin: 8 , editable:true},
          new go.Binding('text'))
      ),
      // Ports
      makePort('t', go.Spot.TopCenter),
      makePort('l', go.Spot.Left),
      makePort('r', go.Spot.Right),
      makePort('b', go.Spot.BottomCenter),
      { // handle mouse enter/leave events to show/hide the ports
        mouseEnter: function(e, node) { showSmallPorts(node, true); },
        mouseLeave: function(e, node) { showSmallPorts(node, false); }
      }
    );

    var actortemplate =
    $(go.Node, 'Spot',
    { locationSpot: go.Spot.Center },
    new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
      {
        contextMenu:
          $('ContextMenu',
            $('ContextMenuButton',
              $(go.TextBlock, 'Group'),
              { click: function(e, obj) { e.diagram.commandHandler.groupSelection(); } },
              new go.Binding('visible', '', function(o) {
                return o.diagram.selection.count > 1;
              }).ofObject())
          )
      }, //           { row: 0, column: 0, fill: "green",        width: 100, height: 20 })
      $(go.Panel, 'Spot',
        $(go.Shape, 'Actor', { stroke: null,width: 45, height: 45 },
          new go.Binding('fill', 'color')
        ),
        $(go.TextBlock,"BL", {
        alignment: go.Spot.BottomCenter, alignmentFocus: go.Spot.TopCenter   , editable:true},
          new go.Binding('text')),
      ),
      // Ports
      makePort('t', go.Spot.TopCenter),
      makePort('l', go.Spot.Left),
      makePort('r', go.Spot.Right),
      makePort('b', go.Spot.BottomCenter),
      { // handle mouse enter/leave events to show/hide the ports
        mouseEnter: function(e, node) { showSmallPorts(node, true); },
        mouseLeave: function(e, node) { showSmallPorts(node, false); }
      }
    );

    function showSmallPorts(node, show) {
      node.ports.each(function(port) {
        if (port.portId !== "") {  // don't change the default port, which is the big shape
          port.fill = show ? "rgba(0,0,0,.3)" : null;
        }
      });
    }

    var templmap = new go.Map<string, go.Node>(); // In TypeScript you could write: new go.Map<string, go.Node>();
    // for each of the node categories, specify which template to use
    templmap.add("actor", actortemplate);
    templmap.add("usecase", usecasetemplate);
    //templmap.add("", dia.nodeTemplate);

    dia.nodeTemplateMap = templmap;

    //novo
          // define the Link template
          // dia.linkTemplate =
          // $(go.Link, go.Link.Orthogonal,
          //   { corner: 5, relinkableFrom: true, relinkableTo: true },
          //   $(go.Shape, { strokeWidth: 2 }));  // the link shape
  
    dia.linkTemplateMap.add("include",
      $(go.Link, go.Link.Bezier,
        { isLayoutPositioned: false, isTreeLink: false, curviness: -10 },
        { relinkableFrom: true, relinkableTo: true },
        $(go.Shape,
          { stroke: "lightskyblue", strokeWidth: 3, strokeDashArray: [8, 4] }),
        $(go.Shape,
          { toArrow: "Standard", stroke: "lightskyeblue", strokeWidth: 4 }),
        $(go.TextBlock,
          new go.Binding("text", "category"),
          {
            stroke: "black", background: "rgba(255,255,255,0.95)",
            maxSize: new go.Size(80, NaN)
          })));

    dia.linkTemplateMap.add("extend",
      $(go.Link, go.Link.Bezier,
        { isLayoutPositioned: false, isTreeLink: false, curviness: -10 },
        { relinkableFrom: true, relinkableTo: true },
        $(go.Shape,
          { stroke: "lightskyeblue", strokeWidth: 3, strokeDashArray: [8, 4] }),
        $(go.Shape,
          { toArrow: "Standard", stroke: "lightskyeblue", strokeWidth: 4 }),
        $(go.TextBlock,
          new go.Binding("text", "category"),
          {
            stroke: "black", background: "rgba(255,255,255,0.95)",
            maxSize: new go.Size(80, NaN)
          })));

          dia.linkTemplateMap.add("generalization",
      $(go.Link, go.Link.Bezier,
        { isLayoutPositioned: false, isTreeLink: false, curviness: -10 },
        { relinkableFrom: true, relinkableTo: true },
        $(go.Shape,
          { stroke: "lightskyeblue", strokeWidth: 3}),
        $(go.Shape,
          { toArrow: "Standard", stroke: "lightskyeblue", strokeWidth: 4 }))); 

          dia.linkTemplateMap.add("association",
          $(go.Link, go.Link.Bezier,
            { isLayoutPositioned: false, isTreeLink: false, curviness: -10 },
            { relinkableFrom: true, relinkableTo: true },
            $(go.Shape,
              { stroke: "lightskyeblue", strokeWidth: 3}),
            )); 


      // default je general.
      (dia.model as go.GraphLinksModel).setCategoryForLinkData(dia.toolManager.linkingTool.archetypeLinkData,"generalization");
  
      return dia;
}

public diagramNodeData: Array<go.ObjectData> = 
// [
//   {
//       "key": "UseCase - 0",
//       "text": "DefTitle0",
//       "color": "lightblue",
//       "category": "usecase"
//   },
//   {
//       "key": "user",
//       "text": "User",
//       "color": "orange",
//       "category": "actor"
//   },
//   {
//       "key": "UseCase - 1",
//       "text": "DefTitle1",
//       "color": "lightblue",
//       "category": "usecase"
//   },
//   {
//       "key": "UseCase - 2",
//       "text": "InnerDefTit",
//       "color": "lightblue",
//       "category": "usecase"
//   },
//   {
//       "key": "UseCase - 3",
//       "text": "InnerDefTit",
//       "color": "lightblue",
//       "category": "usecase"
//   }
// ]




[
  { key: 'Alpha', text: "Node Alpha", color: 'lightblue', category:'actor'},
  { key: 'Beta', text: "Node Beta", color: 'orange', category:'actor' },
  { key: 'Gamma', text: "Node Gamma", color: 'lightgreen',category:'usecase' },
  { key: 'Delta', text: "Node Delta", color: 'pink', category:'usecase' }
];
public diagramLinkData: Array<go.ObjectData> = 
//  [
//   {
//       "key": 0,
//       "from": "user",
//       "to": "UseCase - 0",
//       "fromPort": "b",
//       "toPort": "t",
//       "category": "association"
//   },
//   {
//       "key": 1,
//       "from": "user",
//       "to": "UseCase - 1",
//       "fromPort": "b",
//       "toPort": "t",
//       "category": "association"
//   },
//   {
//       "key": 2,
//       "from": "UseCase - 2",
//       "to": "UseCase - 1",
//       "fromPort": "b",
//       "toPort": "t",
//       "category": "generalization"
//   },
//   {
//       "key": 3,
//       "from": "UseCase - 3",
//       "to": "UseCase - 1",
//       "fromPort": "b",
//       "toPort": "t",
//       "category": "generalization"
//   }
// ]



[
  { key: -1, from: 'Gamma', to: 'Delta', fromPort: 'r', toPort: 'l', category:"association"  },
  { key: -2, from: 'Delta', to: 'Alpha', fromPort: 't', toPort: 'r', category:"association" },
  { key: -3, from:'Alpha', to:'Gamma',fromPort: 'r', toPort: 'l'  , category:"include"},
  { key: -4, from:'Beta', to:'Delta', fromPort: 'r', toPort: 'l' ,category:"extend"},
];
public diagramDivClassName: string = 'myDiagramDiv';
public diagramModelData = { prop: 'value' };
public skipsDiagramUpdate = false;

// When the diagram model changes, update app data to reflect those changes
public diagramModelChange = function(changes: go.IncrementalData) {
  // when setting state here, be sure to set skipsDiagramUpdate: true since GoJS already has this update
  // (since this is a GoJS model changed listener event function)
  // this way, we don't log an unneeded transaction in the Diagram's undoManager history
  this.skipsDiagramUpdate = true;

  this.diagramNodeData = DataSyncService.syncNodeData(changes, this.diagramNodeData);
  this.diagramLinkData = DataSyncService.syncLinkData(changes, this.diagramLinkData);
  this.diagramModelData = DataSyncService.syncModelData(changes, this.diagramModelData);
};



public initPalette(): go.Palette {
  const $ = go.GraphObject.make;
  const palette = $(go.Palette);

// define the Node template
  // dia.nodeTemplate =
  var usecasetemplate =
    $(go.Node, 'Spot',
      {
        contextMenu:
          $('ContextMenu',
            $('ContextMenuButton',
              $(go.TextBlock, 'Group'),
              { click: function(e, obj) { e.diagram.commandHandler.groupSelection(); } },
              new go.Binding('visible', '', function(o) {
                return o.diagram.selection.count > 1;
              }).ofObject())
          )
      },
      $(go.Panel, 'Auto',
        $(go.Shape, 'Ellipse', { stroke: null },
          new go.Binding('fill', 'color')
        ),
        $(go.TextBlock, { margin: 8 },
          new go.Binding('text'))
      ),
    );

// aktor pokusaj
var alpha2 = Math.PI / 4;
var KAPPA = ((4 * (1 - Math.cos(alpha2))) / (3 * Math.sin(alpha2))); 
go.Shape.defineFigureGenerator("Actor", function(shape, w, h) {
 var geo = new go.Geometry();
 var radiusw = .2;
 var radiush = .1;
 var offsetw = KAPPA * radiusw;
 var offseth = KAPPA * radiush;
 var centerx = .5;
 var centery = .1;
 var fig = new go.PathFigure(centerx * w, (centery + radiush) * h, true);
 geo.add(fig);

 // Head
 fig.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - radiusw) * w, centery * h, (centerx - offsetw) * w, (centery + radiush) * h,
 (centerx - radiusw) * w, (centery + offseth) * h));
 fig.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - radiush) * h, (centerx - radiusw) * w, (centery - offseth) * h,
 (centerx - offsetw) * w, (centery - radiush) * h));
 fig.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + radiusw) * w, centery * h, (centerx + offsetw) * w, (centery - radiush) * h,
 (centerx + radiusw) * w, (centery - offseth) * h));
 fig.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery + radiush) * h, (centerx + radiusw) * w, (centery + offseth) * h,
 (centerx + offsetw) * w, (centery + radiush) * h));
 var r = .05;
 var cpOffset = KAPPA * r;
 centerx = .05;
 centery = .25;
 var fig2 = new go.PathFigure(.5 * w, .2 * h, true);
 geo.add(fig2);
 // Body
 fig2.add(new go.PathSegment(go.PathSegment.Line, .95 * w, .2 * h));
 centerx = .95;
 centery = .25;
 // Right arm
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + r) * w, centery * h, (centerx + cpOffset) * w, (centery - r) * h,
 (centerx + r) * w, (centery - cpOffset) * h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, w, .6 * h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .85 * w, .6 * h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .85 * w, .35 * h));
 r = .025;
 cpOffset = KAPPA * r;
 centerx = .825;
 centery = .35;
 // Right under arm
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
 (centerx + cpOffset) * w, (centery - r) * h));
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
 (centerx - r) * w, (centery - cpOffset) * h));
 // Right side/leg
 fig2.add(new go.PathSegment(go.PathSegment.Line, .8 * w, h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .55 * w, h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .55 * w, .7 * h));
 // Right in between
 r = .05;
 cpOffset = KAPPA * r;
 centerx = .5;
 centery = .7;
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
 (centerx + cpOffset) * w, (centery - r) * h));
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
 (centerx - r) * w, (centery - cpOffset) * h));
 // Left side/leg
 fig2.add(new go.PathSegment(go.PathSegment.Line, .45 * w, h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .2 * w, h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .2 * w, .35 * h));
 r = .025;
 cpOffset = KAPPA * r;
 centerx = .175;
 centery = .35;
 // Left under arm
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx + r) * w, (centery - cpOffset) * h,
 (centerx + cpOffset) * w, (centery - r) * h));
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - r) * w, centery * h, (centerx - cpOffset) * w, (centery - r) * h,
 (centerx - r) * w, (centery - cpOffset) * h));
 // Left arm
 fig2.add(new go.PathSegment(go.PathSegment.Line, .15 * w, .6 * h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .6 * h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .25 * h));
 r = .05;
 cpOffset = KAPPA * r;
 centerx = .05;
 centery = .25;
 // Left shoulder
 fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - r) * h, (centerx - r) * w, (centery - cpOffset) * h,
 (centerx - cpOffset) * w, (centery - r) * h));
 fig2.add(new go.PathSegment(go.PathSegment.Line, .5 * w, .2 * h));
 geo.spot1 = new go.Spot(.2, .2);
 geo.spot2 = new go.Spot(.8, .65);
 return geo;
});

    var actortemplate =
    $(go.Node, 'Spot',
      {
        contextMenu:
          $('ContextMenu',
            $('ContextMenuButton',
              $(go.TextBlock, 'Group'),
              { click: function(e, obj) { e.diagram.commandHandler.groupSelection(); } },
              new go.Binding('visible', '', function(o) {
                return o.diagram.selection.count > 1;
              }).ofObject())
          )
      },
      $(go.Panel, 'Auto',
        $(go.Shape, 'Actor', { stroke: null, width:60, height:60 },
          new go.Binding('fill', 'color')
        ),
        $(go.TextBlock, { margin: 8 },
          new go.Binding('text'))
      ),
    );

    var templmap = new go.Map<string, go.Node>(); // In TypeScript you could write: new go.Map<string, go.Node>();
    // for each of the node categories, specify which template to use
    templmap.add("actor", actortemplate);
    templmap.add("usecase", usecasetemplate);
    //templmap.add("", dia.nodeTemplate);

    palette.nodeTemplateMap = templmap;

  // define the Node template
  palette.nodeTemplate =
    $(go.Node, 'Auto',
      $(go.Shape, 'RoundedRectangle',
        {
          stroke: null
        },
        new go.Binding('fill', 'color')
      ),
      $(go.TextBlock, { margin: 8 },
        new go.Binding('text'))
    );

  palette.model = $(go.GraphLinksModel,
    {
      linkKeyProperty: 'key'  // IMPORTANT! must be defined for merges and data sync when using GraphLinksModel
    });

  return palette;
}
public paletteNodeData: Array<go.ObjectData> = [
  { key: 'Actor', text: "Actor", color: 'indianred', category:'actor' },
  { key: 'Use Case', text: "Use Case", color: 'lightsalmon' , category:'usecase'}
];
public paletteLinkData: Array<go.ObjectData> = [
  {  }
];
public paletteModelData = { prop: 'val' };
public paletteDivClassName = 'myPaletteDiv';
public skipsPaletteUpdate = false;
public paletteModelChange = function(changes: go.IncrementalData) {
  // when setting state here, be sure to set skipsPaletteUpdate: true since GoJS already has this update
  // (since this is a GoJS model changed listener event function)
  // this way, we don't log an unneeded transaction in the Palette's undoManager history
  this.skipsPaletteUpdate = true;

  this.paletteNodeData = DataSyncService.syncNodeData(changes, this.paletteNodeData);
  this.paletteLinkData = DataSyncService.syncLinkData(changes, this.paletteLinkData);
  this.paletteModelData = DataSyncService.syncModelData(changes, this.paletteModelData);
};

constructor(private cdr: ChangeDetectorRef, private useCaseService:UseCaseMongoService) { }

// Overview Component testing
// public oDivClassName = 'myOverviewDiv';
// public initOverview(): go.Overview {
//   const $ = go.GraphObject.make;
//   const overview = $(go.Overview);
//   return overview;
// }
public observedDiagram = null;

// currently selected node; for inspector
public selectedNode: go.Node | null = null;

public ngAfterViewInit() {

  if (this.observedDiagram) return;
  this.observedDiagram = this.myDiagramComponent.diagram;
  this.cdr.detectChanges(); // IMPORTANT: without this, Angular will throw ExpressionChangedAfterItHasBeenCheckedError (dev mode only)

  const appComp: UsecaseEditorComponent = this;
  // listener for inspector
  this.myDiagramComponent.diagram.addDiagramListener('ChangedSelection', function(e) {
    if (e.diagram.selection.count === 0) {
      appComp.selectedNode = null;
    }
    const node = e.diagram.selection.first();
    if (node instanceof go.Node) {
      appComp.selectedNode = node;
    } else {
      appComp.selectedNode = null;
    }
  });

} // end ngAfterViewInit


public handleInspectorChange(newNodeData) {
  const key = newNodeData.key;
  // find the entry in nodeDataArray with this key, replace it with newNodeData
  let index = null;
  for (let i = 0; i < this.diagramNodeData.length; i++) {
    const entry = this.diagramNodeData[i];
    if (entry.key && entry.key === key) {
      index = i;
    }
  }

  if (index >= 0) {
    // here, we set skipsDiagramUpdate to false, since GoJS does not yet have this update
    this.skipsDiagramUpdate = false;
    this.diagramNodeData[index] = _.cloneDeep(newNodeData);
    // this.diagramNodeData[index] = _.cloneDeep(newNodeData);
  }

  // var nd = this.observedDiagram.model.findNodeDataForKey(newNodeData.key);
  // console.log(nd);

  console.log("Selected TypeLink :" + this.typeLink);
  
}

updateLinkCat(){
  console.log("Trenutni type je1111:" + this.typeLink);
  //this.myDiagramComponent.diagram.startTransaction("Update link category");
  (this.myDiagramComponent.diagram.model as go.GraphLinksModel).setCategoryForLinkData(this.myDiagramComponent.diagram.toolManager.linkingTool.archetypeLinkData,this.typeLink);
  //this.myDiagramComponent.diagram.commitTransaction("Link updated");
  console.log("Trenutni type je22222:" + this.typeLink);
}

 isError: boolean;
 errorMessage="";

  // Show the diagram's model in JSON format that the user may edit
   save() {
    this.saveDiagramProperties();  // do this first, before writing to JSON
    this.bekap = this.myDiagramComponent.diagram.model.toJson();//this.myDiagramComponent.diagram.model.modelData.toJson();

    this.noviUseCase = JSON.parse(this.bekap);
    this.noviUseCase.id = this.model.mongoKey;
    
    this.noviUseCase.lastModifiedBy = window.localStorage.getItem("userName");
    this.noviUseCase.previousVersionId = this.useCaseDocument.previousVersionId;
    this.noviUseCase.nextVersionId = this.useCaseDocument.nextVersionId;
    
    console.log("SALJEM:"+this.noviUseCase.previousVersionId);
    this.useCaseService.createUseCase(this.noviUseCase).subscribe(data => {
      console.log(data);
      this.isError = false;
    },
    error => {
      console.log(error);
      if(error.status == 409){
        // Conflict error handler hehe
        this.useCaseDocument = error.error;
        this.myDiagramComponent.diagram.model = go.Model.fromJson(this.useCaseDocument);
        this.loadDiagramProperties();  // do this after the Model.modelData has been brought into memory
        this.errorMessage="Doslo je do konflikta!"
        this.isError = true;

      }else{
      // Validation error handler hehe
      this.errorMessage=error.error;
      this.isError = true;
      }
    });
  }

   load() {
  
    this.useCaseService.getUseCase(this.model.mongoKey).subscribe(data =>{
      console.log("Ucitan je model.");
      this.isError = false;
      this.useCaseDocument = data;
      console.log("Prikaz modela ");
      this.myDiagramComponent.diagram.model = go.Model.fromJson(this.useCaseDocument);
      this.loadDiagramProperties();  // do this after the Model.modelData has been brought into memory
    })
  }

   saveDiagramProperties() {
    this.myDiagramComponent.diagram.model.modelData.position = go.Point.stringify(this.myDiagramComponent.diagram.position);
    console.log(this.myDiagramComponent.diagram.model.modelData);
  }


   loadDiagramProperties() {
    // set Diagram.initialPosition, not Diagram.position, to handle initialization side-effects
    var pos = this.myDiagramComponent.diagram.model.modelData.position;
    if(pos) this.myDiagramComponent.diagram.initialPosition = go.Point.parse(pos);
  }
}
