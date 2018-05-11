var app;
(function (app) {
    var controller;
    (function (controller) {
        "use strict";
        /**
         * 共通PDFファイルアップロードのCtrl
         */
        var MyPdfUploadCtrl = (function () {
            function MyPdfUploadCtrl($http, $timeout, messageService, $mdDialog, tblId) {
                this.$http = $http;
                this.$timeout = $timeout;
                this.messageService = messageService;
                this.$mdDialog = $mdDialog;
                this.tblId = tblId;
            }
            /**
             * アップロードを取り消し
             * @return {void}
             */
            MyPdfUploadCtrl.prototype.cancel = function () {
                this.$mdDialog.cancel();
            };
            MyPdfUploadCtrl.prototype.onChange = function (selFile) {
                var _this = this;
                this.selFile = undefined;
                this.fileFullName = undefined;
                this.pdfFileName = undefined;
                var fileExtension;
                if (selFile !== undefined) {
                    var fileName = selFile.name;
                    if (fileName.lastIndexOf(".") > 0) {
                        fileExtension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                    }
                }
                if (fileExtension === ".doc" || fileExtension === ".docx") {
                    var formData = new FormData();
                    formData.append("file", selFile);
                    this.$http.post("/C01P005/selectFileFullName", formData, {
                        transformRequest: null,
                        headers: { "Content-type": undefined }
                    }).then(function (response) {
                        _this.fileFullName = response.data.data;
                        _this.selFile = selFile;
                    }).catch(function () {
                    });
                }
            };
            MyPdfUploadCtrl.prototype.upload = function () {
                var _this = this;
                if (this.selFile === undefined) {
                    this.messageService.showAlert(this.messageService.formatMessage("M.ERR.COM.needSelect", "upload file"));
                }
                else {
                    var formData = new FormData();
                    formData.append("name", this.selFile.name);
                    formData.append("tblId", this.tblId);
                    formData.append("file", this.selFile);
                    angular.element(event.srcElement).attr("disabled", "true");
                    this.$http.post("/C01P005/uploadWord", formData, {
                        transformRequest: null,
                        headers: { "Content-type": undefined }
                    }).then(function (response) {
                        _this.$mdDialog.hide(response.data);
                    }).catch(function () {
                    });
                }
            };
            return MyPdfUploadCtrl;
        }());
        MyPdfUploadCtrl.$inject = ["$http", "$timeout", "MessageService", "$mdDialog", "tblId"];
        controller.MyPdfUploadCtrl = MyPdfUploadCtrl;
        app.main.controller("MyPdfUploadCtrl", MyPdfUploadCtrl);
    })(controller = app.controller || (app.controller = {}));
})(app || (app = {}));
//# sourceMappingURL=MyPdfUploadCtrl.js.map