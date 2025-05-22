document.addEventListener("DOMContentLoaded", function () {
    Dropzone.autoDiscover = false;

    const dropzone = new Dropzone("div.my-dropzone", {
        url: "form-data", // Wird nicht verwendet
        paramName: "imageFile", // Parametername
        maxFiles: 1, // Nur ein Bild zulassen
        acceptedFiles: "image/*", // Nur Bilder erlauben
        dictDefaultMessage: "Ziehe ein Bild hierher oder klicke, um es hochzuladen.",
        autoProcessQueue: false, // Verhindert automatisches Hochladen
        init: function () {
            this.on("maxfilesexceeded", function (file) {
                this.removeFile(file); // Entfernt zusätzliche Dateien
                alert("Es kann nur ein Bild hochgeladen werden.");
            });
        }
    });
});