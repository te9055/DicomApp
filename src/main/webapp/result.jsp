<%@ page import ="java.util.*" %>
<%@ page import="java.nio.file.Files" %>
<%@ page import="static com.sample.AppProperties.OUTPUT_DIRECTORY" %>
<%@ page import="java.nio.file.Paths" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.nio.file.Path" %>
<%@ page import="java.util.function.Predicate" %>
<%@ page import="java.util.function.Function" %>
<!DOCTYPE html>
<html>
    <head>

    </head>

    <body>
        <center>
            <h1>
                DICOM Files
            </h1>
            <%
                List<Object> fileList = Files.list(Paths.get(OUTPUT_DIRECTORY)).filter(new Predicate<Path>() {
                    @Override
                    public boolean test(Path path) {
                        return path.getFileName().toString().endsWith(".jpg");
                    }
                }).map(new Function<Path, String>() {
                    @Override
                    public String apply(Path path) {
                        return path.toFile().getName();
                    }
                }).collect(Collectors.toList());
            %>
            <% for (Object o : fileList) { %>
            <p><%=o%></p>
            <img src="./files/<%=o%>"/>
            <% } %>
        </center>
    </body>
</html>