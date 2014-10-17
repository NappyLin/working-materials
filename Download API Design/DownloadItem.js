
enum DownloadState {"running","paused","canceled","failed","successful"};

enum DownloadError {"unknownError","fileAlreadyExistError","deviceNotFoundError",
                    "httpDataError","insufficientSpaceError","unhandledHttpError",
                    "cannotResumError","invalidValuesError","typeMismatchError",
                    "notFoundError"};
                    
[NoInterfaceObject] interface DownloadItem{
	readonly attribute unsigned long id;
	readonly attribute String sourceUrl;
	readonly attribute String targetFile;
	readonly attribute Date startTime;
	readonly attribute DownloadState state;
	readonly attribute DownloadError error;
	readonly attribute long totalBytes;
	readonly attribute String MIMEType;
	         attribute EventHandler onPause;
	         attribute EventHandler onResume;
	         attribute EventHandler onCancel;
	         attribute EventHandler onProgress;

	Promise start();

	Promise pause();
	
	Promise resume();
	
	Promise cancel();
};