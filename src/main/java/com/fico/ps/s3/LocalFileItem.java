package com.fico.ps.s3;

import com.wavemaker.runtime.file.model.Downloadable;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class LocalFileItem implements FileItem {

    private static final long serialVersionUID = 2467880290855097332L;

    private Logger logger = LoggerFactory.getLogger(LocalFileItem.class);

    private String contentType;

    private final Downloadable downloadable;

    private String fileName;

    public LocalFileItem(Downloadable downloadable) {
        this.downloadable = downloadable;
        this.fileName = downloadable.getFileName();
    }

    public void renameTo(String newName){
        this.fileName = newName;
    }

    @Override
    public void write(File newFile) throws Exception {
        OutputStream outStream = new FileOutputStream(newFile);

        byte[] buffer = new byte[8 * 1024];
        int bytesRead;
        while ((bytesRead = this.downloadable.getContents().read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        IOUtils.closeQuietly(this.downloadable.getContents());
        IOUtils.closeQuietly(outStream);
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.downloadable.getContents();
    }

    @Override
    public long getSize() {

        // Spring's CommonsMultipartFile caches the file size and uses it to determine availability.
        long size = -1L;
        try {
            size = this.downloadable.getContents().available();
        } catch (IOException ignored) {}
        return size;
    }

    @Override
    public void delete() {
        File file = new File(this.fileName);
        file.delete();
    }

    @Override
    public String getName() {
        return this.fileName;
    }

    /* *** properties and unsupported methods *** */

    private FileItemHeaders headers;

    private String fieldName;
    private boolean formField;

    @Override
    public FileItemHeaders getHeaders() {
        return headers;
    }

    @Override
    public void setHeaders(FileItemHeaders headers) {
        this.headers = headers;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isInMemory() {
        return false;
    }

    @Override
    public byte[] get() {
        throw new RuntimeException("Only method write(File) is supported.");
    }

    @Override
    public String getString(String encoding)
            throws UnsupportedEncodingException {
        throw new RuntimeException("Only method write(File) is supported.");
    }

    @Override
    public String getString() {
        throw new RuntimeException("Only method write(File) is supported.");
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public void setFieldName(String name) {
        this.fieldName = name;
    }

    @Override
    public boolean isFormField() {
        return formField;
    }

    @Override
    public void setFormField(boolean state) {
        this.formField = state;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("Only method write(File) is supported.");
    }

    @Override
    public String toString() {
        return "LocalFileItem{" +
                "contentType='" + contentType + '\'' +
                ", downloadable=" + downloadable +
                ", headers=" + headers +
                ", fieldName='" + fieldName + '\'' +
                ", formField=" + formField +
                '}';
    }
}
