
var url = "http://www.download.com/example.html";
var targetFile = "application/com/trinea/download/file/example.html";

var downloadItem;

DownloadManager.createDownload(url, tagetFile). then( function (item) {
          // create the download successfully
          downloadItem = item;
          //start the download task
          downloadItem.start().then(function(){
          	    //start the download successfully
          	    //console output the log
          	    console.log(downlaodItem.startTime);
          	    console.log(downlaodItem.MIMEType);

          	    //add event listener to pause
          	    downloadItem.addEventListener('pause', function() {
          	    	console.log(this.state);
          	    });

          	    //if the download task has been paused successfully then resume it.
          	    if(downloadItem.state == "paused"){
          	    	downloadItem.onResume = function(){
                         console.log(this.state);
                    }
          	    }
          	    // cancel the download task
          	    downloadItem.cancel();

          },function(error){
          	    //fail to start the download task
          	    console.log(downlaodItem.error);
          	    console.log(downlaodItem.state);
          });
}, function (error){ 
         //fail to create the download 
});


