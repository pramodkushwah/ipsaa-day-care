import { Component, OnInit, Output, EventEmitter, NgZone, Renderer, Input, ViewChild, ElementRef } from '@angular/core';
import { timer, Subscription, Observable, Subject} from 'rxjs';
import 'rxjs/add/observable/of';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/do';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/delay';

declare let $: any;

export class UploadEvent {
  constructor(
      public textContent: string,
      public files: any[],
      public images: any[]) {
  }
}

export interface FileSystemEntry {
  name: string;
  isDirectory: boolean;
  isFile: boolean;
}

export interface FileSystemFileEntry extends FileSystemEntry {
  isDirectory: false;
  isFile: true;
  file(callback: (file: File) => void): void;
}

export class UploadFile {
  constructor(
      public relativePath: string,
      public fileEntry: FileSystemEntry) {
  }
}

@Component({
  selector: 'app-email-message-directive',
  templateUrl: './email-message-directive.component.html',
  styleUrls: ['./email-message-directive.component.css']
})
export class EmailMessageDirectiveComponent implements OnInit {

  @Input() disableIf = false;
  @Output()
  public onFileDrop: EventEmitter<UploadEvent> = new EventEmitter<UploadEvent>();
  @Output()
  public onFileOver: EventEmitter<any> = new EventEmitter<any>();
  @Output()
  public onFileLeave: EventEmitter<any> = new EventEmitter<any>();

  private keyUpSubject = new Subject<string>();
  globalStart: Function;
  globalDisable: boolean;
  globalEnd: Function;
  dragoverflag: any;
  files: any[] = [];
  images: any[] = [];
  validImageTypes = ['image/gif', 'image/jpeg', 'image/png'];
  subscription: Subscription;

  private fileCopies: any[] = [];
  private imageCopies: any[] = [];

  model = '';

  @ViewChild('dropZone') dropZone: ElementRef;
  textContent: string;
  uploadEvent: UploadEvent;
  constructor(private zone: NgZone,
    private renderer: Renderer) {
    this.globalStart = this.renderer.listen('document', 'dragstart', (evt) => {
      this.globalDisable = true;
    });
    this.globalEnd = this.renderer.listen('document', 'dragend', (evt) => {
      this.globalDisable = false;
    });

    const onKeyUpEvent = this.keyUpSubject
      .debounceTime(250)
      .distinctUntilChanged()
      .flatMap((textContent) => Observable.of(textContent).delay(100)).subscribe(value => {
        this.textContent = value;
        this.uploadEvent = new UploadEvent(this.textContent, this.fileCopies, this.imageCopies);
        this.onFileDrop.emit(this.uploadEvent);
      });
  }

  ngOnInit() {
  }

  public onDragOver(event: Event): void {
    if (!this.globalDisable && !this.disableIf) {
      if (!this.dragoverflag) {
        this.dragoverflag = true;
        this.onFileOver.emit(event);
      }
      this.preventAndStop(event);
    }
  }

  public onDragLeave(event: Event): void {
    if (!this.globalDisable && !this.disableIf) {
      if (this.dragoverflag) {
        this.dragoverflag = false;
        this.onFileLeave.emit(event);
      }
      this.preventAndStop(event);
    }
  }

  dropFiles(event: any) {
    if (!this.globalDisable && !this.disableIf) {
      event.dataTransfer.dropEffect = 'copy';
      let length;
        if (event.dataTransfer.items) {
          length = event.dataTransfer.items.length;
        } else {
          length = event.dataTransfer.files.length;
        }
        for (let i = 0; i < length; i++) {
          let entry: any;
          if (event.dataTransfer.items) {
            if (event.dataTransfer.items[i].webkitGetAsEntry) {
              entry = event.dataTransfer.items[i].webkitGetAsEntry();
            }
          } else {
            if (event.dataTransfer.files[i].webkitGetAsEntry) {
              entry = event.dataTransfer.files[i].webkitGetAsEntry();
            }
          }

          if (!entry) {
            const file: File = event.dataTransfer.files[i];
            if (file) {
              const fakeFileEntry: FileSystemFileEntry = {
                name: file.name,
                isDirectory: false,
                isFile: true,
                file: (callback: (filea: File) => void): void => {
                  callback(file);
                }
              };
              const toUpload: UploadFile = new UploadFile(fakeFileEntry.name, fakeFileEntry);
              this.addToQueue(toUpload);
            }
          } else {
            if (entry.isFile) {
              const toUpload: UploadFile = new UploadFile(entry.name, entry);
              this.addToQueue(toUpload);
            }
          }
        }
      this.preventAndStop(event);
      const timerObservable = timer(200, 200);
      this.subscription = timerObservable.subscribe(t => {
        if (this.files.length > 0 || this.images.length > 0) {
          this.uploadEvent = new UploadEvent(this.textContent, this.files, this.images);
          this.onFileDrop.emit(this.uploadEvent);
          this.files = [];
          this.images = [];
        }
      });
    }
  }

  private addToQueue(file: UploadFile) {

    this.files = this.fileCopies;
    this.images = this.imageCopies;
    const fileEntry = file.fileEntry as FileSystemFileEntry;
    const reader = new FileReader();
    const image = document.createElement('img');
    reader.onload = function (e: any) {
      image.setAttribute('src', e.target.result);
    };

    fileEntry.file(fileData => {
      if (this.validImageTypes.indexOf(fileData.type) === -1) {
        this.files.push(fileData);
      } else {
        this.images.push(fileData);
        image.setAttribute('id', 'img' + new Date().getTime());
        image.setAttribute('name', fileData.name);
        image.setAttribute('type', fileData.type);
        image.setAttribute('height', '200' );
        image.setAttribute('width', '150');

        this.dropZone.nativeElement.append(image);
      }
      reader.readAsDataURL(fileData);
    });
    this.fileCopies = this.files;
    this.imageCopies = this.images;
  }

  private preventAndStop(event) {
    event.stopPropagation();
    event.preventDefault();
  }

  getElements(event) {
    this.preventAndStop(event);
    if (event.key === 'Backspace') {
      const imgs = document.getElementById('dropZone').getElementsByTagName('img');
      const result = Object.keys(imgs).map(function (key) {
        return { [key]: imgs[key] };
      });
      this.imageCopies = this.imageCopies.filter(image => {
          return result.find(element => element['0'].name == image.name);
        });
      this.uploadEvent = new UploadEvent(this.textContent, this.fileCopies, this.imageCopies);
      this.onFileDrop.emit(this.uploadEvent);
    }
  }

}
