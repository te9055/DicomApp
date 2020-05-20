package com.sample;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import java.io.File;
import java.io.IOException;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.dcm4che2.tool.dcm2jpg.Dcm2Jpg;

import static com.sample.AppProperties.OUTPUT_DIRECTORY;

@WebServlet(
        name = "selectdicomservlet",
        urlPatterns = "/UploadDicom"
)

public class SelectDicomServlet extends HttpServlet {


    private static final long serialVersionUID = 1;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        //PrintWriter out = response.getWriter();
        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            return;
        }
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> fields = upload.parseRequest(request);
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) {
                return;
            }
            while (it.hasNext()) {
                FileItem fileItem = it.next();
                boolean isFormField = fileItem.isFormField();

                if (!isFormField) {
                    Path outputFilePath = Paths.get(OUTPUT_DIRECTORY, fileItem.getName().concat(".jpg"));
                    if (fileItem.getSize() > 0) {
                        Files.deleteIfExists(outputFilePath);
                        try {
                            File src = ((DiskFileItem) fileItem).getStoreLocation();
                            File dest = outputFilePath.toFile();
                            Dcm2Jpg dcm2jpg = new Dcm2Jpg();
                            dcm2jpg.convert(src, dest);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            final String dir = System.getProperty("user.dir");
            System.out.println("current dir = " + dir);
            e.printStackTrace();

        } finally {
            RequestDispatcher view = request.getRequestDispatcher("result.jsp");
            view.forward(request, response);
        }
    }
}
