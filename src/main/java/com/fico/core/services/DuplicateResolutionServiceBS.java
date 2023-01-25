package com.fico.core.services;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service("facade.DuplicateResolutionServiceBS")
public class DuplicateResolutionServiceBS {
	
/*	private static final Logger logger = LoggerFactory.getLogger(DuplicateResolutionServiceBS.class);

	@Autowired
	private CoreQueryExecutorService coreQueryExecutorService;
	
	
	public DuplicateResolutionReviewResponseWrapper getApplicationMatchDataset(Integer applicationId) throws Exception
	{
		DuplicateResolutionReviewResponseWrapper duplicateResolutionReviewResponseWrapper = new DuplicateResolutionReviewResponseWrapper();
		if(logger.isInfoEnabled())
			logger.info("--------Inside method 'getApplicationMatchDataset- applicationId received:'"
				+ applicationId);
		
		Page<QueryDuplicationResolutionReviewDatasetResponse> duplicateResolutionReviewPageData = null;
		Pageable pageable = null;
		int currentPage = 0;
		int pageSize = Integer.MAX_VALUE;
		
		try {
			pageable = PageRequest.of(currentPage, pageSize);

			// call the core query API
			duplicateResolutionReviewPageData = coreQueryExecutorService.executeQuery_DuplicationResolutionReviewDataset(applicationId,pageable);
			if (duplicateResolutionReviewPageData != null) {
				List<QueryDuplicationResolutionReviewDatasetResponse> duplicateCheckDataset = duplicateResolutionReviewPageData
						.toList();

				// set up the wrapped response data

				duplicateResolutionReviewResponseWrapper.setTotalElements(duplicateResolutionReviewPageData.getTotalElements());
				duplicateResolutionReviewResponseWrapper.setDuplicateResolutionReviewDataSet(duplicateCheckDataset);
				if(logger.isInfoEnabled())
					logger.info("--------Inside method 'getApplicationMatchDataset- total elements:'"
						+ duplicateResolutionReviewResponseWrapper.getTotalElements());
			}
		
		
		
		return duplicateResolutionReviewResponseWrapper;
	
	} catch (Exception e) {
		throw new Exception(e);
	}
		
}
	
	

	
	public class DuplicateResolutionReviewResponseWrapper
	{
		
		private List<QueryDuplicationResolutionReviewDatasetResponse> duplicateResolutionReviewDataSet;
		private int pageNumber;
        private int pageSize;
        private long totalElements;
        
        
		public List<QueryDuplicationResolutionReviewDatasetResponse> getDuplicateResolutionReviewDataSet() {
			return duplicateResolutionReviewDataSet;
		}
		public void setDuplicateResolutionReviewDataSet(
				List<QueryDuplicationResolutionReviewDatasetResponse> duplicateResolutionReviewDataSet) {
			this.duplicateResolutionReviewDataSet = duplicateResolutionReviewDataSet;
		}
		public int getPageNumber() {
			return pageNumber;
		}
		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}
		public int getPageSize() {
			return pageSize;
		}
		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
		public long getTotalElements() {
			return totalElements;
		}
		public void setTotalElements(long totalElements) {
			this.totalElements = totalElements;
		}
		
		
	}  */
}
