package com.fico.core.services;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fico.core.handlers.ReportFileHandler;
import com.fico.ps.model.queue.QueueReportResponseWrapper;
import com.fico.pscomponent.model.ApplicationDashboardReportDTO;
import com.fico.pscomponent.model.ManualReviewDashboardReportDTO;
import com.fico.pscomponent.model.OperatorPerformanceReportDTO;
@Service("facade.ReportFileServiceBS")
public class ReportFileServiceBS {

}
