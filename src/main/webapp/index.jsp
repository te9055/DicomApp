<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Dicom Image viewer</title>
    </head>

    <body>
        <center>
            <h1>
                Select Dicom image
            </h1>
            <form action="UploadDicom" enctype="multipart/form-data" method="post">
                 Select <input type="file" name="file2" accept=".dcm" /> <br>
                <input type="submit" value="upload" />
            </form>
        </center>
    </body>
</html>