package com.hannibal.scalpel.task;


import com.hannibal.scalpel.bean.DiseasedTissueBean;

import java.io.File;
import java.util.List;

public class PhotoUploadingWorker extends TaskUploadWorkerBase {
	
	private boolean reportFaultsInfo(DiseasedTissueBean rescueTask) {
		
		VehicleFaultInfoSubmit vehicleFaultsReport = new VehicleFaultInfoSubmit();
		vehicleFaultsReport.setContext(getContext());
		HttpResult<WebApiResponse> result = vehicleFaultsReport.doFaultInfoSubmit(rescueTask);
		
		if (result == null || result.getResponse() == null) {
			return false;
		} else if (result.getResponse().ReturnCode == 0) {
			return true;
		} else {
			WebApiServerException serverError = new WebApiServerException(result.getResponse().Message, getContext());
			serverError.setCaseNumber(rescueTask.getCaseNumber());
			ExceptionsHandlingService.handleException(serverError);
			return false;
		}
	}
	
	private boolean isReturnMessageIndicatingDuplicatedUpload(String message) {
		return StringExtensions.isNotNullAndEmpty(message)
					&& message.contains(getContext().getResources().getString(R.string.data_hasexist));	
	}
	
	public boolean uploadToServer(PhotoUploadArg arg) {
		
		HttpResult<WebApiResponse> data = null;
		
		RescueTaskStatusUpdate taskStatusUpdate = new RescueTaskStatusUpdate();
		
		taskStatusUpdate.setContext(getContext());
		
		try {
			
			data = taskStatusUpdate.doUpdate(arg);
			
		} catch (Exception excetion) {
			
			ExceptionsHandlingService.handleException(excetion);
		}

		if (data == null || data.getResponse() == null) {
			
			return false;
			
		} else if (data.getResponse().ReturnCode == 0 || isReturnMessageIndicatingDuplicatedUpload(data.getResponse().Message)) {
			
			return true;
			
		} else {
			
			WebApiServerException serverError = new WebApiServerException(data.getResponse().Message, getContext());
			serverError.setCaseNumber(arg.getTaskId());
			ExceptionsHandlingService.handleException(serverError);
			return false;
		}
	}
	
	private boolean uploadRescuePhoto(DiseasedTissueBean taskPhoto) {
		
		int photoType = taskPhoto.getPhotoType();
		
		byte[] photoData = null;
		String photoFilename = null;
		String pathname = taskPhoto.getPhotoPath();
		
		if (StringExtensions.isNotNullAndEmpty(pathname)) {
				
			if (!Constants.FakeImage.equals(pathname)) {
				
				File photoFile = new File(pathname);
				
				if (photoFile.exists()) {
					
					try {
						
						photoData = FileUtils.readFileToByteArray(photoFile);
						
						photoFilename = photoFile.getName();
						
					} catch (Exception e) {
						ExceptionsHandlingService.handleException(e);
					}
					
				} else {
					// fuck , nothing is impossible. photos have gone   . 
					ExceptionsHandlingService.handleException(new WebApiServerException(pathname, getContext()), true);
				}
			}
		}
		
		RescueTaskDto rescueTask = taskPhoto.getRescueTask();
		
		Double distance = null;
		
		if (photoType == RescueTaskStatus.ArrivedAtRescueSite.getStatusId()) {
			
			distance = rescueTask.getArriveSceneDistance();
			
		} else if (photoType == RescueTaskStatus.ArrivedAt4SShop.getStatusId()) {
			
			distance = rescueTask.getArrive4SDistance();
		}
		
		PhotoUploadArg arg = new PhotoUploadArg();
		arg.setContext(getContext());
		arg.setTaskId(rescueTask.getTaskId());
		arg.setTaskStatus(RescueTaskStatus.getById(photoType));
		arg.setRecordingTime(taskPhoto.getRecordingTime());
		arg.setFilename(photoFilename);
		arg.setPhotoData(photoData);
		arg.setLatitude(taskPhoto.getLatitude());
		arg.setLongitude(taskPhoto.getLongitude());
		
		if (distance != null) {
			
			arg.setDistance(distance);
		}
		
		return uploadToServer(arg);
	}

