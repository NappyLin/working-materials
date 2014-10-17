// Copyright (c) 2013 Intel Corporation. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.
var AllowDownloadOnNetworkType = {
  'ALL': 0,
  'CELLULAR': 1,
  'WIFI': 2
}; 

var g_next_async_call_id = 0;
var g_async_calls = [];
var g_listeners = [];

// Preserve 6 spaces to hold onStart, onPause, onResume,onCancel,
// getDownloadRequest,getNetworkType, getMIMEType, getState
// callback functions.
var g_next_async_call_id = 8;

function AsyncCall(resolve, reject) {
  this.resolve = resolve;
  this.reject = reject;
}

function createPromise(msg) {
  var promise = new Promise(function(resolve, reject) {
    g_async_calls[g_next_async_call_id] = new AsyncCall(resolve, reject);
  });
  msg.asyncCallId = g_next_async_call_id;
  extension.postMessage(JSON.stringify(msg));
  ++g_next_async_call_id;
  return promise;
}

var _sendSyncMessage = function(msg) {
return extension.internal.sendSyncMessage(JSON.stringify(msg));
};
//get the download request through the downloadId
exports.getDownloadRequest = function(downloadId) {
  var msg = {
    'cmd': 'download_getDownloadRequest',
    'data': {
      'downloadId':downloadId
    }
  };
  return createPromise(msg);
};

//get the networkType
exports.getNetworkType = function(downloadId,callback) {
  var msg = {
    'cmd': 'download_getNetworkType ',
    'data': {
      'downloadId':downloadId
    }
  };
  return createPromise(msg);
};

//get the MIMEType
exports.getMIMEType = function(downloadId){
	var msg = {
		'cmd': 'download_getMIMEType',
		'data' :{
			'downloadId' : downloadId
		}
	};
}

//get the state
exports.getState = function(downloadId){
  var msg = {
    'cmd': 'download_getState',
    'data' :{
      'downloadId' : downloadId
    }
  };
}