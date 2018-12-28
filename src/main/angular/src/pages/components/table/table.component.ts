import { Component, Input, AfterViewInit, Output, EventEmitter } from '@angular/core';

declare let $: any;

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.css']
})
export class TableComponent implements AfterViewInit {
  tableColums: any[] = [];
  tableData: any[] = [];
  tableTitle: string;
  tableForObject: string;
  icon: string;

  @Input()
  set dataArray(data: any[]) {
    this.tableData = data;
    // if ( data.length ) {
    //  Object.keys(data[0]).forEach((element: any, index: any) => {
    //   this.tableColums[index] = element;
    //  });
    // }
  }

  @Input()
  set tableColumn(data: any[]) {
    this.tableColums = data;
  }

  @Input()
  set title(text: string) {
    this.tableTitle = text;
  }

  @Input()
  set iconUrl(text: string) {
    this.icon = text;
  }

  @Input()
  set tableFor(text: string) {
    this.tableForObject = text;
  }

  @Output() showDetail: EventEmitter<boolean> = new EventEmitter<boolean>();

  constructor() {}

  ngAfterViewInit() {
    // $('.table-responsive').perfectScrollbar();
  }

  showInfoInSidePanel(object: any) {
    object['selectedObject'] = this.tableForObject;
    this.showDetail.emit(object);
  }
}
