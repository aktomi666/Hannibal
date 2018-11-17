package com.hannibal.scalpel.task;

import android.content.Context;

import com.controlexpert.allianzRescue.exceptions.WebApiServerException;
import com.controlexpert.allianzRescue.listeners.PhotoUploadingProgressListener;
import com.controlexpert.allianzRescue.model.RescueTaskDto;
import com.controlexpert.allianzRescue.model.RescueTaskPhoto;
import com.controlexpert.allianzRescue.model.UploadingTaskInfo;
import com.controlexpert.allianzRescue.persistence.RescueTaskDaoExtensions;
import com.controlexpert.allianzRescue.persistence.UploadingTaskDaoExtensions;
import com.controlexpert.allianzRescue.pojo.BooleanValue;
import com.controlexpert.allianzRescue.pojo.PhotoUploadProgress;
import com.controlexpert.allianzRescue.utils.RescueTaskPhotoManager;
import com.hannibal.scalpel.bean.DiseasedTissueBean;

import java.util.List;

public abstract class TaskUploadWorkerBase {
	
	/**
	 * Eight minutes
	 */
	public static final int MinimalUploadingInterval = 8 * 60 * 1000;
	
	private PhotoUploadingProgressListener progressListener;
	
	private String executorName;
	
	protected Context context;
	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	
	public String getExecutorName() {
		return executorName;
	}
	
	public void setExecutorName(String executorName) {
		this.executorName = executorName;
	}
	
	protected abstract void _doUpload(DiseasedTissueBean rescueTask);
	
	public void doUpload(int rescueTaskId) {

		DiseasedTissueBean rescueTask = RescueTaskDaoExtensions.getRescueTask(getContext(), rescueTaskId);
		
		if (rescueTask != null) {
			
			doUpload(rescueTask);
			
		} else {
			
			WebApiServerException exception = new WebApiServerException("RESCUETASK IS NULL", getContext());
			
			ExceptionsHandlingService.handleException(exception, true);
			
			reportProgress();
		}
	}
	
	/**
	 * Call this method to report to caller that 
	 * 		summit information about given task is not allowed at this moment.
	 */
	private void reportProgress() {
		
		PhotoUploadingProgressListener listener = getProgressListener();
		
		if (listener != null) {
			
			PhotoUploadProgress progress = new PhotoUploadProgress();
			progress.setCurrentPhotoIndex(1);
			progress.setTotoalNumberOfPhotosToUpload(1);
			progress.setUploadPhotoFailed(new BooleanValue(true));
			
			listener.onProgressUpdated(progress);
		}
	}
	
	public void doUpload(DiseasedTissueBean rescueTask) {
		
		UploadingTaskInfo uploadingTask = null;
		
		try {
			
			long currentTime = System.currentTimeMillis();
			
			uploadingTask = UploadingTaskDaoExtensions.getUploadingTaskInfo(getContext(), rescueTask.getId());
			
			if (uploadingTask != null) {
				
				boolean canUpload = false;
				
				if (!uploadingTask.isFinished()) {
					
					canUpload = (currentTime - uploadingTask.getStartTime()) > MinimalUploadingInterval;
					
				} else {
					
					canUpload = true;
				}
				
				if (canUpload) {
					
					uploadingTask.setStartTime(currentTime);
					uploadingTask.setEndTime(0);
					uploadingTask.setFinished(false);
					UploadingTaskDaoExtensions.updateUploadingTaskInfo(getContext(), rescueTask, uploadingTask);
				
				} else {
					
					reportProgress();
					String tag = String.format("Upload blocked (%s)", getExecutorName());
					WebApiServerException exception = createWebApiServerException(rescueTask, tag);
					ExceptionsHandlingService.handleException(exception);
					return;
				}
			} else {
				
				UploadingTaskDaoExtensions.addUploadingTaskInfo(getContext(), rescueTask, currentTime);
				uploadingTask = new UploadingTaskInfo();
				uploadingTask.setRescueTaskId(rescueTask.getId());
				uploadingTask.setStartTime(currentTime);
			}
			
			_doUpload(rescueTask);
			
		} finally {
				
			uploadingTask.setFinished(true);
			uploadingTask.setEndTime(System.currentTimeMillis());
			UploadingTaskDaoExtensions.updateUploadingTaskInfo(getContext(), rescueTask, uploadingTask);
		}
	}
	
	protected void cleanRescueTask(RescueTaskDto rescueTask) {
		
		RescueTaskDaoExtensions.cleanRescueTask(getContext(), rescueTask.getId());
		
		RescueTaskPhotoManager.clearPhotoFilesOfRescueTask(getContext(), rescueTask);
	}
	
	protected WebApiServerException createWebApiServerException(RescueTaskDto rescueTask, String message) {
		
		WebApiServerException exception = new WebApiServerException(message, getContext());
		exception.setExecutorName(getExecutorName());
		exception.setCaseNumber(rescueTask.getCaseNumber());
		return exception;
	}
	
	public PhotoUploadingProgressListener getProgressListener() {
		return progressListener;
	}

	public void setProgressListener(PhotoUploadingProgressListener progressListener) {
		this.progressListener = progressListener;
	}
}
