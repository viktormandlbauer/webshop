function handleFiles(files) {
    const preview = document.getElementById('preview');
    if (files.length > 0) {
        const file = files[0];
        preview.src = URL.createObjectURL(file);
        preview.style.display = 'block';
    } else {
        preview.style.display = 'none';
    }
}

const dropzone = document.getElementById('dropzone');
dropzone.addEventListener('dragover', (event) => {
    event.preventDefault();
    dropzone.classList.add('drag-over');
});

dropzone.addEventListener('dragleave', () => {
    dropzone.classList.remove('drag-over');
});

dropzone.addEventListener('drop', (event) => {
    event.preventDefault();
    dropzone.classList.remove('drag-over');
    const files = event.dataTransfer.files;
    document.getElementById('fileElem').files = files;
    handleFiles(files);
});