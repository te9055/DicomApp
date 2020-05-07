package com.sample;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;



import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.dcm4che2.imageio.plugins.dcm.DicomImageReadParam;




@WebServlet(
        name = "selectdicomservlet",
        urlPatterns = "/UploadDicom"
)

public class SelectDicomServlet extends HttpServlet {

    static BufferedImage myJpegImage=null;

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
            List <FileItem> fields = upload.parseRequest(request);
            Iterator < FileItem > it = fields.iterator();
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
                File dicomimage = new File("d:\\dicom\\in.dcm");

                BufferedImage outputjpg = null;
                Iterator<ImageReader> iter = ImageIO.getImageReadersByFormatName("DICOM");

                ImageReader reader =  (ImageReader) iter.next();

                DicomImageReadParam param  = (DicomImageReadParam) reader.getDefaultReadParam();

                try{
                    ImageInputStream iis = ImageIO.createImageInputStream(dicomimage);
                    reader.setInput(iis,false);
                    outputjpg = reader.read(0,param);
                    iis.close();
                    if(outputjpg == null){
                        System.out.println("error with DICOM");
                        return;
                    }
                    File myJpegFile = new File("d:\\dicom\\out.jpg");
                    OutputStream output = new BufferedOutputStream(new FileOutputStream(myJpegFile));
                    ImageIO.write(myJpegImage,"jpeg",output);
                    output.close();
                }
                catch (IOException e){
                    System.out.println("error with DICOM");
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
