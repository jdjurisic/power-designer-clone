import { ChangeDetectorRef, Component, Input, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import * as go from 'gojs';
import { DataSyncService, DiagramComponent, PaletteComponent } from 'gojs-angular';
import * as _ from 'lodash';
import { ClassModelMongo } from '../models/classModelMongo';
import { Model } from '../models/model';
import { ClassModelMongoService } from '../services/classmodel-mongo.service';
import { M2cgeneratorService } from '../services/m2cgenerator.service';

@Component({
  selector: 'app-classmodel-editor',
  templateUrl: './classmodel-editor.component.html',
  styleUrls: ['./classmodel-editor.component.css'],
  encapsulation: ViewEncapsulation.ShadowDom
})
export class ClassmodelEditorComponent implements OnInit {

  @ViewChild('myDiagram', { static: true }) public myDiagramComponent: DiagramComponent;
  @ViewChild('myPalette', { static: true }) public myPaletteComponent: PaletteComponent;

  @Input() classDocument:ClassModelMongo;
  @Input() model:Model;

  public typeLink:string = 'generalization';
  bekap: any;

  public noviClassDocument:ClassModelMongo = new ClassModelMongo();

  
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
  

      function showSmallPorts(node, show) {
        node.ports.each(function(port) {
          if (port.portId !== "") {  // don't change the default port, which is the big shape
            port.fill = show ? "rgba(0,0,0,.3)" : null;
          }
        });
      }

      // NOVO 
           // show visibility or access as a single character at the beginning of each property or method
           function convertVisibility(v) {
            switch (v) {
              case "public": return "+";
              case "private": return "-";
              case "protected": return "#";
              case "package": return "~";
              default: return v;
            }
          }
    
          // the item template for properties
          var propertyTemplate =
            $(go.Panel, "Horizontal",
              // property visibility/access
              $(go.TextBlock,
                { isMultiline: false, editable: false, width: 12 },
                new go.Binding("text", "visibility", convertVisibility)),
              // property name, underlined if scope=="class" to indicate static property
              $(go.TextBlock,
                { isMultiline: false, editable: true },
                new go.Binding("text", "name").makeTwoWay(),
                new go.Binding("isUnderline", "scope", function (s) { return s[0] === 'c' })),
              // property type, if known
              $(go.TextBlock, "",
                new go.Binding("text", "type", function (t) { return (t ? ": " : ""); })),
              $(go.TextBlock,
                { isMultiline: false, editable: true },
                new go.Binding("text", "type").makeTwoWay()),
              // property default value, if any
              // $(go.TextBlock,  // DEFAULT VREDNOST VALJDA !!!!
              //   { isMultiline: false, editable: false },
              //   new go.Binding("text", "default", function (s) { return s ? " = " + s : ""; }))
            );
    
          // the item template for methods
          var methodTemplate =
            $(go.Panel, "Horizontal",
              // method visibility/access
              $(go.TextBlock,
                { isMultiline: false, editable: false, width: 12 },
                new go.Binding("text", "visibility", convertVisibility)),
              // method name, underlined if scope=="class" to indicate static method
              $(go.TextBlock,
                { isMultiline: false, editable: true },
                new go.Binding("text", "name").makeTwoWay(),
                new go.Binding("isUnderline", "scope", function (s) { return s[0] === 'c' })),
              // method parameters
              $(go.TextBlock, "()",
                // this does not permit adding/editing/removing of parameters via inplace edits
                new go.Binding("text", "parameters", function (parr) {
                  var s = "(";
                  for (var i = 0; i < parr.length; i++) {
                    var param = parr[i];
                    if (i > 0) s += ", ";
                    s += param.name + ": " + param.type;
                  }
                  return s + ")";
                })),
              // method return type, if any
              $(go.TextBlock, "",
                new go.Binding("text", "type", function (t) { return (t ? ": " : ""); })),
              $(go.TextBlock,
                { isMultiline: false, editable: true },
                new go.Binding("text", "type").makeTwoWay())
            );
    

      var classtemplate =
      $(go.Node, 'Auto',
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
        $(go.Shape, { fill: "lightyellow" }),
        $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 3, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: true
              },
              new go.Binding("text", "name").makeTwoWay()),
            // properties
            $(go.TextBlock, "Properties",
              { row: 1, font: "italic 10pt sans-serif" },
              new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("PROPERTIES")),
            $(go.Panel, "Vertical", { name: "PROPERTIES" },
              new go.Binding("itemArray", "properties"),
              {
                row: 1, margin: 3, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightyellow",
                itemTemplate: propertyTemplate
              }
            ),
            // methods
            $(go.TextBlock, "Methods",
            { row: 2, font: "italic 10pt sans-serif" },
            new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("METHODS")),
          $(go.Panel, "Vertical", { name: "METHODS" },
            new go.Binding("itemArray", "methods"),
            {
              row: 2, margin: 3, stretch: go.GraphObject.Fill,
              defaultAlignment: go.Spot.Left, background: "lightyellow",
              itemTemplate: methodTemplate
            }
          ),),
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

      var abstractclasstemplate =
      $(go.Node, 'Auto',
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
        $(go.Shape, { fill: "lightcoral" }),
        $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 3, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: true
              },
              new go.Binding("text", "name").makeTwoWay()),
            // properties
            $(go.TextBlock, "Properties",
              { row: 1, font: "italic 10pt sans-serif" },
              new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("PROPERTIES")),
            $(go.Panel, "Vertical", { name: "PROPERTIES" },
              new go.Binding("itemArray", "properties"),
              {
                row: 1, margin: 3, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightcoral",
                itemTemplate: propertyTemplate
              }
            ),
            // methods
            $(go.TextBlock, "Methods",
            { row: 2, font: "italic 10pt sans-serif" },
            new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("METHODS")),
          $(go.Panel, "Vertical", { name: "METHODS" },
            new go.Binding("itemArray", "methods"),
            {
              row: 2, margin: 3, stretch: go.GraphObject.Fill,
              defaultAlignment: go.Spot.Left, background: "lightcoral",
              itemTemplate: methodTemplate
            }
          ),),
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

      var interfacetemplate =
      $(go.Node, 'Auto',
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
        $(go.Shape, { fill: "lightcyan" }),
        $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 3, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: true
              },
              new go.Binding("text", "name").makeTwoWay()),
            // properties
            $(go.TextBlock, "Properties",
              { row: 1, font: "italic 10pt sans-serif" },
              new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("PROPERTIES")),
            $(go.Panel, "Vertical", { name: "PROPERTIES" },
              new go.Binding("itemArray", "properties"),
              {
                row: 1, margin: 3, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightcyan",
                itemTemplate: propertyTemplate
              }
            ),
            // methods
            $(go.TextBlock, "Methods",
            { row: 2, font: "italic 10pt sans-serif" },
            new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("METHODS")),
          $(go.Panel, "Vertical", { name: "METHODS" },
            new go.Binding("itemArray", "methods"),
            {
              row: 2, margin: 3, stretch: go.GraphObject.Fill,
              defaultAlignment: go.Spot.Left, background: "lightcyan",
              itemTemplate: methodTemplate
            }
          ),),
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


      // 

      var templmap = new go.Map<string, go.Node>(); // In TypeScript you could write: new go.Map<string, go.Node>();
      // for each of the node categories, specify which template to use
      templmap.add("class",classtemplate);
      templmap.add("abstractclass",abstractclasstemplate);
      templmap.add("interface",interfacetemplate);

      dia.nodeTemplateMap = templmap;


      // define the Link template
            
      dia.linkTemplateMap.add("generalization",
      $(go.Link,
        { routing: go.Link.AvoidsNodes,
          corner: 10  },
        { relinkableFrom: true, relinkableTo: true },
        $(go.Shape,
          { stroke: "lightskyeblue", strokeWidth: 3}),
        $(go.Shape,
          { toArrow: "Triangle", stroke: "lightskyeblue", strokeWidth: 2,fill:"white" }))); 


      dia.linkTemplateMap.add("association",
      $(go.Link,
        { routing: go.Link.AvoidsNodes,
          corner: 10  },
        { relinkableFrom: true, relinkableTo: true },
        $(go.Shape,
          { stroke: "lightskyeblue", strokeWidth: 3}))); 

      dia.linkTemplateMap.add("realization",
      $(go.Link,
        { routing: go.Link.AvoidsNodes,
          corner: 10  },
        { relinkableFrom: true, relinkableTo: true },
        $(go.Shape,
          { stroke: "lightskyeblue", strokeWidth: 3, strokeDashArray: [8, 4] }),
        $(go.Shape,
          { toArrow: "Triangle", stroke: "lightskyeblue", strokeWidth: 2,fill:"white" }))); 


        // default je general.
        (dia.model as go.GraphLinksModel).setCategoryForLinkData(dia.toolManager.linkingTool.archetypeLinkData,"generalization");
    
        return dia;
  }
