<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Alta de libro</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h2>Sube tu libro</h2>

<form action="AltaLibro" method="post">
    <p><label for="isbn">Isbn: &nbsp</label><input type="text" id="isbn" name="isbn"></p>
    <%
        String isbnInc = (String)request.getAttribute("isbnIncorrecto");

        if (isbnInc != null){
    %>
    <p class="error"><%=isbnInc%></p>
    <%
        }
    %>
    <p><label for="titulo">Titulo: &nbsp</label><input type="text" id="titulo" name="titulo"></p>
    <%
        String campoInc = (String)request.getAttribute("campoIncorrecto");

        if (campoInc != null){
    %>
    <p class="error"><%=campoInc%></p>
    <%
        }
    %>
    <p><label for="autor">Autor: &nbsp</label><input type="text" id="autor" name="autor"></p>
    <%
        String campoInc2 = (String)request.getAttribute("campoIncorrecto2");

        if (campoInc2 != null){
    %>
    <p class="error"><%=campoInc2%></p>
    <%
        }
    %>

    <button type="submit">Enviar</button>
</form>
</body>
</html>
