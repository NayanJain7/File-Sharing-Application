const dropZone = document.querySelector(".drop-zone");
const fileInput = document.querySelector("#fileInput");
const browseBtn = document.querySelector(".browseBtn");

const bgProgress = document.querySelector(".bg-progress");
const progressContainer = document.querySelector(".progress-container");
const uploadContainer = document.querySelector(".upload-container");
const percentDiv = document.querySelector("#percent");
const progressBar = document.querySelector(".progress-bar");
const fileURL = document.querySelector("#fileURL");
const copyBtn = document.querySelector("#copyBtn");
const sharingContainer = document.querySelector(".sharing-container");
const emailForm = document.querySelector("#emailForm");
const emailContainer = document.querySelector(".email-container");
const toast = document.querySelector(".toast");

const maxFileSixe =  100 * 1024 * 1024;



const uploadUrl = "http://localhost:8080/upload-single-file";
const emailUrl = "http://localhost:8080/upload-file/send";

dropZone.addEventListener("dragover",(e) => {
    
    e.preventDefault();

    if(!dropZone.classList.contains("dragged")){
        dropZone.classList.add("dragged");
    }
});

dropZone.addEventListener("dragleave", (e) => {
    dropZone.classList.remove("dragged");
});

dropZone.addEventListener("drop",(e) => {
    e.preventDefault();

    dropZone.classList.remove("dragged");
   
    const files = e.dataTransfer.files;

    if(files.length){
        fileInput.files = files;
        console.log(files)
        uploadFile();
    }
});

browseBtn.addEventListener("click",(e) => {
    fileInput.click()
});

fileInput.addEventListener("change", (e) => {
    uploadFile();

});

copyBtn.addEventListener("click",(e) => {

    fileURL.select();
    document.execCommand("copy");
    showToast("Link copied","80")
});


const uploadFile = () => {

    if(fileInput.files.length>1){
        resetFileInput();
        showToast("Only upload 1 file!","80");
        return;
    }

    const size =fileInput.files[0].size;

    if(size>maxFileSixe){
        showToast("Can't upload more than 100MB","80");
        resetFileInput();
        return;
    }

    progressContainer.style.display = "block";
    uploadContainer.style.marginTop="15%";

    const files = fileInput.files[0];
    var formData = new FormData();
    formData.append("file", files);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", uploadUrl);

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
          //console.log("Line no. 58 " + response.fileDownloadUri);

          showLink(response);
            
        } else {
           console.log("Some Error Occurred");
        }
    }

    xhr.upload.onprogress = updateProgress;

    xhr.upload.onerror = () =>{
        resetFileInput()
        showToast(`Error in upload: ${xhr.statusText}`,"80");
    }

    xhr.send(formData);

};

    const resetFileInput = () => {
        fileInput.value="";
    }

    const updateProgress = (e) => {

        const percent = Math.round(e.loaded/e.total)*100;
        
        bgProgress.style.width=`${percent}%`;
        percentDiv.innerText = percent;
        percentDiv.innerText = percent;
        progressBar.style.transform = `scaleX(${percent/100})`;

         console.log(e);
    }

    const showLink = (response) => {

        emailForm[2].removeAttribute("disabled");
        resetFileInput()
        
        progressContainer.style.display = "none";
        sharingContainer.style.display = "block";
        emailContainer.style.display = "block";
        fileURL.value = response.shareLink;

    }
    emailForm.addEventListener("submit",(e) => {
        e.preventDefault();
		const url=fileURL.value;
        const formData = {
        
        	uuid:url.split("/")[4],
            emailTo: emailForm.elements["reciver"].value, 
            emailFrom:emailForm.elements["sender"].value,
        };

        emailForm[2].setAttribute("disabled","true");

        fetch(emailUrl, {
            method:"POST",
            headers:{
                "Content-Type":"application/json"
            },
            body:JSON.stringify(formData)
        }).then(res => res.json())
        .then((data) => {
            if(data){

                showToast("Email Sent","120");
            
            }
            else{
                showToast("Email not sent, try again!!","120");
                
            }
           
        });
        
    });

    let toastTimer
    const showToast = (msg,setTop) => {

        
        toast.innerText=msg;
        toast.style.display="block";
        toast.style.top=`${setTop}vh`;
       
        clearTimeout(toastTimer)
        toastTimer = setTimeout(() => {
        toast.style.display="none";
        },2000);
       
       
    };