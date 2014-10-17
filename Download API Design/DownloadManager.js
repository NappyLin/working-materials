[NoInterfaceObject] interface DownloadManager{
	
	Promise<DownloadItem> createDownload( sourcrUrl, targetFile);
	
	Promise<Array<DownloadItem>> getAll();
	promise<DownloadItem> getDownloadById(id);
	promise<Array<DownloadItem>> getDownloadsByState( state );

	Promise removeDownloads( idArray );
	promise removeAll();
};