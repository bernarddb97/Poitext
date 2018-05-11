namespace app.controller {
  "use strict";

  /**
   * 共通PDFファイルアップロードのCtrl
   */
  export class MyPdfUploadCtrl {

    selFile: any;
    fileFullName: string;
    pdfFileName: string;
    static $inject = ["$http", "$timeout", "MessageService", "$mdDialog", "tblId"];
    constructor(private $http: ng.IHttpService
      , private $timeout: ng.ITimeoutService
      , private messageService: app.service.MessageService
      , private $mdDialog: ng.material.IDialogService
      , private tblId: string) {
    }

    /**
     * アップロードを取り消し
     * @return {void}
     */
    cancel() {
      this.$mdDialog.cancel();
    }

    onChange(selFile: any) {
      this.selFile = undefined;
      this.fileFullName = undefined;
      this.pdfFileName = undefined;
      let fileExtension: string;
      if (selFile !== undefined) {
        const fileName: string = selFile.name;
        if (fileName.lastIndexOf(".") > 0) {
          fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
      }
      if (fileExtension === ".doc" || fileExtension === ".docx") {
        const formData = new FormData();
        formData.append("file", selFile);
        this.$http.post<app.model.CommonResponse<string>>("/C01P005/selectFileFullName", formData, {
          transformRequest: null,
          headers: { "Content-type": undefined }
        }).then((response) => {
          this.fileFullName = response.data.data;
          this.selFile = selFile;
        }).catch(() => {
        });
      }
    }

    upload() {
      if (this.selFile === undefined) {
        this.messageService.showAlert(this.messageService.formatMessage("M.ERR.COM.needSelect", "upload file"));
      } else {
        const formData = new FormData();
        formData.append("name", this.selFile.name);
        formData.append("tblId", this.tblId);
        formData.append("file", this.selFile);
        angular.element(event.srcElement).attr("disabled", "true");
        this.$http.post("/C01P005/uploadWord", formData, {
          transformRequest: null,
          headers: { "Content-type": undefined }
        }).then((response) => {
          this.$mdDialog.hide(response.data);
        }).catch(() => {

        });
      }
    }

  }

  main.controller("MyPdfUploadCtrl", MyPdfUploadCtrl);
}