	@Override protected void _doUpload(RescueTaskDto rescueTask) {

		List<RescueTaskPhoto> photosToBeUploaded = TaskPhotoDaoWrapper.getPhotosToBeUploaded(getContext(), rescueTask.getId());
		
		PhotoUploadProgress progressArg = new PhotoUploadProgress();
		progressArg.setCurrentPhotoIndex(0);
		progressArg.setTotoalNumberOfPhotosToUpload(1);
		
		boolean retryCounterIncremented = false;
		
		if (photosToBeUploaded != null && photosToBeUploaded.size() > 0) {
			
			if (getProgressListener() != null) {
				progressArg.setTotoalNumberOfPhotosToUpload(photosToBeUploaded.size() + 1);
				progressArg.setCurrentPhotoIndex(0);
				getProgressListener().onProgressUpdated(progressArg);
			}
			
			Boolean hadUpload = UploadFailedTaskDaoExtensions.isUploadFailed(getContext(), rescueTask);
			
			if (RescueTaskStatus.Internal_Finished == rescueTask.getStatus() && 
																	!hadUpload &&
																	hasUploadFailedTaskPhotos(photosToBeUploaded)) {
				
				UploadFailedTaskDaoExtensions.createUploadFailedOrIncrementRetryCount(getContext(), rescueTask);
				
				retryCounterIncremented = true;
			}
			
			for (RescueTaskPhoto photo: photosToBeUploaded) {
				
				photo.setRescueTask(rescueTask);
				
				boolean photoUploaded = uploadRescuePhoto(photo);
				
				if (photoUploaded) { 
					
					photo.setIsUploadFailed(0);
					photo.setStatus(Constants.PhotoStatus_Uploaded);
					
					boolean isSucceeded = TaskPhotoDaoWrapper.updateRescueTaskPhoto(context, photo);
					
					int count = 0;
					
					while (count < 5 && !isSucceeded) {
						
						count ++;
						
						isSucceeded = TaskPhotoDaoWrapper.updateRescueTaskPhoto(context, photo);
					}
					
				} else {
					
					photo.setIsUploadFailed(1);
					
					boolean isSucceeded = TaskPhotoDaoWrapper.updateRescueTaskPhoto(context, photo);
					
					int count = 0;
					
					while (count < 5 && !isSucceeded) {
						
						count ++;
						
						isSucceeded = TaskPhotoDaoWrapper.updateRescueTaskPhoto(context, photo);
					}
				}
				
				if (getProgressListener() != null) {
					
					progressArg.setCurrentPhotoIndex(progressArg.getCurrentPhotoIndex() + 1);
					
					if (!photoUploaded) {
						progressArg.setUploadPhotoFailed(new BooleanValue(!photoUploaded));
					}
					
					getProgressListener().onProgressUpdated(progressArg);
					
					if (!photoUploaded) {
						return;
					}
				}
			}
		}
		
		if (getProgressListener() != null) {
			progressArg.setCurrentPhotoIndex(progressArg.getTotoalNumberOfPhotosToUpload());
			getProgressListener().onProgressUpdated(progressArg);
		}
		
		boolean hasUploadFailed = hasUploadFailedTaskPhotos(photosToBeUploaded);
		
		if (rescueTask.getTaskSource() == Constants.TaskSource_PingAn && 
					rescueTask.getStatus() == RescueTaskStatus.Internal_Finished) {
			
			
			if (hasUploadFailed) {
				
				UploadFailedTaskDaoExtensions.createUploadFailedOrIncrementRetryCount(getContext(), rescueTask);
				
			} else {
				
				cleanRescueTask(rescueTask);
			}
			
			if (getProgressListener() != null) {
				progressArg.setUploadPhotoFailed(new BooleanValue(hasUploadFailed));
				getProgressListener().onProgressUpdated(progressArg);
			}
			
		} else if (rescueTask.getTaskSource() == Constants.TaskSource_JLR &&
										RescueTaskStatus.Internal_Finished == rescueTask.getStatus() && 
										!hasUploadFailed) {
			
			boolean reportFaultsInfoSucceeded = reportFaultsInfo(rescueTask);
			
			if (reportFaultsInfoSucceeded) {
				
				cleanRescueTask(rescueTask);
				
			} else if (!retryCounterIncremented) {
				
				UploadFailedTaskDaoExtensions.createUploadFailedOrIncrementRetryCount(getContext(), rescueTask);
			}
			
			if (getProgressListener() != null) {
				progressArg.setUploadPhotoFailed(new BooleanValue(!reportFaultsInfoSucceeded));
				getProgressListener().onProgressUpdated(progressArg);
			}
			
		} else if (getProgressListener() != null) {
			progressArg.setUploadPhotoFailed(new BooleanValue(hasUploadFailed));
			getProgressListener().onProgressUpdated(progressArg);
		}
	}
}