// novo
// var nodedata = [
//   {
//     key: 1,
//     name: "BankAccount",
//     properties: [
//       { name: "owner", type: "String", visibility: "public" },
//       { name: "balance", type: "Currency", visibility: "public", default: "0" }
//     ],
//     methods: [
//       { name: "deposit", parameters: [{ name: "amount", type: "Currency" }], visibility: "public" },
//       { name: "withdraw", parameters: [{ name: "amount", type: "Currency" }], visibility: "public" }
//     ]
//   },
//   {
//     key: 11,
//     name: "Person",
//     properties: [
//       { name: "name", type: "String", visibility: "public" },
//       { name: "birth", type: "Date", visibility: "protected" }
//     ],
//     methods: [
//       { name: "getCurrentAge", type: "int", visibility: "public" }
//     ]
//   },
//   {
//     key: 12,
//     name: "Student",
//     properties: [
//       { name: "classes", type: "List<Course>", visibility: "public" }
//     ],
//     methods: [
//       { name: "attend", parameters: [{ name: "class", type: "Course" }], visibility: "private" },
//       { name: "sleep", visibility: "private" }
//     ]
//   },
//   {
//     key: 13,
//     name: "Professor",
//     properties: [
//       { name: "classes", type: "List<Course>", visibility: "public" }
//     ],
//     methods: [
//       { name: "teach", parameters: [{ name: "class", type: "Course" }], visibility: "private" }
//     ]
//   },
//   {
//     key: 14,
//     name: "Course",
//     properties: [
//       { name: "name", type: "String", visibility: "public" },
//       { name: "description", type: "String", visibility: "public" },
//       { name: "professor", type: "Professor", visibility: "public" },
//       { name: "location", type: "String", visibility: "public" },
//       { name: "times", type: "List<Time>", visibility: "public" },
//       { name: "prerequisites", type: "List<Course>", visibility: "public" },
//       { name: "students", type: "List<Student>", visibility: "public" }
//     ]
//   }
// ];
//
  public diagramNodeData: Array<go.ObjectData> = [
    {
      key: 'Klasica',
      name: "BankAccountoOoOoO",
      properties: [
        { name: "owner", type: "String", visibility: "public" },
        { name: "balance", type: "Currency", visibility: "public"}
      ],
      methods: [
        { name: "deposit", type: "void", parameters: [{ name: "amount", type: "Currency" }], visibility: "public" },
        { name: "withdraw", type: "void", parameters: [{ name: "amount", type: "Currency" }], visibility: "public" }
      ],
      category:'class' 
    },
    {
      key: "Klasa2",
      name: "BankAccountO",
      properties: [
        { name: "owner", type: "String", visibility: "public" },
        { name: "balance", type: "Currency", visibility: "public"}
      ],
      methods: [
        { name: "deposit",  type: "void",parameters: [{ name: "amount", type: "Currency" }], visibility: "public" },
        { name: "withdraw",  type: "void",parameters: [{ name: "amount", type: "Currency" }], visibility: "public" }
      ],
      category:'class' 
    }
  ];

  public diagramLinkData: Array<go.ObjectData> =  [
    {
        "key": -1,
        "from": "Klasa2",
        "to": "Klasica",
        "fromPort": "b",
        "toPort": "t",
        "category": "generalization"
    }

]
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
 
           // show visibility or access as a single character at the beginning of each property or method
           function convertVisibility(v) {
            switch (v) {
              case "public": return "+";
              case "private": return "-";
              case "protected": return "#";
              case "package": return "~";
              default: return v;
            }
          }

         // the item template for properties
         var propertyTemplate =
         $(go.Panel, "Horizontal",
           // property visibility/access
           $(go.TextBlock,
             { isMultiline: false, editable: false, width: 12 },
             new go.Binding("text", "visibility", convertVisibility)),
           // property name, underlined if scope=="class" to indicate static property
           $(go.TextBlock,
             { isMultiline: false, editable: true },
             new go.Binding("text", "name").makeTwoWay(),
             new go.Binding("isUnderline", "scope", function (s) { return s[0] === 'c' })),
           // property type, if known
           $(go.TextBlock, "",
             new go.Binding("text", "type", function (t) { return (t ? ": " : ""); })),
           $(go.TextBlock,
             { isMultiline: false, editable: true },
             new go.Binding("text", "type").makeTwoWay()),
           // property default value, if any
           $(go.TextBlock,
             { isMultiline: false, editable: false },
             new go.Binding("text", "default", function (s) { return s ? " = " + s : ""; }))
         );
 
       // the item template for methods
       var methodTemplate =
         $(go.Panel, "Horizontal",
           // method visibility/access
           $(go.TextBlock,
             { isMultiline: false, editable: false, width: 12 },
             new go.Binding("text", "visibility", convertVisibility)),
           // method name, underlined if scope=="class" to indicate static method
           $(go.TextBlock,
             { isMultiline: false, editable: true },
             new go.Binding("text", "name").makeTwoWay(),
             new go.Binding("isUnderline", "scope", function (s) { return s[0] === 'c' })),
           // method parameters
           $(go.TextBlock, "()",
             // this does not permit adding/editing/removing of parameters via inplace edits
             new go.Binding("text", "parameters", function (parr) {
               var s = "(";
               for (var i = 0; i < parr.length; i++) {
                 var param = parr[i];
                 if (i > 0) s += ", ";
                 s += param.name + ": " + param.type;
               }
               return s + ")";
             })),
           // method return type, if any
           $(go.TextBlock, "",
             new go.Binding("text", "type", function (t) { return (t ? ": " : ""); })),
           $(go.TextBlock,
             { isMultiline: false, editable: true },
             new go.Binding("text", "type").makeTwoWay())
         );



      var classtemplate =
      $(go.Node, 'Auto',
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
        $(go.Shape, { fill: "lightyellow" }),
        $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 3, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: true
              },
              new go.Binding("text", "name").makeTwoWay()),
            // properties
            $(go.TextBlock, "Properties",
              { row: 1, font: "italic 10pt sans-serif" },
              new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("PROPERTIES")),
            $(go.Panel, "Vertical", { name: "PROPERTIES" },
              new go.Binding("itemArray", "properties"),
              {
                row: 1, margin: 3, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightyellow",
                itemTemplate: propertyTemplate
              }
            ),
            // methods
            $(go.TextBlock, "Methods",
            { row: 2, font: "italic 10pt sans-serif" },
            new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("METHODS")),
          $(go.Panel, "Vertical", { name: "METHODS" },
            new go.Binding("itemArray", "methods"),
            {
              row: 2, margin: 3, stretch: go.GraphObject.Fill,
              defaultAlignment: go.Spot.Left, background: "lightyellow",
              itemTemplate: methodTemplate
            }
          ),)
      );

      var abstractclasstemplate =
      $(go.Node, 'Auto',
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
        $(go.Shape, { fill: "lightcoral" }),
        $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 3, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: true
              },
              new go.Binding("text", "name").makeTwoWay()),
            // properties
            $(go.TextBlock, "Properties",
              { row: 1, font: "italic 10pt sans-serif" },
              new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("PROPERTIES")),
            $(go.Panel, "Vertical", { name: "PROPERTIES" },
              new go.Binding("itemArray", "properties"),
              {
                row: 1, margin: 3, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightcoral",
                itemTemplate: propertyTemplate
              }
            ),
            // methods
            $(go.TextBlock, "Methods",
            { row: 2, font: "italic 10pt sans-serif" },
            new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("METHODS")),
          $(go.Panel, "Vertical", { name: "METHODS" },
            new go.Binding("itemArray", "methods"),
            {
              row: 2, margin: 3, stretch: go.GraphObject.Fill,
              defaultAlignment: go.Spot.Left, background: "lightcoral",
              itemTemplate: methodTemplate
            }
          ),)
      );

      var interfacetemplate =
      $(go.Node, 'Auto',
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
        $(go.Shape, { fill: "lightcyan" }),
        $(go.Panel, "Table",
            { defaultRowSeparatorStroke: "black" },
            // header
            $(go.TextBlock,
              {
                row: 0, columnSpan: 2, margin: 3, alignment: go.Spot.Center,
                font: "bold 12pt sans-serif",
                isMultiline: false, editable: true
              },
              new go.Binding("text", "name").makeTwoWay()),
            // properties
            $(go.TextBlock, "Properties",
              { row: 1, font: "italic 10pt sans-serif" },
              new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("PROPERTIES")),
            $(go.Panel, "Vertical", { name: "PROPERTIES" },
              new go.Binding("itemArray", "properties"),
              {
                row: 1, margin: 3, stretch: go.GraphObject.Fill,
                defaultAlignment: go.Spot.Left, background: "lightcyan",
                itemTemplate: propertyTemplate
              }
            ),
            // methods
            $(go.TextBlock, "Methods",
            { row: 2, font: "italic 10pt sans-serif" },
            new go.Binding("visible", "visible", function (v) { return !v; }).ofObject("METHODS")),
          $(go.Panel, "Vertical", { name: "METHODS" },
            new go.Binding("itemArray", "methods"),
            {
              row: 2, margin: 3, stretch: go.GraphObject.Fill,
              defaultAlignment: go.Spot.Left, background: "lightcyan",
              itemTemplate: methodTemplate
            }
          ),)
      );

      
      var templmap = new go.Map<string, go.Node>(); // In TypeScript you could write: new go.Map<string, go.Node>();
      // for each of the node categories, specify which template to use
      templmap.add("class", classtemplate);
      templmap.add("abstractclass",abstractclasstemplate);
      templmap.add("interface",interfacetemplate);

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
    { key: 'Class', name:"Class", category:'class', properties:[], methods:[]},
    { key: 'Interface', name:"Interface", category:'interface',properties:[],methods:[]},
    { key: 'AbstractClass', name:"AbstractClass", category:'abstractclass',properties:[],methods:[]}
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

  constructor(  private cdr: ChangeDetectorRef,
                private classService: ClassModelMongoService,
                private classGeneratorService:M2cgeneratorService) { }

  public observedDiagram = null;

  // currently selected node; for inspector
  public selectedNode: go.Node | null = null;

  public ngAfterViewInit() {

    if (this.observedDiagram) return;
    this.observedDiagram = this.myDiagramComponent.diagram;
    this.cdr.detectChanges(); // IMPORTANT: without this, Angular will throw ExpressionChangedAfterItHasBeenCheckedError (dev mode only)

    const appComp: ClassmodelEditorComponent = this;
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
    }
    
  }

  updateLinkCat(){
    console.log("Trenutni type je1111:" + this.typeLink);
    (this.myDiagramComponent.diagram.model as go.GraphLinksModel).setCategoryForLinkData(this.myDiagramComponent.diagram.toolManager.linkingTool.archetypeLinkData,this.typeLink);
    console.log("Trenutni type je22222:" + this.typeLink);
  }
  isError:boolean;
  errorMessage = "";

    // Show the diagram's model in JSON format that the user may edit
     save() {
      this.saveDiagramProperties();  // do this first, before writing to JSON    
      this.bekap = this.myDiagramComponent.diagram.model.toJson();//this.myDiagramComponent.diagram.model.modelData.toJson();
      console.log(this.bekap);

      this.noviClassDocument = JSON.parse(this.bekap);
      this.noviClassDocument.id = this.model.mongoKey;
      
      this.noviClassDocument.lastModifiedBy = window.localStorage.getItem("userName");
      this.noviClassDocument.previousVersionId = this.classDocument.previousVersionId;
      this.noviClassDocument.nextVersionId = this.classDocument.nextVersionId;

      console.log("SALJEM: " + this.noviClassDocument);

      this.classService.createClassModel(this.noviClassDocument).subscribe(data => {
        // ovde ce da bude IF koji ce da proveri data i posalje novi id u methodController(SAVE).

        console.log("Ucitan je model.");
        this.classDocument = data;
        console.log("Prikaz modela. ");
        this.myDiagramComponent.diagram.model = go.Model.fromJson(this.classDocument);
        this.loadDiagramProperties();  // do this after the Model.modelData has been brought into memory

        this.isError = false;
        console.log(data);
      },
      error => {
      

        if(error.status == 409){
        // Conflict error handler hehe
        this.classDocument = error.error;
        this.myDiagramComponent.diagram.model = go.Model.fromJson(this.classDocument);
        this.loadDiagramProperties();  // do this after the Model.modelData has been brought into memory
        this.errorMessage="Doslo je do konflikta!"
        this.isError = true;
        }else{
          console.log(error);
          // Validation error handler hehe
          this.errorMessage=error.error;
          this.isError = true;
  
        }
      });
    }
    generateClassModelMongo:ClassModelMongo;
    generatedShit: any;

    generate(){
      //let sourceData = this.myDiagramComponent.diagram.model.toJson();
      this.generatedShit = this.myDiagramComponent.diagram.model.toJson();
      this.generateClassModelMongo = JSON.parse(this.generatedShit);
      this.classGeneratorService.generateCodeFromModel(this.generateClassModelMongo).subscribe(data =>{
        console.log("Generisan je model.");
      })}

     load() {    
      this.classService.getClassModel(this.model.mongoKey).subscribe(data =>{
        this.isError = false;
        console.log("Ucitan je model.");
        this.classDocument = data;
        console.log(this.classDocument);
        console.log("Prikaz modela. ");
        this.myDiagramComponent.diagram.model = go.Model.fromJson(this.classDocument);
        this.loadDiagramProperties();  // do this after the Model.modelData has been brought into memory
      })
      // this.myDiagramComponent.diagram.model = go.Model.fromJson(this.bekap);
      // this.loadDiagramProperties();  // do this after the Model.modelData has been brought into memory
    }

     saveDiagramProperties() {
      this.myDiagramComponent.diagram.model.modelData.position = go.Point.stringify(this.myDiagramComponent.diagram.position);
      console.log(this.myDiagramComponent.diagram.model.modelData);
    }

     loadDiagramProperties() {
      var pos = this.myDiagramComponent.diagram.model.modelData.position;
      if(pos) this.myDiagramComponent.diagram.initialPosition = go.Point.parse(pos);
    }


    
  }

