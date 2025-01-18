<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modifica un libro</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h2>Modifica un libro</h2>

<form action="ActualizaLibro" method="post">
    <p><label for="isbn">Isbn: &nbsp</label><input type="text" id="isbn" name="isbn"></p>
    <%
        String mensaje = (String)request.getAttribute("noExisteLibro");

        if (mensaje != null){
    %>
    <p class="error"><%=mensaje%></p>
    <%
        }
    %>
    <p><label for="titulo">Titulo: &nbsp</label><input type="text" id="titulo" name="titulo"></p>
    <p><label for="autor">Autor: &nbsp</label><input type="text" id="autor" name="autor"></p>

    <button type="submit">Enviar</button>
</form>
</body>
</html>
