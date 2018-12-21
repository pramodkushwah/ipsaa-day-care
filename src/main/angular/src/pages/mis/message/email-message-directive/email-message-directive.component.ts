import { Component, OnInit, Output, EventEmitter, NgZone, Renderer, Input, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { timer, Subscription, Observable, Subject } from 'rxjs';
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
export class EmailMessageDirectiveComponent implements OnInit, AfterViewInit {

  @Input() disableIf = false;
  @Output() onFileDrop: EventEmitter<any> = new EventEmitter<any>();
  // @Output()
  // public onFileOver: EventEmitter<any> = new EventEmitter<any>();
  // @Output()
  // public onFileLeave: EventEmitter<any> = new EventEmitter<any>();

  public keyUpSubject = new Subject<string>();
  globalStart: Function;
  globalDisable: boolean;
  globalEnd: Function;
  dragoverflag: any;
  files: any[] = [];
  images = {};
  validImageTypes = ['image/gif', 'image/jpeg', 'image/png'];
  subscription: Subscription;

  public fileCopies: any[] = [];
  public imageCopies: any[] = [];

  model = '';

  @ViewChild('dropZone') dropZone: ElementRef;
  textContent: string;
  uploadEvent: UploadEvent;
  count: any = 0;
  attachments: any;
  constructor(public zone: NgZone,
    public renderer: Renderer) {
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
        // this.textContent = value;
        this.textContent = $('#dropZone').innerText;
        this.uploadEvent = new UploadEvent(this.textContent, this.fileCopies, this.imageCopies);
        this.onFileDrop.emit(this.uploadEvent);
      });
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    document.getElementById('message').addEventListener('paste', (event) => {
      const pastedData: any = event.clipboardData.items[0];
      if (pastedData.type.indexOf('image') === 0) {
        this.composeThumbnail(pastedData.getAsFile()); // this still works!
      }
    });
  }


  // public onDragOver(event: Event): void {
  //   if (!this.globalDisable && !this.disableIf) {
  //     if (!this.dragoverflag) {
  //       this.dragoverflag = true;
  //       this.onFileOver.emit(event);
  //     }
  //     this.preventAndStop(event);
  //   }
  // }

  // public onDragLeave(event: Event): void {
  //   if (!this.globalDisable && !this.disableIf) {
  //     if (this.dragoverflag) {
  //       this.dragoverflag = false;
  //       this.onFileLeave.emit(event);
  //     }
  //     this.preventAndStop(event);
  //   }
  // }

  // dropFiles(event: any) {
  //   if (!this.globalDisable && !this.disableIf) {
  //     event.dataTransfer.dropEffect = 'copy';
  //     let length;
  //       if (event.dataTransfer.items) {
  //         length = event.dataTransfer.items.length;
  //       } else {
  //         length = event.dataTransfer.files.length;
  //       }
  //       for (let i = 0; i < length; i++) {
  //         let entry: any;
  //         if (event.dataTransfer.items) {
  //           if (event.dataTransfer.items[i].webkitGetAsEntry) {
  //             entry = event.dataTransfer.items[i].webkitGetAsEntry();
  //           }
  //         } else {
  //           if (event.dataTransfer.files[i].webkitGetAsEntry) {
  //             entry = event.dataTransfer.files[i].webkitGetAsEntry();
  //           }
  //         }

  //         if (!entry) {
  //           const file: File = event.dataTransfer.files[i];
  //           if (file) {
  //             const fakeFileEntry: FileSystemFileEntry = {
  //               name: file.name,
  //               isDirectory: false,
  //               isFile: true,
  //               file: (callback: (filea: File) => void): void => {
  //                 callback(file);
  //               }
  //             };
  //             const toUpload: UploadFile = new UploadFile(fakeFileEntry.name, fakeFileEntry);
  //             this.addToQueue(toUpload);
  //           }
  //         } else {
  //           if (entry.isFile) {
  //             const toUpload: UploadFile = new UploadFile(entry.name, entry);
  //             this.addToQueue(toUpload);
  //           }
  //         }
  //       }
  //     this.preventAndStop(event);
  //     const timerObservable = timer(200, 200);
  //     this.subscription = timerObservable.subscribe(t => {
  //       if (this.files.length > 0 || this.images.length > 0) {
  //         this.textContent = $('#dropZone').innerText;
  //         this.uploadEvent = new UploadEvent(this.textContent, this.files, this.images);
  //         this.onFileDrop.emit(this.uploadEvent);
  //         this.files = [];
  //         this.images = [];
  //       }
  //     });
  //   }
  // }

  // public addToQueue(file: UploadFile) {

  //   this.files = this.fileCopies;
  //   this.images = this.imageCopies;
  //   const fileEntry = file.fileEntry as FileSystemFileEntry;
  //   const reader = new FileReader();
  //   const image = document.createElement('img');
  //   reader.onload = function (e: any) {
  //     image.setAttribute('src', e.target.result);
  //   };

  //   fileEntry.file(fileData => {
  //     if (this.validImageTypes.indexOf(fileData.type) === -1) {
  //       this.files.push(fileData);
  //     } else {
  //       this.images.push(fileData);
  //       image.setAttribute('id', 'img' + new Date().getTime());
  //       image.setAttribute('name', fileData.name);
  //       image.setAttribute('type', fileData.type);
  //       image.setAttribute('height', '100%' );
  //       image.setAttribute('width', '100%');

  //       this.dropZone.nativeElement.append(image);
  //     }
  //     reader.readAsDataURL(fileData);
  //   });
  //   this.fileCopies = this.files;
  //   this.imageCopies = this.images;
  // }

  public preventAndStop(event) {
    event.stopPropagation();
    event.preventDefault();
  }

  // getElements(event) {
  //   this.preventAndStop(event);
  //   if (event.key === 'Backspace') {
  //     const imgs = document.getElementById('dropZone').getElementsByTagName('img');
  //     const result = Object.keys(imgs).map(function (key) {
  //       return { [key]: imgs[key] };
  //     });
  //     this.imageCopies = this.imageCopies.filter(image => {
  //         return result.find(element => element['0'].name === image.name);
  //       });
  //       this.textContent = $('#dropZone').innerText;
  //     this.uploadEvent = new UploadEvent(this.textContent, this.fileCopies, this.imageCopies);
  //     this.onFileDrop.emit(this.uploadEvent);
  //   }
  // }



  composeThumbnail(file) {
    if (!/^image\//.test(file.type)) {
      console.log('ERROR: Not an image file.');
      return false;
    }

    // compose an <img> for the thumbnail
    let thumbnailImage: any = document.createElement('img');
    thumbnailImage.setAttribute('cid', this.generateCid());
    thumbnailImage.setAttribute('style', 'width:300px');

    thumbnailImage.file = file;
    // document.getElementById('message').appendChild(thumbnailImage);
    const range = window.getSelection().getRangeAt(0);
    range.deleteContents();
    range.insertNode(thumbnailImage);

    this.images[this.getCurrentCid()] = file;

    const reader = new FileReader();
    reader.onload = function (event) {
      thumbnailImage.src = this.result;
    };
    reader.readAsDataURL(file);
    this.createObject();
  }

  generateCid() {
    let val = 'inline_image-';
    this.count++;
    val = val + this.count;
    return val;
  }

  getCurrentCid() {
    let val = 'inline_image-';
    val = val + this.count;
    return val;
  }

  postobject = {
    cids: [],
    emailcontent: '',
    images: [],
  };

  createObject() {

    // 1. removing url image from img
    // 2. putting image and their cid in postobject.images and postobject.cids
    const message = $('#message').clone();
    console.log(message);

    const imgs = message.find('img');
    for (let i = 0; i < imgs.length; i++) {
      const img = $(imgs[i]);
      const cid = img.attr('cid');
      img.removeAttr('src');
      img.attr('src', 'cid:' + cid);
      if (typeof this.images[cid] !== 'undefined') {
        this.postobject.cids.push(cid);
        this.postobject.images.push(this.images[cid]);
      }
    }
    this.postobject.emailcontent = message.html();
    // for (let i = 0; i < this.attachments.length; i++) {
    //   if (this.attachments[i].size < (1024 * 1024 * MAX_FILE_BYTES)) {
    //     this.postobject.files.push(this.attachments[i]);
    //   }
    //   else {
    //     error("Attachment size must less then " + MAX_FILE_SIZE + " MB");
    //     return;
    //   }
    // }
    this.onFileDrop.emit(this.postobject);
  }


  getElements(event) {
    this.createObject();
  }

}
