package com.sample;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
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
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.dcm4che2.tool.dcm2jpg.Dcm2Jpg;




@WebServlet(
        name = "selectdicomservlet",
        urlPatterns = "/UploadDicom"
)

public class SelectDicomServlet extends HttpServlet {


    private static final long serialVersionUID = 1 ;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String file_name = null;
        String fileName = "C:\\Servers\\apache-tomcat-9.0.34\\webapps\\DicomApp_war\\fileupload\\tmpfile.jpg";
        String outName = "C:\\Servers\\apache-tomcat-9.0.34\\webapps\\DicomApp_war\\fileupload\\tmpfile.jpg";

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
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
                if (isFormField) {
                    if (file_name == null) {
                        if (fileItem.getFieldName().equals("file_name")) {
                            file_name = fileItem.getString();
                        }
                    }
                } else {
                    if (fileItem.getSize() > 0) {
                        boolean result = Files.deleteIfExists(Paths.get(fileName));
                        fileItem.write(new File(fileName));
                    }
                }
            }
        } catch (Exception e) {
            final String dir = System.getProperty("user.dir");
            System.out.println("current dir = " + dir);
            e.printStackTrace();

        } finally {
            String dicomin = "D:\\dicom\\in.dcm";
            String jpegout = "D:\\dicom\\out.jpg";

            try{
                File src = new File(dicomin);
                File dest = new File(jpegout);
                Dcm2Jpg dcm2jpg = new Dcm2Jpg();
                dcm2jpg.convert(src, dest);
                System.out.println("Completed");
            } catch(IOException e){
                e.printStackTrace();
            }

            /*
            System.load("C:\\opencv\\OPENCV_BIN\\build\\java\\x64\\opencv_java430.dll");
            //String infile = fileName;
            String inFile = "D:\\dicom\\in.dcm";
            String outfile = "D:\\dicom\\out.jpg";

            System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);

            Dcm2Jpg conv = new Dcm2Jpg();
            try {
                conv.convert(new File(inFile), new File(outfile));
            } catch (Exception e){
                System.out.println(e);

            }*/
            RequestDispatcher view = request.getRequestDispatcher("result.jsp");
            view.forward(request, response);
            }
        }
    }
